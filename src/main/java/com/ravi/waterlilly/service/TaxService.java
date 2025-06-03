package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Tax;
import com.ravi.waterlilly.payload.billingTaxes.TaxPayloadDTO;
import com.ravi.waterlilly.payload.billingTaxes.TaxWithAmountDTO;

import java.math.BigDecimal;
import java.util.List;

// Service interface of Tax-related services.
public interface TaxService {

    // get all taxes
    List<TaxPayloadDTO> getAllTaxes();

    // get single tax
    TaxPayloadDTO getTax(Integer id);

    // add new tax
    void addTax(TaxPayloadDTO taxPayloadDTO);

    // update tax
    void updateTax(Integer id, TaxPayloadDTO taxPayloadDTO);

    // delete tax
    void deleteTax(Integer id);

    // restore a tax
    void restoreTax(Integer id);

    // get tax by id
    Tax getTaxById(Integer id);

    // Helper method to calculate tax (Only VAT)
    TaxWithAmountDTO calculateTaxes(BigDecimal price);
}
