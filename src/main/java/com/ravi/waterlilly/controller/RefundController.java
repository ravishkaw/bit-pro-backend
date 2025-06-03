package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.billing.RefundPayloadDTO;
import com.ravi.waterlilly.service.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// handle all refund related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/refund")
public class RefundController {
    private final RefundService refundService;

    // get all refunds
    @GetMapping
    public ResponseEntity<List<RefundPayloadDTO>> getAllRefunds() {
        List<RefundPayloadDTO> refundDTOS = refundService.getAllRefunds();
        return new ResponseEntity<>(refundDTOS, HttpStatus.OK);
    }

    // get a single refund
    @GetMapping("/{refundId}")
    public ResponseEntity<RefundPayloadDTO> getRefundById(@PathVariable Integer refundId) {
        RefundPayloadDTO refundPayloadDTO = refundService.getSingleRefund(refundId);
        return new ResponseEntity<>(refundPayloadDTO, HttpStatus.OK);
    }

    // add new refund
    @PostMapping
    public ResponseEntity<RefundPayloadDTO> addNewRefund(@Valid @RequestBody RefundPayloadDTO refundPayloadDTO) {
        refundService.addRefund(refundPayloadDTO);
        return new ResponseEntity<>(refundPayloadDTO, HttpStatus.CREATED);
    }

//    // update a refund
//    @PutMapping("/{refundId}")
//    public ResponseEntity<?> updateRefund(@PathVariable Integer refundId, @Valid @RequestBody RefundPayloadDTO refundPayloadDTO) {
//        refundService.updateRefund(refundId, refundPayloadDTO);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    // delete a refund
//    @DeleteMapping("/{refundId}")
//    public ResponseEntity<?> deleteRefund(@PathVariable Integer refundId) {
//        refundService.deleteRefund(refundId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

}
