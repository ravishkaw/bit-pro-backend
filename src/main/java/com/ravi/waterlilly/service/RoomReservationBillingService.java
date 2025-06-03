package com.ravi.waterlilly.service;

import com.ravi.waterlilly.payload.roomReservationBilling.CheckOutBillingDTO;
import com.ravi.waterlilly.payload.roomReservationBilling.RoomReservationBillingDTO;
import com.ravi.waterlilly.payload.roomReservationBilling.RoomReservationBillingPayloadDTO;

// service layer of room reservation billing
public interface RoomReservationBillingService {
    RoomReservationBillingDTO calculatePrice(RoomReservationBillingPayloadDTO billingDTO);

    // calculate price when check out
    CheckOutBillingDTO calculatePriceForCheckOut(Long reservationId);
}
