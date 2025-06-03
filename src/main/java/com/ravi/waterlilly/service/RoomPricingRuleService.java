package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.RoomPricingRule;
import com.ravi.waterlilly.payload.roomPricingRule.RoomPricingRulePageResponse;
import com.ravi.waterlilly.payload.roomPricingRule.RoomPricingRulePayloadDTO;
import jakarta.validation.Valid;

//  Service interface for managing room pricing rules.
public interface RoomPricingRuleService {
    // get all rules
    RoomPricingRulePageResponse getAllRules(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // get single rule
    RoomPricingRulePayloadDTO getSingleRule(Integer pricingRuleId);

    // add new rule
    void addNewRule(@Valid RoomPricingRulePayloadDTO roomPricingRuleDTO);

    // update existing rule
    void updateRule(@Valid RoomPricingRulePayloadDTO roomPricingRuleDTO, Integer pricingRuleId);

    // delete a rule
    void deleteRule(Integer pricingRuleId);

    // restore deleted rule
    void restorePricingRule(Integer pricingRuleId);

    // Helper method to get pricing rule by id
    RoomPricingRule getRoomPricingRuleById(Integer pricingRuleId);
}
