package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Discount;
import com.ravi.waterlilly.payload.billingDiscount.DiscountPayloadDTO;
import com.ravi.waterlilly.payload.billingDiscount.DiscountReferenceDTO;
import com.ravi.waterlilly.payload.billingDiscount.DiscountWithAmountDTO;
import com.ravi.waterlilly.repository.DiscountRepository;
import com.ravi.waterlilly.service.DiscountService;
import com.ravi.waterlilly.service.StatusService;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

// Implementation of the discount service
@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final ModelMapper modelMapper;
    private final PrivilegeUtils privilegeUtils;
    private final StatusService statusService;

    // get all discounts
    @Override
    public List<DiscountPayloadDTO> getAllDiscounts() {

        // check privileges
        privilegeUtils.privilegeCheck("Discounts", AppConstants.SELECT);

        // check if any discount is available and return the list
        return discountRepository.findAll().stream()
                .map(discount -> modelMapper.map(discount, DiscountPayloadDTO.class))
                .toList();
    }

    // get a single discount
    @Override
    public DiscountPayloadDTO getSingleDiscount(Integer id) {

        // check privileges
        privilegeUtils.privilegeCheck("Discounts", AppConstants.SELECT);

        Discount discount = getDiscountById(id);

        return modelMapper.map(discount, DiscountPayloadDTO.class);
    }

    // add a discount
    @Override
    public void addDiscount(DiscountPayloadDTO discountPayloadDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Discounts", AppConstants.INSERT);

        // check if the discount already exists
        checkUniqueness(discountPayloadDTO, null);

        // update fields and save discount
        Discount discount = new Discount();
        updateDiscountFields(discount, discountPayloadDTO);
        discountRepository.save(discount);
    }

    // update a discount
    @Override
    public void updateDiscount(Integer id, DiscountPayloadDTO discountPayloadDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Discounts", AppConstants.UPDATE);

        // check if the discount already exists
        checkUniqueness(discountPayloadDTO, id);

        // get existing discount
        Discount discount = getDiscountById(id);

        // update fields and save discount
        updateDiscountFields(discount, discountPayloadDTO);
        discountRepository.save(discount);
    }

    // delete a discount
    @Override
    public void deleteDiscount(Integer id) {

        // check privileges
        privilegeUtils.privilegeCheck("Discounts", AppConstants.DELETE);

        // get existing discount
        Discount discount = getDiscountById(id);
        discount.setStatus(statusService.getDeletedStatus());
        discountRepository.save(discount);
    }

    // restore a discount
    @Override
    public void restoreDiscount(Integer id) {

        // check privileges
        privilegeUtils.privilegeCheck("Discounts", AppConstants.INSERT);

        // get existing discount
        Discount discount = getDiscountById(id);
        discount.setStatus(statusService.getActiveStatus());
        discountRepository.save(discount);
    }

    // get discount by id
    @Override
    public Discount getDiscountById(Integer id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount", "id", id.toString()));
    }

    // Helper method to calculate discount
    @Override
    public DiscountWithAmountDTO calculateDiscount(BigDecimal price, LocalDate checkInDate) {
        BigDecimal discountAmount;

        // find applicable discounts for check in date
        List<Discount> applicableDiscounts = discountRepository
                .findApplicableDiscount(checkInDate);

        // apply discount if applicable, return the 0
        if (!applicableDiscounts.isEmpty()) {
            Discount discount = applicableDiscounts.get(0);
            discountAmount = price.multiply(discount.getPercentage()).setScale(0, RoundingMode.HALF_UP);
            return new DiscountWithAmountDTO(
                    modelMapper.map(discount, DiscountReferenceDTO.class), discountAmount
            );
        }
        return new DiscountWithAmountDTO(null, BigDecimal.ZERO);
    }

    // Helper method to get discount amount by code
    @Override
    public BigDecimal getDiscountByCode(String code, BigDecimal price) {
        Discount discount = discountRepository.findByCode(code);
        if (discount == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(discount.getPercentage()).setScale(0, RoundingMode.HALF_UP);
    }

    // Helper method to update discount fields
    private void updateDiscountFields(Discount discount, DiscountPayloadDTO discountPayloadDTO) {
        discount.setCode(discountPayloadDTO.getCode());
        discount.setPercentage(discountPayloadDTO.getPercentage());
        discount.setStartDate(discountPayloadDTO.getStartDate());
        discount.setEndDate(discountPayloadDTO.getEndDate());
        discount.setStatus(statusService.getStatusByName(discountPayloadDTO.getStatusName()));
    }

    // Helper method to check uniqueness of the discount
    private void checkUniqueness(DiscountPayloadDTO discountPayloadDTO, Integer id) {

        // check the discount already exists
        Discount discount = discountRepository.findByCode(discountPayloadDTO.getCode());
        if (discount != null && !discount.getId().equals(id)) {
            throw new APIException("Discount with code " + discountPayloadDTO.getCode() + " already exists");
        }

        // check discount dates overlaps
        LocalDate startDate = discountPayloadDTO.getStartDate();
        LocalDate endDate = discountPayloadDTO.getEndDate();

        boolean discountExists;
        if (id != null) {
            // check if the discount is already there for the time range
            discountExists = discountRepository.existsByDateRangeOverlap(startDate, endDate);
        } else {
            // check if the discount is already there for the time range excluding the discount id
            discountExists = discountRepository.existsByDateRangeOverlapExcludingId(startDate, endDate, id);
        }

        if (discountExists) {
            throw new APIException("Discount overlaps with another discount");
        }
    }
}
