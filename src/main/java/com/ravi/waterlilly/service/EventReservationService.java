package com.ravi.waterlilly.service;

import com.ravi.waterlilly.payload.eventReservation.EventReservationPageResponse;
import com.ravi.waterlilly.payload.eventReservation.EventReservationPayloadDTO;
import com.ravi.waterlilly.payload.eventReservation.EventReservationPayloadFormDTO;

// service layer of event reservation
public interface EventReservationService {
    // get all reservations
    EventReservationPageResponse getAllReservations(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String status, String searchQuery);

    // get a single reservation
    EventReservationPayloadDTO getSingleReservation(Long reservationId);

    // add new reservation
    void addEventReservation(EventReservationPayloadFormDTO eventReservation);

    // update existing reservation
    void updateEventReservation(Long reservationId, EventReservationPayloadFormDTO eventReservation);

//    // calculate pricing and return billing details
//    BillingPayloadDTO calculatePrice(BillingPayloadDTO billingDTO);
//
//    // update reservation status to action
//    void updateStatusToAction(Long reservationId, String action);

    // checkout
//    void reservationCheckOut(Long reservationId, CheckOutDTO checkOutDTO);
}
