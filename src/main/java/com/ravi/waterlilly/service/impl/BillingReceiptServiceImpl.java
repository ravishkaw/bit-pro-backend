package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Billing;
import com.ravi.waterlilly.model.Guest;
import com.ravi.waterlilly.model.Room;
import com.ravi.waterlilly.model.RoomReservation;
import com.ravi.waterlilly.payload.pdfGenerator.LineItem;
import com.ravi.waterlilly.repository.RoomReservationRepository;
import com.ravi.waterlilly.service.BillingReceiptService;
import com.ravi.waterlilly.utils.DateTimeUtils;
import com.ravi.waterlilly.utils.PdfGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingReceiptServiceImpl implements BillingReceiptService {

    private final PdfGenerator pdfGenerator;
    private final RoomReservationRepository roomReservationRepository;
    private final DateTimeUtils dateTimeUtils;

    @Value("${app.billing.receipt.storage.path:pdf/receipts}")
    private String receiptStoragePath;

    // Generates and stores a detailed checkout receipt
    @Override
    public String generateAndStoreCheckoutReceipt(Long reservationId) {
        try {
            // Get reservation details
            RoomReservation reservation = getRoomReservationById(reservationId);
            Guest guest = reservation.getPrimaryGuest();
            Room room = reservation.getRoom();
            Billing billing = reservation.getBilling();

            // Generate detailed receipt
            byte[] receiptPdf = generateDetailedCheckoutReceipt(billing, reservation, guest, room);

            // Store the receipt
            return storeReceipt(receiptPdf, reservation.getBilling().getId());

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate checkout receipt", e);
        }
    }

    // Generates detailed checkout receipt
    private byte[] generateDetailedCheckoutReceipt(Billing billing, RoomReservation reservation, Guest guest, Room room) {

        Long actualNights = dateTimeUtils.getNumberOfNights(reservation.getCheckInDate(), reservation.getCheckOutDate());

        // Create detailed line items from the checkout calculation
        List<LineItem> lineItems = createDetailedLineItems(billing, reservation, actualNights);

        // Generate invoice number
        String invoiceNo = billing.getId().toString();

        BigDecimal refundAmount = billing.getTotalPrice().subtract(billing.getNetAmount());

        return pdfGenerator.generateDetailedHotelReceipt(
                PdfGenerator.DocumentType.RECEIPT,
                invoiceNo,
                reservation.getCheckOutDate(),
                null, // Due date not needed for receipts
                guest.getFullName(),
                guest.getEmail(),
                guest.getMobileNo(),
                guest.getAddress(),
                room.getNumber().toString(),
                room.getRoomType().getName(),
                reservation.getReservedCheckInDate(),
                reservation.getReservedCheckOutDate(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                actualNights.intValue(),
                lineItems,
                billing.getBasePrice(),
                billing.getDiscountAmount(),
                billing.getTotalTax(),
                billing.getTotalPrice(),
                billing.getNetAmount(),
                refundAmount,
                billing.getPaymentMethod().getName(),
                "PAID"
        );
    }

    // Creates detailed line items for the receipt showing all charges and adjustments
    private List<LineItem> createDetailedLineItems(Billing billing, RoomReservation reservation, Long actualNights) {
        List<LineItem> lineItems = new ArrayList<>();

        // Calculate values from reservation and billing data
        Long reservedNights = dateTimeUtils.getNumberOfNights(reservation.getReservedCheckInDate(), reservation.getReservedCheckOutDate());

        // Calculate daily rate from base price and reserved nights
        BigDecimal dailyRate = reservedNights > 0 ?
                billing.getBasePrice().divide(BigDecimal.valueOf(actualNights), 2, BigDecimal.ROUND_HALF_UP) :
                BigDecimal.ZERO;

        // Base accommodation charge (always show original reserved nights)
        lineItems.add(new LineItem(
                "Room Accommodation",
                String.format("%d nights × Rs. %s", reservedNights, dailyRate),
                dailyRate.multiply(BigDecimal.valueOf(reservedNights)),
                LineItem.LineItemType.CHARGE
        ));

        // Parse notes to detect adjustments
        String notes = billing.getNote();

        // Extended stay charges (overstay)
        if (actualNights > reservedNights) {
            long extraNights = actualNights - reservedNights;
            BigDecimal extraCharge = dailyRate.multiply(BigDecimal.valueOf(extraNights));
            lineItems.add(new LineItem(
                    "Extended Stay (Overstay)",
                    String.format("%d extra night(s) × Rs. %s", extraNights, dailyRate),
                    extraCharge,
                    LineItem.LineItemType.CHARGE
            ));
        }

        // Late check-in fee (detect from notes)
        if (notes.contains("LATE CHECK-IN ADJUSTMENT:")) {
            // Extract late fee from notes - look for pattern "Late fee applied: Rs. X"
            BigDecimal lateFee = extractAmountFromNotes(notes, "Late fee applied: Rs. ");
            if (lateFee.compareTo(BigDecimal.ZERO) > 0) {
                lineItems.add(new LineItem(
                        "Late Check-in Fee",
                        "Fee for checking in after scheduled time",
                        lateFee,
                        LineItem.LineItemType.FEE
                ));
            }
        }

        // Early checkout penalty (detect from notes)
        if (notes.contains("EARLY CHECKOUT ADJUSTMENT:")) {
            // Extract penalty from notes - look for pattern "Early checkout penalty: Rs. X"
            BigDecimal earlyPenalty = extractAmountFromNotes(notes, "Early checkout penalty: Rs. ");
            if (earlyPenalty.compareTo(BigDecimal.ZERO) > 0) {
                lineItems.add(new LineItem(
                        "Early Checkout Penalty",
                        "Penalty for checking out before reserved date",
                        earlyPenalty,
                        LineItem.LineItemType.PENALTY
                ));
            }
        }

        // Early checkout refund (detect from notes)
        if (notes.contains("Partial refund for unused nights:")) {
            BigDecimal refundPrice = extractAmountFromNotes(notes, "Partial refund for unused nights: Rs. ");
            if (refundPrice.compareTo(BigDecimal.ZERO) > 0) {
                long refundNights = reservedNights - actualNights;
                lineItems.add(new LineItem(
                        "Early Checkout Refund",
                        String.format("Partial refund for %d unused night(s)", refundNights),
                        refundPrice,
                        LineItem.LineItemType.REFUND
                ));
            }
        }

        // Discount
        if (billing.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
            lineItems.add(new LineItem(
                    "Discount Applied",
                    "Promotional discount",
                    billing.getDiscountAmount(),
                    LineItem.LineItemType.DISCOUNT
            ));
        }

        // Tax
        if (billing.getTotalTax().compareTo(BigDecimal.ZERO) > 0) {
            lineItems.add(new LineItem(
                    "Government Tax & Service Charge",
                    "Applicable taxes and charges",
                    billing.getTotalTax(),
                    LineItem.LineItemType.TAX
            ));
        }

        return lineItems;
    }

    // Utility method to extract amount from notes text
    private BigDecimal extractAmountFromNotes(String notes, String pattern) {
        try {
            int startIndex = notes.indexOf(pattern);
            if (startIndex == -1) return BigDecimal.ZERO;

            startIndex += pattern.length();
            int endIndex = notes.indexOf('\n', startIndex);
            if (endIndex == -1) endIndex = notes.length();

            String amountStr = notes.substring(startIndex, endIndex).trim();

            // Handle patterns like "Rs. 500 (2 nights × Rs. 250 daily rate)"
            if (amountStr.contains(" (")) {
                amountStr = amountStr.substring(0, amountStr.indexOf(" (")).trim();
            }

            return new BigDecimal(amountStr);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    // Stores the receipt PDF file directly in the billing/receipts directory
    private String storeReceipt(byte[] receiptPdf, Long billingId) throws IOException {
        // Create the billing/receipts directory if it doesn't exist
        Path receiptDir = Paths.get(receiptStoragePath);
        Files.createDirectories(receiptDir);

        // Generate simple filename: {billingId}.pdf
        String fileName = String.format("%d.pdf", billingId);

        Path receiptPath = receiptDir.resolve(fileName);

        // Write the PDF file
        Files.write(receiptPath, receiptPdf, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        return fileName;
    }

    // method to get room reservation by id
    private RoomReservation getRoomReservationById(Long reservationId) {
        return roomReservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Room Reservation", "reservationId", reservationId));
    }
}