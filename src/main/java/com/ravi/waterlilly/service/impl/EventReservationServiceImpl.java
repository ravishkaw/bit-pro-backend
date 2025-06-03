package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.*;
import com.ravi.waterlilly.payload.billing.BillingPayloadDTO;
import com.ravi.waterlilly.payload.eventReservation.EventReservationPageResponse;
import com.ravi.waterlilly.payload.eventReservation.EventReservationPayloadDTO;
import com.ravi.waterlilly.payload.eventReservation.EventReservationPayloadFormDTO;
import com.ravi.waterlilly.payload.eventReservation.EventReservationTableData;
import com.ravi.waterlilly.payload.eventService.EventServiceQuantityDTO;
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Implementation of Event Reservation Service.
@Service
@RequiredArgsConstructor
public class EventReservationServiceImpl implements EventReservationService {
    private final PrivilegeUtils privilegeUtils;
    private final EventReservationRepository eventReservationRepository;
    private final ModelMapper modelMapper;
    private final BillingService billingService;
    private final EventVenueRepository eventVenueRepository;
    private final EventServicesService eventServicesService;
    private final EventPackageService eventPackageService;
    private final EventStatusService eventStatusService;
    private final EventVenueService eventVenueService;
    private final EventTypeService eventTypeService;
    private final GuestService guestService;

    // get all reservations
    @Override
    public EventReservationPageResponse getAllReservations(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String status, String searchQuery) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Reservation", AppConstants.SELECT);

        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<EventReservation> page = StringUtils.hasText(searchQuery)
                ? eventReservationRepository.searchEventReservations(searchQuery.trim(), pageable)
                : eventReservationRepository.getReservationByEventStatus(status, pageable);

        List<EventReservation> reservations = page.getContent();

        List<EventReservationTableData> dto = reservations.stream()
                .map(reservation -> {
                    EventReservationTableData data = modelMapper.map(reservation, EventReservationTableData.class);
                    data.setTotalPrice(reservation.getBilling().getTotalPrice());
                    data.setPaymentStatusName(reservation.getBilling().getPaymentStatus().getName());
                    return data;
                })
                .toList();

        return new EventReservationPageResponse(
                dto,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    // get single reservation
    @Override
    public EventReservationPayloadDTO getSingleReservation(Long reservationId) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Reservation", AppConstants.SELECT);

        EventReservation reservation = eventReservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Event reservation", "reservationId", reservationId));

        EventReservationPayloadDTO dto = modelMapper.map(reservation, EventReservationPayloadDTO.class);

        Set<EventServiceQuantityDTO> serviceQuantityDTOS = reservation.getReservationServices().stream()
                .map(services -> {
                    EventServiceQuantityDTO quantityDTO = new EventServiceQuantityDTO();
                    quantityDTO.setEventServiceId(services.getEventServices().getId());
                    quantityDTO.setQuantity(services.getQuantity());
                    return quantityDTO;
                })
                .collect(Collectors.toSet());

        dto.setServices(serviceQuantityDTOS);
        return dto;
    }

    // add new reservation
    @Override
    @Transactional
    public void addEventReservation(EventReservationPayloadFormDTO formDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Reservation", AppConstants.INSERT);

        // validate
        validateStartDateIsInPast(formDTO);
        validateStartDateAndEndDate(formDTO);
        validateVenueAvailability(formDTO.getEventVenueId(), formDTO.getStartDatetime(), formDTO.getEndDatetime(), formDTO.getExpectedGuestCount());

        // update fields
        EventReservation reservation = new EventReservation();
        updateReservationFields(formDTO, reservation);
        eventReservationRepository.save(reservation);
    }

    // update existing reservation
    @Override
    @Transactional
    public void updateEventReservation(Long reservationId, EventReservationPayloadFormDTO formDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Reservation", AppConstants.UPDATE);

        // validate
        validateStartDateAndEndDate(formDTO);
        validateVenueAvailability(formDTO.getEventVenueId(), formDTO.getStartDatetime(), formDTO.getEndDatetime(), reservationId);

        EventReservation reservation = eventReservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Event reservation", "reservationId", reservationId));

        validateReservationUpdateAllowed(reservation);

        updateReservationFields(formDTO, reservation);
        eventReservationRepository.save(reservation);
    }

    // Helper method to update reservation fields
    private void updateReservationFields(EventReservationPayloadFormDTO dto, EventReservation reservation) {
        reservation.setGuest(guestService.getGuestById(dto.getGuestId()));
        reservation.setEventType(eventTypeService.getEventTypeById(dto.getEventTypeId()));
        reservation.setName(dto.getName());
        reservation.setEventVenue(eventVenueService.getVenueById(dto.getEventVenueId()));
        reservation.setStartDatetime(dto.getStartDatetime());
        reservation.setEndDatetime(dto.getEndDatetime());
        reservation.setExpectedGuestCount(dto.getExpectedGuestCount());
        reservation.setNote(dto.getNote());
        reservation.setEventStatus(eventStatusService.getEventStatusById(dto.getEventStatusId()));
        reservation.setEventPackage(eventPackageService.getEventPackageById(dto.getEventPackageId()));

        if (reservation.getId() == null)
            reservation.setReservationDatetime(LocalDateTime.now());

        updateBillingRecords(reservation, dto.getBillingPayloadDTO());
        updateEventServices(reservation, dto);
    }

    // Helper method to update billing records
    private void updateBillingRecords(EventReservation reservation, BillingPayloadDTO billingPayloadDTO) {
        if (reservation.getBilling() != null && reservation.getBilling().getId() != null) {
            billingService.updateBillingDetails(reservation.getBilling().getId(), billingPayloadDTO);
        } else {
            Billing billing = billingService.createNewBillingDetails(billingPayloadDTO);
            reservation.setBilling(billing);
        }
    }

    // Helper method to update event services
    private void updateEventServices(EventReservation reservation, EventReservationPayloadFormDTO dto) {
        if (dto.getServices() != null) {
            if (reservation.getReservationServices() != null && !reservation.getReservationServices().isEmpty()) {
                reservation.clearReservationServices();
            }

            dto.getServices().forEach(service -> {
                EventService serviceObj = eventServicesService.getEventServiceById(service.getEventServiceId());
                reservation.addReservationService(serviceObj, service.getQuantity());
            });
        }
    }

    // Helper method to check venue is available when reserving
    private void validateVenueAvailability(Long venueId, LocalDateTime startDatetime, LocalDateTime endDatetime, Integer capacity) {
        List<EventVenue> venues = eventVenueRepository
                .findAvailableVenues(startDatetime, endDatetime, capacity);

        boolean isVenueAvailable = venues.stream()
                .anyMatch(venue -> venue.getId().equals(venueId));

        if (!isVenueAvailable) {
            throw new APIException("The venue is not available for this time period");
        }
    }

    // Helper method to validate venue updating
    private void validateVenueAvailability(Long venueId, LocalDateTime startDatetime, LocalDateTime endDatetime, Long reservationId) {
        List<EventReservation> reservations = eventReservationRepository
                .findOverlappingReservationsExcludingCurrent(venueId, startDatetime, endDatetime, reservationId);

        if (!reservations.isEmpty()) {
            throw new APIException("There is already a reservation in this venue for this time period");
        }
    }

    // Helper method to validate start and end date
    private void validateStartDateAndEndDate(EventReservationPayloadFormDTO dto) {
        if (dto.getStartDatetime().isAfter(dto.getEndDatetime())) {
            throw new APIException("Start date cannot be after end date");
        }
    }

    // Helper method to validate the start date is in past
    private void validateStartDateIsInPast(EventReservationPayloadFormDTO dto) {
        if (dto.getStartDatetime().isBefore(LocalDateTime.now())) {
            throw new APIException("Start date cannot be in past");
        }
    }

    // validate status changes
    private void validateReservationUpdateAllowed(EventReservation reservation) {
        // Define statuses that don't allow updates
        Set<String> nonUpdatableStatuses = Set.of(
                "COMPLETED",
                "CANCELLED"
        );

        if (nonUpdatableStatuses.contains(reservation.getEventStatus().getName())) {
            throw new APIException("Cannot update reservation in " +
                    reservation.getEventStatus().getName() + " status");
        }
    }
}
