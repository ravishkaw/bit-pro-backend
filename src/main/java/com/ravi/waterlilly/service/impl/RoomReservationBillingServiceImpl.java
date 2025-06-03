package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.model.*;
import com.ravi.waterlilly.payload.billingDiscount.DiscountReferenceDTO;
import com.ravi.waterlilly.payload.billingDiscount.DiscountWithAmountDTO;
import com.ravi.waterlilly.payload.billingTaxes.TaxReferenceDTO;
import com.ravi.waterlilly.payload.billingTaxes.TaxWithAmountDTO;
import com.ravi.waterlilly.payload.roomReservationAmenity.AmenityNameQuantityDTO;
import com.ravi.waterlilly.payload.roomReservationBilling.*;
import com.ravi.waterlilly.service.*;
import com.ravi.waterlilly.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

// implementation of billing service impl
@Service
@RequiredArgsConstructor
public class RoomReservationBillingServiceImpl implements RoomReservationBillingService {
    private final RoomService roomService;
    private final RoomPackageService roomPackageService;
    private final DiscountService discountService;
    private final TaxService taxService;
    private final RoomReservationAmenityService roomReservationAmenityService;
    private final RoomReservationService roomReservationService;
    private final LateCheckinPolicyService lateCheckinPolicyService;
    private final EarlyCheckoutPolicyService earlyCheckoutPolicyService;
    private final ModelMapper modelMapper;
    private final DateTimeUtils dateTimeUtils;

    // calculate price for room reservation when reserving
    @Override
    public RoomReservationBillingDTO calculatePrice(RoomReservationBillingPayloadDTO billingDTO) {
        // set values
        Room room = roomService.getRoomById(billingDTO.getRoomId());
        RoomPackage roomPackage = roomPackageService.getRoomPackageById(billingDTO.getRoomPackageId());
        LocalDateTime checkInDate = billingDTO.getCheckInDate();
        LocalDateTime checkOutDate = billingDTO.getCheckOutDate();
        long numberOfNights = dateTimeUtils.getNumberOfNights(checkInDate, checkOutDate);

        // calculate price
        BigDecimal roomPrice = roomService.calculateReservationTotalPrice(room, checkInDate, checkOutDate);
        BigDecimal packagePrice = roomPackageService.calculateRoomPackagePrice(roomPackage);
        BigDecimal amenityPrice = roomReservationAmenityService.calculateAmenityPrice(billingDTO.getAmenities());

        BigDecimal basePrice = roomPrice
                .add(packagePrice.multiply(BigDecimal.valueOf(numberOfNights)))
                .add(amenityPrice.multiply(BigDecimal.valueOf(numberOfNights)));

        DiscountWithAmountDTO discountWithAmount = discountService.calculateDiscount(basePrice, checkInDate.toLocalDate());
        DiscountReferenceDTO discount = discountWithAmount.getDiscount() != null ? discountWithAmount.getDiscount() : null;
        BigDecimal discountAmount = discountWithAmount.getAmount();

        TaxWithAmountDTO taxWithAmount = taxService.calculateTaxes(basePrice.subtract(discountWithAmount.getAmount()));
        TaxReferenceDTO taxReferenceDTO = modelMapper.map(taxWithAmount.getTax(), TaxReferenceDTO.class);
        BigDecimal taxesAmount = taxWithAmount.getAmount();

        BigDecimal totalPrice = basePrice.subtract(discountAmount).add(taxesAmount).setScale(2, RoundingMode.HALF_UP);

        List<AmenityNameQuantityDTO> nameQuantityDTOS = getAmenityNameQuantityDTOS(billingDTO);

        return new RoomReservationBillingDTO(
                room.getNumber(),
                basePrice,
                discount,
                discountAmount,
                taxReferenceDTO,
                taxesAmount,
                totalPrice,
                totalPrice,
                checkInDate,
                checkOutDate,
                roomPackage.getName(),
                nameQuantityDTOS
        );
    }

    // calculate price when check out
    @Override
    public CheckOutBillingDTO calculatePriceForCheckOut(Long reservationId) {

        RoomReservation roomReservation = roomReservationService.getRoomReservationById(reservationId);
        Billing billingRecord = roomReservation.getBilling();

        // Dates
        LocalDateTime reservationCheckInDate = roomReservation.getReservedCheckInDate();
        LocalDateTime reservationCheckOutDate = roomReservation.getReservedCheckOutDate();
        LocalDateTime actualCheckInDate = roomReservation.getCheckInDate();
        LocalDateTime rawCheckOutTime = LocalDateTime.now();

        // Apply grace period - this is the key change
        LocalDateTime effectiveCheckOutTime = getEffectiveCheckoutTime(rawCheckOutTime, reservationCheckOutDate);

        // Billing details
        BigDecimal basePrice = billingRecord.getBasePrice();
        BigDecimal paidAmount = billingRecord.getPaidAmount();
        BigDecimal discountAmount = billingRecord.getDiscountAmount();
        BigDecimal taxAmount = billingRecord.getTotalTax();
        Integer discountId = billingRecord.getDiscount() != null ? billingRecord.getDiscount().getId() : null;
        Integer taxId = billingRecord.getTax() != null ? billingRecord.getTax().getId() : null;

        // Calculate nights using effective checkout time
        long reservedNights = dateTimeUtils.getNumberOfNights(reservationCheckInDate, reservationCheckOutDate);
        long actualNights = dateTimeUtils.getNumberOfNights(actualCheckInDate, effectiveCheckOutTime); // Use effective time

        // Overstay calculation - now much simpler
        long overstayNights = Math.max(0, actualNights - reservedNights);
        boolean isOverstay = overstayNights > 0;

        BigDecimal dailyRate = basePrice.divide(BigDecimal.valueOf(reservedNights), RoundingMode.HALF_UP);

        BigDecimal newBasePrice = basePrice;
        boolean isBasePriceAdjusted = false;
        if (isOverstay) {
            newBasePrice = basePrice.add(dailyRate.multiply(BigDecimal.valueOf(overstayNights)));
            isBasePriceAdjusted = true;
        }

        // Discount (even if now inactive) and taxes calculation
        BigDecimal newDiscountAmount = discountAmount;
        BigDecimal newTaxAmount = taxAmount;

        if (isBasePriceAdjusted) {
            if (billingRecord.getDiscount() != null) {
                newDiscountAmount = discountService.getDiscountByCode(billingRecord.getDiscount().getCode(), newBasePrice);
            }
            newTaxAmount = taxService.calculateTaxes(newBasePrice.subtract(newDiscountAmount)).getAmount();
        }

        // Check-in lateness
        Duration lateDuration = Duration.between(reservationCheckInDate, actualCheckInDate);
        long hoursLate = lateDuration.toHours();

        boolean checkInLate = hoursLate > 0;
        LateCheckinPolicy lateCheckinPolicy = checkInLate ? lateCheckinPolicyService.getApplicablePolicy((int) hoursLate) : null;
        BigDecimal lateFee = lateCheckinPolicy != null ? lateCheckinPolicy.getLateCheckinFee() : BigDecimal.ZERO;

        // Early checkout check
        boolean leftEarly = actualNights < reservedNights;
        long availableNightsFromActualCheckin = 0;
        if (leftEarly) {
            // Calculate how many nights were available from actual check-in to reserved checkout
            availableNightsFromActualCheckin = dateTimeUtils.getNumberOfNights(actualCheckInDate, reservationCheckOutDate);
        }
        EarlyCheckoutPolicy earlyCheckoutPolicy = leftEarly ?
                earlyCheckoutPolicyService.getApplicablePolicy((int) reservedNights, (int) actualNights, (int) availableNightsFromActualCheckin)
                : null;

        BigDecimal earlyPenalty = leftEarly && earlyCheckoutPolicy != null ? earlyCheckoutPolicy.getEarlyCheckoutFee() : BigDecimal.ZERO;

        // Early checkout refund
        BigDecimal refundPrice = BigDecimal.ZERO;
        if (leftEarly && earlyCheckoutPolicy != null) {

            // Only calculate refund if guest left before using all available nights from their actual check-in
            if (actualNights < availableNightsFromActualCheckin) {
                BigDecimal unusedNights = BigDecimal.valueOf(availableNightsFromActualCheckin - actualNights);
                BigDecimal refundPercentage = earlyCheckoutPolicy.getPartialRefundPercentage();
                refundPrice = unusedNights.multiply(dailyRate).multiply(refundPercentage).setScale(0, RoundingMode.HALF_UP);
            }
        }

        BigDecimal newTotalPrice = newBasePrice.subtract(newDiscountAmount).add(newTaxAmount)
                .add(lateFee).add(earlyPenalty)
                .setScale(2, RoundingMode.HALF_UP);

        // Net amount after refund
        BigDecimal netAmountPayable = newTotalPrice.subtract(refundPrice).setScale(2, RoundingMode.HALF_UP);

        // Notes
        StringBuilder noteBuilder = new StringBuilder();

        // Track if any adjustments were made
        boolean hasAdjustments = false;

        // Base price adjustment for overstay
        if (isOverstay) {
            hasAdjustments = true;
            BigDecimal extraCharge = dailyRate.multiply(BigDecimal.valueOf(overstayNights));
            noteBuilder.append("OVERSTAY ADJUSTMENT:\n");
            noteBuilder.append(String.format("• Guest stayed %d extra night(s) beyond reservation\n", overstayNights));
            noteBuilder.append(String.format("• Additional charge: Rs. %s (%d nights × Rs. %s daily rate)\n", extraCharge, overstayNights, dailyRate));
            noteBuilder.append(String.format("• Base price adjusted: Rs. %s → Rs. %s\n", basePrice, newBasePrice));
            noteBuilder.append(String.format("• Discounts : Rs. %s and taxes : Rs. %s\n\n", newDiscountAmount, newTaxAmount));
        }

        // Late check-in fee
        if (checkInLate && lateCheckinPolicy != null) {
            hasAdjustments = true;
            noteBuilder.append("LATE CHECK-IN ADJUSTMENT:\n");
            noteBuilder.append(String.format("• Guest arrived %d hour(s) after reserved time\n", hoursLate));
            noteBuilder.append(String.format("• Reserved: %s | Actual: %s\n",
                    reservationCheckInDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")),
                    actualCheckInDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))));
            noteBuilder.append(String.format("• Late fee applied: Rs. %s\n\n", lateFee));
        }

        // Early checkout penalty and refund
        if (leftEarly && earlyCheckoutPolicy != null) {
            hasAdjustments = true;
            long unusedNights = availableNightsFromActualCheckin - actualNights;
            noteBuilder.append("EARLY CHECKOUT ADJUSTMENT:\n");
            noteBuilder.append(String.format("• Guest left %d night(s) before reserved checkout\n", unusedNights));
            noteBuilder.append(String.format("• Reserved: %d nights | Actual: %d nights | Unused: %d nights\n", reservedNights, actualNights, unusedNights));
            noteBuilder.append(String.format("• Early checkout penalty: Rs. %s\n", earlyPenalty));

            if (refundPrice.compareTo(BigDecimal.ZERO) > 0) {
                noteBuilder.append(String.format("• Partial refund for unused nights: Rs. %s (%.0f%% × Rs. %s daily rate × %d unused night(s))\n",
                        refundPrice,
                        earlyCheckoutPolicy.getPartialRefundPercentage().multiply(BigDecimal.valueOf(100)),
                        dailyRate,
                        unusedNights));
            }
            noteBuilder.append("\n");
        }

        // Show adjustment summary only if there were adjustments
        if (hasAdjustments) {
            noteBuilder.append("ADJUSTMENT SUMMARY:\n");
            noteBuilder.append(String.format("• Original total: Rs. %s\n", billingRecord.getTotalPrice()));
            noteBuilder.append(String.format("• Revised total (original + penalties - discounts + taxes): Rs. %s\n", newTotalPrice));

            if (refundPrice.compareTo(BigDecimal.ZERO) > 0) {
                noteBuilder.append(String.format("• Net amount (revised total - unused night refunds): Rs. %s\n", netAmountPayable));
            }
            noteBuilder.append("\n");
        }

        // Refund information - only show if there's a refund
        BigDecimal overpaymentRefund = BigDecimal.ZERO;
        BigDecimal totalRefundDue = paidAmount.subtract(netAmountPayable);
        if (totalRefundDue.compareTo(BigDecimal.ZERO) > 0) {
            noteBuilder.append("REFUNDS:\n");
            noteBuilder.append(String.format("• Total refund due to guest: Rs. %s\n", totalRefundDue));
            overpaymentRefund = totalRefundDue;
            noteBuilder.append("\n");
        }

        // Add processing timestamp
        noteBuilder.append(String.format("Processed: %s\n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm"))));

        String note = noteBuilder.toString();

        return new CheckOutBillingDTO(
                newBasePrice,
                discountId,
                newDiscountAmount,
                taxId,
                newTaxAmount,
                newTotalPrice,
                netAmountPayable,
                paidAmount,
                (int) actualNights,
                effectiveCheckOutTime,
                note,
                overpaymentRefund
        );
    }

    // get amenity name and quantity
    private List<AmenityNameQuantityDTO> getAmenityNameQuantityDTOS(RoomReservationBillingPayloadDTO billingDTO) {
        return billingDTO.getAmenities().stream()
                .map(idQuantityDTOs -> {
                    AmenityNameQuantityDTO dto = new AmenityNameQuantityDTO();
                    RoomReservationAmenity amenity = roomReservationAmenityService.getAmenityById(idQuantityDTOs.getAmenityId());
                    dto.setAmenityName(amenity.getName());
                    dto.setQuantity(idQuantityDTOs.getQuantity());
                    return dto;
                })
                .toList();
    }

    // apply checkout grace period logic
    private LocalDateTime getEffectiveCheckoutTime(LocalDateTime actualCheckOutTime, LocalDateTime reservedCheckOutTime) {
        int hour = actualCheckOutTime.getHour();

        // Apply universal grace: if checkout time is before 12 PM, treat as 10 AM (standard checkout)
        if (hour < 12) {
            return actualCheckOutTime.withHour(AppConstants.DEFAULT_CHECK_OUT_TIME.getHour())
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);
        }

        // After 12 PM, use actual checkout time
        return actualCheckOutTime;
    }
}