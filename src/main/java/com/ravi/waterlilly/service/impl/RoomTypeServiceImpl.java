package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.*;
import com.ravi.waterlilly.payload.roomType.RoomTypeBasicDTO;
import com.ravi.waterlilly.payload.roomType.RoomTypePayloadDTO;
import com.ravi.waterlilly.payload.roomType.RoomTypePageResponse;
import com.ravi.waterlilly.repository.RoomTypeRepository;
import com.ravi.waterlilly.service.BedTypeService;
import com.ravi.waterlilly.service.RoomPricingRuleService;
import com.ravi.waterlilly.service.RoomTypeService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Implementation of RoomTypeService.
@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final ModelMapper modelMapper;
    private final PrivilegeUtils privilegeUtils;
    private final StatusService statusService;
    private final RoomPricingRuleService roomPricingRuleService;
    private final BedTypeService bedTypeService;

    // fetch all room types
    @Override
    public RoomTypePageResponse getAllRoomTypes(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {

        // privilege check
        privilegeUtils.privilegeCheck("Room Type", AppConstants.SELECT);

        // Determine the sorting order and sort by
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Create a Pageable object for pagination and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<RoomType> roomTypePage = StringUtils.hasText(searchQuery)
                ? roomTypeRepository.searchRoomTypes(searchQuery.trim(), pageable)
                : roomTypeRepository.findAll(pageable);

        // Extract the list of room types from the page
        List<RoomType> pricingRules = roomTypePage.getContent();

        // Map the list of room types to a list of RoomTypeBasicDTO using ModelMapper
        List<RoomTypeBasicDTO> roomTypeDTOS = pricingRules.stream()
                .map(roomType -> modelMapper.map(roomType, RoomTypeBasicDTO.class))
                .toList();

        // Create a response object of room type data
        return new RoomTypePageResponse(
                roomTypeDTOS,
                roomTypePage.getNumber(),
                roomTypePage.getSize(),
                roomTypePage.getTotalElements(),
                roomTypePage.getTotalPages(),
                roomTypePage.isLast());
    }

    // get all room types no pagination
    @Override
    public List<RoomTypeBasicDTO> getRoomTypes() {
        // privilege check
        privilegeUtils.privilegeCheck("Room Type", AppConstants.SELECT);

        // map and return all room types that hasn't deleted
        return roomTypeRepository.getRoomTypesNotDeleted().stream()
                .map(roomType -> modelMapper.map(roomType, RoomTypeBasicDTO.class))
                .toList();
    }

    // fetch single room type
    @Override
    public RoomTypePayloadDTO getSingleRoomType(Integer roomTypeId) {

        // privilege check
        privilegeUtils.privilegeCheck("Room Type", AppConstants.SELECT);

        // find and map and return
        RoomType roomType = fetchRoomTypeById(roomTypeId);
        RoomTypePayloadDTO roomTypeDTO = modelMapper.map(roomType, RoomTypePayloadDTO.class);

        Set<Integer> pricingRuleIds = roomType.getPricingRules().stream()
                .map(rule -> rule.getId())
                .collect(Collectors.toSet());

        roomTypeDTO.setPricingRuleIds(pricingRuleIds);
        return roomTypeDTO;
    }

    // add new room type
    @Override
    public void addNewRoomType(RoomTypePayloadDTO roomTypeDTO) {
        // privilege check
        privilegeUtils.privilegeCheck("Room Type", AppConstants.INSERT);

        // check if the room type is already in database
        validateRoomTypeUniqueness(roomTypeDTO, null);

        //map and save
        RoomType newRoomType = new RoomType();
        updateRoomTypeFields(newRoomType, roomTypeDTO);

        roomTypeRepository.save(newRoomType);
    }

    // update an existing room type
    @Override
    public void updateRoomType(RoomTypePayloadDTO roomTypeDTO, Integer roomTypeId) {

        // privilege check
        privilegeUtils.privilegeCheck("Room Type", AppConstants.UPDATE);

        // check if the room type exists
        RoomType existingRoomType = fetchRoomTypeById(roomTypeId);

        // check if the room type new status is inactive and has associated rooms
        if (roomTypeDTO.getStatusName().equalsIgnoreCase(AppConstants.INACTIVE_STATUS)) {
            checkAssociatedRooms(existingRoomType);
        }

        // check if the room type is already in database
        validateRoomTypeUniqueness(roomTypeDTO, roomTypeId);

        //set details and save
        updateRoomTypeFields(existingRoomType, roomTypeDTO);
        roomTypeRepository.save(existingRoomType);
    }

    // delete a room type - soft delete
    @Override
    public void deleteRoomType(Integer roomTypeId) {

        // privilege check
        privilegeUtils.privilegeCheck("Room Type", AppConstants.DELETE);

        // check if the room type exists
        RoomType existingRoomType = fetchRoomTypeById(roomTypeId);

        // check if the room type has associated rooms
        checkAssociatedRooms(existingRoomType);

        //get room type status
        Status status = statusService.getDeletedStatus();
        existingRoomType.setStatus(status);
        roomTypeRepository.save(existingRoomType);
    }

    // restore a room type
    @Override
    public void restoreRoomType(Integer roomTypeId) {

        // privilege check
        privilegeUtils.privilegeCheck("Room Type", AppConstants.DELETE);

        // check if the room type exists
        RoomType existingRoomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Room Type", "room id", String.valueOf(roomTypeId)));

        //get room type status
        existingRoomType.setStatus(statusService.getActiveStatus());
        roomTypeRepository.save(existingRoomType);
    }

    // method to fetch room type by id
    @Override
    public RoomType fetchRoomTypeById(Integer roomTypeId) {
        return roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Room Type", "roomTypeId", String.valueOf(roomTypeId)));
    }

    // Methods to calculate price based on pricing rules
    @Override
    public BigDecimal calculateRoomTypePriceForReservation(RoomType roomType, LocalDateTime checkIn, LocalDateTime checkOut) {
        BigDecimal basePrice = roomType.getBasePrice();
        LocalDate checkInDate = checkIn.toLocalDate();
        LocalDate checkOutDate = checkOut.toLocalDate();

        // Ensure at least one night is charged
        if (!checkOutDate.isAfter(checkInDate)) {
            checkOutDate = checkInDate.plusDays(1);
        }

        BigDecimal totalPrice = BigDecimal.ZERO;

        // Loop through each date in the reservation
        for (LocalDate date = checkInDate; date.isBefore(checkOutDate); date = date.plusDays(1)) {
            final LocalDate currentDate = date;

            // Find applicable pricing rule with highest multiplier
            RoomPricingRule applicableRule = roomType.getPricingRules().stream()
                    .filter(rule -> rule.getStatus().getName().equals(AppConstants.ACTIVE_STATUS))
                    .filter(rule -> rule.getStartDate() != null && rule.getEndDate() != null)
                    .filter(rule -> !rule.getEndDate().isBefore(currentDate) && !rule.getStartDate().isAfter(currentDate))
                    .max(Comparator.comparing(RoomPricingRule::getPricingMultiplier))
                    .orElse(null);

            BigDecimal nightlyRate = (applicableRule != null)
                    ? basePrice.multiply(applicableRule.getPricingMultiplier())
                    : basePrice;

            totalPrice = totalPrice.add(nightlyRate);
        }

        return totalPrice;
    }

    // Helper method to update room type fields
    private void updateRoomTypeFields(RoomType roomType, RoomTypePayloadDTO roomTypeDTO) {
        roomType.setName(roomTypeDTO.getName());
        roomType.setBasePrice(roomTypeDTO.getBasePrice());
        roomType.setDescription(roomTypeDTO.getDescription());
        roomType.setStatus(statusService.getStatusByName(roomTypeDTO.getStatusName()));
        roomType.setBedType(bedTypeService.getBedTypeById(roomTypeDTO.getBedTypeId()));
        roomType.setPricingRules(fetchPricingRules(roomTypeDTO.getPricingRuleIds()));
    }

    // Helper method to check if the room type is in db
    private void validateRoomTypeUniqueness(RoomTypePayloadDTO roomTypeDTO, Integer roomTypeId) {

        // Check duplicate room type name
        RoomType duplicateRoomType = roomTypeRepository.findByRoomTypeName(roomTypeDTO.getName());

        if (duplicateRoomType != null && !duplicateRoomType.getId().equals(roomTypeId))
            throw new APIException("Room with room name " + roomTypeDTO.getName() + " already exists!");
    }

    // Helper method to check if the room type has associated rooms
    private static void checkAssociatedRooms(RoomType existingRoomType) {
        Set<Room> associatedRooms = existingRoomType.getRooms();

        if (associatedRooms != null && !associatedRooms.isEmpty()) {
            throw new APIException("Room Type - " + existingRoomType.getName() + " cannot be deleted / set inactive as it is associated with " + associatedRooms.size() + " rooms");
        }
    }

    // Helper method to fetch pricing rules
    private Set<RoomPricingRule> fetchPricingRules(Set<Integer> ruleIds) {
        return ruleIds.stream()
                .map(ruleId -> roomPricingRuleService.getRoomPricingRuleById(ruleId))
                .collect(Collectors.toSet());
    }
}
