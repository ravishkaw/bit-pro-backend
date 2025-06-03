package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.*;
import com.ravi.waterlilly.payload.billing.BillingPayloadDTO;
import com.ravi.waterlilly.payload.roomReservation.*;
import com.ravi.waterlilly.payload.roomReservationAmenity.AmenityQuantityDTO;
import com.ravi.waterlilly.repository.*;
import com.ravi.waterlilly.service.*;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// implementation of room reservation service
@Service
@RequiredArgsConstructor
public class RoomReservationServiceImpl implements RoomReservationService {
    private final PrivilegeUtils privilegeUtils;
    private final RoomReservationRepository roomReservationRepository;
    private final ModelMapper modelMapper;
    private final RoomService roomService;
    private final BillingService billingService;
    private final RoomPackageService roomPackageService;
    private final TaskService taskService;
    private final RoomReservationTypeService roomReservationTypeService;
    private final RoomReservationStatusService roomReservationStatusService;
    private final RoomReservationSourceService roomReservationSourceService;
    private final GuestService guestService;
    private final RoomReservationAmenityService roomReservationAmenityService;
    private final RoomStatusService roomStatusService;
    private final ChildService childService;
    private final BillingReceiptService billingReceiptService;

    // get all room reservations
    @Override
    public RoomReservationPageResponse getAllReservations(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String status, String searchQuery) {

        // check privileges
        privilegeUtils.privilegeCheck("Room Reservation", AppConstants.SELECT);

        // sorting
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // pagination
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<RoomReservation> roomReservationPage = StringUtils.hasText(searchQuery)
                ? roomReservationRepository.searchReservation(searchQuery.trim(), pageable)
                : roomReservationRepository.getReservationByStatus(status, pageable);

        // Extract the list of room reservations from the page
        List<RoomReservation> roomReservations = roomReservationPage.getContent();

        // Map the list of room reservations
        List<RoomReservationTableData> tableData = roomReservations.stream()
                .map(this::mapToTableDataDTO)
                .toList();

        return new RoomReservationPageResponse(
                tableData,
                roomReservationPage.getNumber(),
                roomReservationPage.getSize(),
                roomReservationPage.getTotalElements(),
                roomReservationPage.getTotalPages(),
                roomReservationPage.isLast()
        );
    }

    // fetch single room reservation
    @Override
    public RoomReservationPayloadDTO getSingleReservation(Long reservationId) {

        // check privileges
        privilegeUtils.privilegeCheck("Room Reservation", AppConstants.SELECT);

        RoomReservation roomReservation = getRoomReservationById(reservationId);

        RoomReservationPayloadDTO roomReservationDTO = modelMapper.map(roomReservation, RoomReservationPayloadDTO.class);

        // convert amenities with quantity
        List<AmenityQuantityDTO> amenities = roomReservation.getReservationHasAmenities().stream()
                .map(entry -> {
                    AmenityQuantityDTO aq = new AmenityQuantityDTO();
                    aq.setAmenityId(entry.getRoomReservationAmenity().getId());
                    aq.setQuantity(entry.getQuantity());
                    return aq;
                }).toList();

        // guest id and children list
        Set<Long> guests = roomReservation.getGuests()
                .stream().map(Guest::getId)
                .collect(Collectors.toSet());
        Set<Long> children = roomReservation.getChildren().stream()
                .map(Child::getId)
                .collect(Collectors.toSet());

        roomReservationDTO.setAmenities(amenities);
        roomReservationDTO.setGuestIds(guests);
        roomReservationDTO.setChildIds(children);
        return roomReservationDTO;
    }

    // add new room reservation
    @Override
    @Transactional
    public void addRoomReservation(RoomReservationPayloadFormDTO formDTO) {
        // check privileges
        privilegeUtils.privilegeCheck("Room Reservation", AppConstants.INSERT);

        // Validate dates
        validateCheckInBeforeCheckOut(formDTO);
        validateCheckInNotInPast(formDTO);

        // Check room availability
        roomService.validateRoomAvailability(formDTO.getRoomId(), formDTO.getReservedCheckInDate(), formDTO.getReservedCheckOutDate());
        checkPricePaid(formDTO);

        // Create and update reservation
        RoomReservation reservation = new RoomReservation();
        updateRoomReservationFields(reservation, formDTO);

        // update check in time if status is checked in
        if (formDTO.getRoomReservationStatusId().equals(roomReservationStatusService.getCheckedInStatus().getId())) {
            reservation.setCheckInDate(LocalDateTime.now());
        }

        roomReservationRepository.save(reservation);
    }

    // update room reservation
    @Override
    @Transactional
    public void updateRoomReservation(Long reservationId, RoomReservationPayloadFormDTO formDTO) {
        // check privileges
        privilegeUtils.privilegeCheck("Room Reservation", AppConstants.UPDATE);

        // Fetch existing reservation
        RoomReservation existingReservation = getRoomReservationById(reservationId);

        // Validate status allows updates
        validateReservationUpdateAllowed(existingReservation);
        checkPricePaid(formDTO);

        // Validate dates
        validateCheckInBeforeCheckOut(formDTO);

        // Check room availability (exclude current reservation from check)
        if (!formDTO.getRoomId().equals(existingReservation.getRoom().getId())) {
            validateRoomAvailability(formDTO.getRoomId(),
                    formDTO.getReservedCheckInDate(),
                    formDTO.getReservedCheckOutDate(),
                    reservationId
            );
        }

        // Update reservation fields
        updateRoomReservationFields(existingReservation, formDTO);
        roomReservationRepository.save(existingReservation);
    }

    // update status of the room for a action
    @Override
    @Transactional
    public void updateStatusToAction(Long reservationId, String action) {

        // check privileges
        privilegeUtils.privilegeCheck("Room Reservation", AppConstants.UPDATE);

        RoomReservation reservation = getRoomReservationById(reservationId);
        manageCheckInAction(action, reservation);
        roomReservationStatusService.updateRoomReservationToAction(reservation, action);
    }

    // room reservation check out
    @Override
    @Transactional
    public String reservationCheckOut(Long reservationId, CheckOutDTO checkOutDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Room Reservation", AppConstants.UPDATE);

        RoomReservation reservation = getRoomReservationById(reservationId);

        reservation.setCheckOutDate(checkOutDTO.getCheckOutDate());
        reservation.setRoomReservationStatus(roomReservationStatusService.getStatusByName("CHECKED-OUT"));
        updateBillingRecords(reservation, checkOutDTO.getBillingPayloadDTO());
        roomReservationRepository.save(reservation);

        String receiptName = billingReceiptService.generateAndStoreCheckoutReceipt(reservationId);
        String receiptUrl = "/receipts/" + receiptName;

        Long roomId = reservation.getRoom().getId();
        roomStatusService.setRoomToNeedsCleaning(roomId);
        // add task to clean the room
        taskService.addTaskWhenRoomCheckingOut(roomId);
        return receiptUrl;
    }

    // method to get room reservation by id
    @Override
    public RoomReservation getRoomReservationById(Long reservationId) {
        return roomReservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Room Reservation", "reservationId", reservationId));
    }

    // Helper method to update room reservation fields
    private void updateRoomReservationFields(RoomReservation reservation, RoomReservationPayloadFormDTO formDTO) {
        reservation.setRoom(roomService.getRoomById(formDTO.getRoomId()));
        reservation.setPrimaryGuest(guestService.getGuestById(formDTO.getPrimaryGuestId()));
        reservation.setReservedCheckInDate(formDTO.getReservedCheckInDate().atTime(AppConstants.DEFAULT_CHECK_IN_TIME));
        reservation.setReservedCheckOutDate(formDTO.getReservedCheckOutDate().atTime(AppConstants.DEFAULT_CHECK_OUT_TIME));
        reservation.setCheckInDate(formDTO.getCheckInDate());
        reservation.setCheckOutDate(formDTO.getCheckOutDate());
        reservation.setAdultNo(formDTO.getAdultNo());
        reservation.setChildNo(formDTO.getChildNo());
        reservation.setInfantNo(formDTO.getInfantNo());
        reservation.setNote(formDTO.getNote());
        reservation.setRoomReservationType(roomReservationTypeService.getReservationTypeById(formDTO.getReservationTypeId()));
        reservation.setRoomReservationStatus(roomReservationStatusService.getStatusById(formDTO.getRoomReservationStatusId()));
        reservation.setRoomReservationSource(roomReservationSourceService.getSourceById(formDTO.getRoomReservationSourceId()));
        reservation.setRoomPackage(roomPackageService.getRoomPackageById(formDTO.getRoomPackageId()));
        reservation.setGuests(fetchGuests(formDTO.getGuestIds()));
        reservation.setChildren(fetchChildren(formDTO.getChildIds()));

        LocalDateTime now = LocalDateTime.now();
        if (reservation.getId() == null) {
            reservation.setAddedDatetime(now);
        }
        reservation.setLastModifiedDatetime(now);

        updateBillingRecords(reservation, formDTO.getBillingPayloadDTO());
        updateAmenities(formDTO, reservation);
    }

    // Helper method to update amenities
    private void updateAmenities(RoomReservationPayloadFormDTO formDTO, RoomReservation reservation) {
        if (formDTO.getAmenities() != null) {
            if (reservation.getReservationHasAmenities() != null && !reservation.getReservationHasAmenities().isEmpty()) {
                reservation.clearAmenities();
            }
            formDTO.getAmenities().forEach(amenity -> {
                RoomReservationAmenity roomAmenity = roomReservationAmenityService.getAmenityById(amenity.getAmenityId());
                reservation.addAmenity(roomAmenity, amenity.getQuantity());
            });
        }
    }

    // Helper method to update billing records
    private void updateBillingRecords(RoomReservation reservation, BillingPayloadDTO billingPayloadDTO) {
        if (reservation.getBilling() != null && reservation.getBilling().getId() != null) {
            billingService.updateBillingDetails(reservation.getBilling().getId(), billingPayloadDTO);
        } else {
            Billing billing = billingService.createNewBillingDetails(billingPayloadDTO);
            reservation.setBilling(billing);
        }
    }

    // Helper method to manage check in action
    private void manageCheckInAction(String action, RoomReservation reservation) {
        if (action.equals("check-in")) {
            LocalDateTime reservedCheckInDate = reservation.getReservedCheckInDate();
            LocalDateTime checkInDate = LocalDateTime.now();
            LocalDateTime earliestCheckInTime = reservedCheckInDate.minusMinutes(15); // Allow 15 minutes early check-in

            if (checkInDate.isBefore(earliestCheckInTime)) {
                throw new APIException("Check-in is only allowed from 15 minutes before the reserved check-in time");
            }

            if (checkInDate.isBefore(reservedCheckInDate)) {
                reservation.setCheckInDate(reservedCheckInDate);  // If checking in early (within 15-min window)
            } else {
                reservation.setCheckInDate(checkInDate); // If checking in after reserved time
            }
            roomStatusService.setRoomToOccupied(reservation.getRoom().getId());
        }
    }

    // validate check in before checkout
    private void validateCheckInBeforeCheckOut(RoomReservationPayloadFormDTO formDTO) {
        if (!formDTO.getReservedCheckInDate().isBefore(formDTO.getReservedCheckOutDate())) {
            throw new APIException("Check-in date/time must be before check-out date/time");
        }
    }

    // validate if it is in past
    private void validateCheckInNotInPast(RoomReservationPayloadFormDTO formDTO) {
        if (formDTO.getReservedCheckInDate().isBefore(LocalDate.now())) {
            throw new APIException("Check-in date cannot be in the past");
        }
    }

    // validate room availability when updating
    private void validateRoomAvailability(Long roomId, LocalDate checkIn, LocalDate checkOut, Long excludeReservationId) {
        List<RoomReservation> overlappingReservations = roomReservationRepository
                .findOverlappingReservationsExcludingCurrent(roomId, checkIn, checkOut, excludeReservationId);

        if (!overlappingReservations.isEmpty()) {
            throw new APIException("Room is not available for the selected dates");
        }
    }

    // validate status changes
    private void validateReservationUpdateAllowed(RoomReservation reservation) {
        // Define statuses that don't allow updates
        Set<String> nonUpdatableStatuses = Set.of(
                "CHECKED-OUT",
                "CANCELLED"
        );

        if (nonUpdatableStatuses.contains(reservation.getRoomReservationStatus().getName())) {
            throw new APIException("Cannot update reservation in " +
                    reservation.getRoomReservationStatus().getName() + " status");
        }
    }

    // validate if price has been paid at least 20% excluding pending reservations
    private void checkPricePaid(RoomReservationPayloadFormDTO formDTO) {
        if (formDTO.getRoomReservationStatusId().equals(roomReservationStatusService.getPendingStatus().getId())) {
            return;
        }
        BigDecimal twentyPercent = formDTO.getBillingPayloadDTO().getTotalPrice().divide(BigDecimal.valueOf(4), 0, RoundingMode.UP);
        if (formDTO.getBillingPayloadDTO().getPaidAmount().compareTo(twentyPercent) < 0) {
            throw new APIException("Price must be paid at least 20% of the total amount");
        }
    }

    // Helper method to fetch guests
    private Set<Guest> fetchGuests(Set<Long> guestIds) {
        return guestIds.stream()
                .map(guestService::getGuestById
                ).collect(Collectors.toSet());
    }

    // Helper method to fetch children
    private Set<Child> fetchChildren(Set<Long> childIds) {
        return childIds.stream()
                .map(childService::getChildById)
                .collect(Collectors.toSet());
    }

    // DTO mapping
    private RoomReservationTableData mapToTableDataDTO(RoomReservation roomReservation) {
        RoomReservationTableData data = modelMapper.map(roomReservation, RoomReservationTableData.class);
        data.setTotalPrice(roomReservation.getBilling().getTotalPrice());
        data.setPaymentStatusName(roomReservation.getBilling().getPaymentStatus().getName());
        return data;
    }
}