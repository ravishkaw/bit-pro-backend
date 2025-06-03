package com.ravi.waterlilly.service;

import com.ravi.waterlilly.payload.billing.RefundPayloadDTO;

import java.util.List;

// Service layer of the refund
public interface RefundService {

    // get all refunds
    List<RefundPayloadDTO> getAllRefunds();

    // get a single refund
    RefundPayloadDTO getSingleRefund(Integer id);

    // add refund
    void addRefund(RefundPayloadDTO refundPayloadDTO);

//    // update refund
//    void updateRefund(Integer id, RefundPayloadDTO refundPayloadDTO);
//
//    // delete refund
//    void deleteRefund(Integer id);

}
