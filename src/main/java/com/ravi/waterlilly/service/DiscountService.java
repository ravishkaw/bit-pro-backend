package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Discount;
import com.ravi.waterlilly.payload.billingDiscount.DiscountPayloadDTO;
import com.ravi.waterlilly.payload.billingDiscount.DiscountWithAmountDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// Service layer of Discount
public interface DiscountService {
    // get all discounts
    List<DiscountPayloadDTO> getAllDiscounts();

    // get a single discount
    DiscountPayloadDTO getSingleDiscount(Integer id);

    // add discount
    void addDiscount(DiscountPayloadDTO discountPayloadDTO);

    // update discount
    void updateDiscount(Integer id, DiscountPayloadDTO discountPayloadDTO);

    // delete discount
    void deleteDiscount(Integer id);

    // restore a discount
    void restoreDiscount(Integer id);

    // get discount by id
    Discount getDiscountById(Integer id);

    // Helper method to calculate discount
    DiscountWithAmountDTO calculateDiscount(BigDecimal price, LocalDate checkInDate);

    // Helper method to get discount by code
    BigDecimal getDiscountByCode(String code, BigDecimal price);
}
