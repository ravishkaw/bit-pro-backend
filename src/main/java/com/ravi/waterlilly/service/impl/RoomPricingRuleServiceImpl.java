package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.RoomPricingRule;
import com.ravi.waterlilly.model.RoomType;
import com.ravi.waterlilly.payload.roomPricingRule.RoomPricingRulePageResponse;
import com.ravi.waterlilly.payload.roomPricingRule.RoomPricingRulePayloadDTO;
import com.ravi.waterlilly.repository.RoomPricingRuleRepository;
import com.ravi.waterlilly.service.RoomPricingRuleService;
import com.ravi.waterlilly.service.StatusService;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

// implementation of pricing rule service
@Service
@RequiredArgsConstructor
public class RoomPricingRuleServiceImpl implements RoomPricingRuleService {
    private final RoomPricingRuleRepository roomPricingRuleRepository;
    private final ModelMapper modelMapper;
    private final PrivilegeUtils privilegeUtils;
    private final StatusService statusService;

    // get all rules
    @Override
    public RoomPricingRulePageResponse getAllRules(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {

        // privilege check for the user
        privilegeUtils.privilegeCheck("Room Pricing Rule", AppConstants.SELECT);

        // Determine the sorting order and sort by
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Create a Pageable object for pagination and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<RoomPricingRule> roomPricingRulePage = StringUtils.hasText(searchQuery)
                ? roomPricingRuleRepository.searchPricingRules(searchQuery.trim(), pageable)
                : roomPricingRuleRepository.findAll(pageable);

        // Extract the list of pricing rule from the page
        List<RoomPricingRule> pricingRules = roomPricingRulePage.getContent();

        // Map the list of pricing rule to a list of RoomPricingRulePayloadDTO using ModelMapper
        List<RoomPricingRulePayloadDTO> ruleDTOs = pricingRules.stream()
                .map(rules -> modelMapper.map(rules, RoomPricingRulePayloadDTO.class))
                .toList();

        // Create a response object of pricing rule data
        return new RoomPricingRulePageResponse(
                ruleDTOs,
                roomPricingRulePage.getNumber(),
                roomPricingRulePage.getSize(),
                roomPricingRulePage.getTotalElements(),
                roomPricingRulePage.getTotalPages(),
                roomPricingRulePage.isLast()
        );
    }

    // get single rule
    @Override
    public RoomPricingRulePayloadDTO getSingleRule(Integer pricingRuleId) {

        // privilege check for the user
        privilegeUtils.privilegeCheck("Room Pricing Rule", AppConstants.SELECT);

        RoomPricingRule roomPricingRule = getRoomPricingRuleById(pricingRuleId);

        return modelMapper.map(roomPricingRule, RoomPricingRulePayloadDTO.class);
    }

    // add a new pricing rule
    @Override
    public void addNewRule(RoomPricingRulePayloadDTO roomPricingRuleDTO) {

        // privilege check for the user
        privilegeUtils.privilegeCheck("Room Pricing Rule", AppConstants.INSERT);

        // check if the room pricing rule is already in database
        //validateRoomPricingRuleUniqueness(roomPricingRuleDTO, null);

        // set details and save
        RoomPricingRule newRoomPricingRule = new RoomPricingRule();
        updateRoomPricingRuleFields(newRoomPricingRule, roomPricingRuleDTO);

        roomPricingRuleRepository.save(newRoomPricingRule);
    }

    // update a pricing rule
    @Override
    public void updateRule(RoomPricingRulePayloadDTO roomPricingRuleDTO, Integer pricingRuleId) {
        // privilege check for the user
        privilegeUtils.privilegeCheck("Room Pricing Rule", AppConstants.UPDATE);

        // Find the existing pricing rule
        RoomPricingRule existingRule = getRoomPricingRuleById(pricingRuleId);

        // check if the pricing rule is applied to any room type for inactive status
        if (existingRule.getStatus().getName().equals(AppConstants.INACTIVE_STATUS)) {
            checkAppliedRoomTypes(existingRule);
        }

        // check if the room pricing rule is already in database
        //validateRoomPricingRuleUniqueness(roomPricingRuleDTO, pricingRuleId);

        // Update the pricing rule and save
        updateRoomPricingRuleFields(existingRule, roomPricingRuleDTO);

        roomPricingRuleRepository.save(existingRule);
    }

    // delete a rule (soft delete)
    @Override
    public void deleteRule(Integer pricingRuleId) {
        // privilege check for the user
        privilegeUtils.privilegeCheck("Room Pricing Rule", AppConstants.DELETE);

        // Find the existing pricing rule
        RoomPricingRule existingRule = getRoomPricingRuleById(pricingRuleId);

        // check if the pricing rule is applied to any room type before deleting
        checkAppliedRoomTypes(existingRule);

        // Soft delete by setting isDeleted flag
        existingRule.setStatus(statusService.getDeletedStatus());

        roomPricingRuleRepository.save(existingRule);
    }

    // restore a pricing rule
    @Override
    public void restorePricingRule(Integer pricingRuleId) {
        // privilege check for the user
        privilegeUtils.privilegeCheck("Room Pricing Rule", AppConstants.DELETE);

        // Find the existing pricing rule
        RoomPricingRule existingRule = getRoomPricingRuleById(pricingRuleId);

        existingRule.setStatus(statusService.getActiveStatus());
        roomPricingRuleRepository.save(existingRule);
    }

    // Helper method to get pricing rule by id
    @Override
    public RoomPricingRule getRoomPricingRuleById(Integer pricingRuleId) {
        return roomPricingRuleRepository.findById(pricingRuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Room Pricing Rule", "pricingRuleId", String.valueOf(pricingRuleId)));
    }

    // Helper method to update room pricing rule fields
    private void updateRoomPricingRuleFields(RoomPricingRule pricingRule, RoomPricingRulePayloadDTO pricingRuleWithRoomTypeDTO) {
        pricingRule.setName(pricingRuleWithRoomTypeDTO.getName());
        pricingRule.setStartDate(pricingRuleWithRoomTypeDTO.getStartDate());
        pricingRule.setEndDate(pricingRuleWithRoomTypeDTO.getEndDate());
        pricingRule.setPricingMultiplier(pricingRuleWithRoomTypeDTO.getPricingMultiplier());
        pricingRule.setStatus(statusService.getStatusByName(pricingRuleWithRoomTypeDTO.getStatusName()));
    }

    // Helper method to check pricing rule applied to room type
    private void checkAppliedRoomTypes(RoomPricingRule roomPricingRule) {
        Set<RoomType> appliedRoomTypes = roomPricingRule.getRoomTypes();
        if (appliedRoomTypes != null && !appliedRoomTypes.isEmpty()) {
            throw new APIException("Pricing rule with name " + roomPricingRule.getName() + " cannot be deleted / set inactive as it is associated with " + appliedRoomTypes.size() + " room type(s).");
        }
    }
}


//    // Helper method to check if the room pricing rule is in db
//    private void validateRoomPricingRuleUniqueness(RoomPricingRulePayloadDTO roomPricingRuleDTO, Integer pricingRuleId) {
//
//        LocalDate startDate = roomPricingRuleDTO.getStartDate();
//        LocalDate endDate = roomPricingRuleDTO.getEndDate();
//
//        boolean pricingRuleExists;
//        if (pricingRuleId == null) {
//            // Check for overlapping date ranges when adding a new rule
//            pricingRuleExists = roomPricingRuleRepository.existsByDateRangeOverlap(
//                    startDate, endDate
//            );
//        } else {
//            // Check for overlapping date ranges when updating an existing rule
//            pricingRuleExists = roomPricingRuleRepository.existsByDateRangeOverlapExcludingId(
//                    startDate, endDate, pricingRuleId
//            );
//        }
//
//        if (pricingRuleExists) {
//            throw new APIException("The provided date range overlaps with an existing pricing rule for this room type.");
//        }
//    }