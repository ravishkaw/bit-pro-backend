package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Tax;
import com.ravi.waterlilly.payload.billingTaxes.TaxPayloadDTO;
import com.ravi.waterlilly.payload.billingTaxes.TaxWithAmountDTO;
import com.ravi.waterlilly.repository.TaxRepository;
import com.ravi.waterlilly.service.StatusService;
import com.ravi.waterlilly.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

// implementation of tax service
@Service
@RequiredArgsConstructor
public class TaxServiceImpl implements TaxService {
    private final TaxRepository taxRepository;
    private final ModelMapper modelMapper;
    private final StatusService statusService;

    // get all taxes
    @Override
    public List<TaxPayloadDTO> getAllTaxes() {

        // map and return list of taxes
        return taxRepository.findAll().stream()
                .map(taxes -> modelMapper.map(taxes, TaxPayloadDTO.class))
                .toList();
    }

    // get single tax
    @Override
    public TaxPayloadDTO getTax(Integer id) {

        // find, map and return tax
        Tax tax = getTaxById(id);
        return modelMapper.map(tax, TaxPayloadDTO.class);
    }

    // add tax
    @Override
    public void addTax(TaxPayloadDTO taxPayloadDTO) {

        // validate uniqueness of tax name
        validateUniqueness(taxPayloadDTO, null);

        // create and save tax
        Tax tax = new Tax();
        updateTaxFields(tax, taxPayloadDTO);
        taxRepository.save(tax);
    }

    // update tax
    @Override
    public void updateTax(Integer id, TaxPayloadDTO taxPayloadDTO) {

        // validate uniqueness of tax name
        validateUniqueness(taxPayloadDTO, id);

        // find tax and update fields
        Tax tax = getTaxById(id);
        updateTaxFields(tax, taxPayloadDTO);
        taxRepository.save(tax);
    }

    // delete a tax
    @Override
    public void deleteTax(Integer id) {

        // find tax and delete
        Tax tax = getTaxById(id);
        tax.setStatus(statusService.getDeletedStatus());
        taxRepository.save(tax);
    }

    // restore a tax
    @Override
    public void restoreTax(Integer id) {

        // find tax and restore
        Tax tax = getTaxById(id);
        tax.setStatus(statusService.getActiveStatus());
        taxRepository.save(tax);
    }

    // get tax by id
    @Override
    public Tax getTaxById(Integer id) {
        return taxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax", "taxId", id.toString()));
    }

    // Helper method to calculate tax (Only VAT)
    @Override
    public TaxWithAmountDTO calculateTaxes(BigDecimal price) {
        Tax tax = taxRepository.getReferenceById(1); // Vat
        BigDecimal taxAmount = price.multiply(tax.getPercentage()).setScale(0, RoundingMode.HALF_UP);
        return new TaxWithAmountDTO(tax, taxAmount);
    }

    // Helper method to update tax fields
    private void updateTaxFields(Tax tax, TaxPayloadDTO taxPayloadDTO) {
        tax.setName(taxPayloadDTO.getName());
        tax.setPercentage(taxPayloadDTO.getPercentage());
        tax.setDescription(taxPayloadDTO.getDescription());
        tax.setStatus(statusService.getStatusByName(taxPayloadDTO.getStatusName()));
    }

    // Helper method to validate uniqueness
    private void validateUniqueness(TaxPayloadDTO taxPayloadDTO, Integer id) {
        Tax tax = taxRepository.findTaxByName(taxPayloadDTO.getName());

        if (tax != null && !tax.getId().equals(id)) {
            throw new APIException("Tax with name " + taxPayloadDTO.getName() + " already exists");
        }
    }
}
