package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.RoomFacility;
import com.ravi.waterlilly.payload.roomFacility.RoomFacilityPayloadDTO;
import com.ravi.waterlilly.payload.roomFacility.RoomFacilityTableResponse;
import com.ravi.waterlilly.repository.RoomFacilityRepository;
import com.ravi.waterlilly.service.RoomFacilityService;
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

// Implementation of room facility service
@Service
@RequiredArgsConstructor
public class RoomFacilityServiceImpl implements RoomFacilityService {
    private final RoomFacilityRepository roomFacilityRepository;
    private final ModelMapper modelMapper;
    private final PrivilegeUtils privilegeUtils;
    private final StatusService statusService;

    // Get all facilities with pagination and sorting
    @Override
    public RoomFacilityTableResponse getAllFacilities(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {

        // check privileges
        privilegeUtils.privilegeCheck("Room Facility", AppConstants.SELECT);

        // Determine the sorting order and sort by
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Create a Pageable object for pagination and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<RoomFacility> roomFacilityPage = StringUtils.hasText(searchQuery)
                ? roomFacilityRepository.searchFacilities(searchQuery.trim(), pageable)
                : roomFacilityRepository.findAll(pageable);

        // Extract the list of room facilities from the page
        List<RoomFacility> pricingRules = roomFacilityPage.getContent();

        // Map the list of room facilities to a list of RoomFacilityBasicDTO using ModelMapper
        List<RoomFacilityPayloadDTO> facilityBasicDTOS = pricingRules.stream()
                .map(facility -> modelMapper.map(facility, RoomFacilityPayloadDTO.class))
                .toList();

        // Create a response object of room facility data
        return new RoomFacilityTableResponse(
                facilityBasicDTOS,
                roomFacilityPage.getNumber(),
                roomFacilityPage.getSize(),
                roomFacilityPage.getTotalElements(),
                roomFacilityPage.getTotalPages(),
                roomFacilityPage.isLast()
        );
    }

    // get all active facilities
    @Override
    public List<RoomFacilityPayloadDTO> getFacilities() {
        // privilege check
        privilegeUtils.privilegeCheck("Room Facility", AppConstants.SELECT);

        return roomFacilityRepository.getActiveFacilities().stream()
                .map(roomFacility -> modelMapper.map(roomFacility, RoomFacilityPayloadDTO.class))
                .toList();
    }

    // fetch single facility
    @Override
    public RoomFacilityPayloadDTO getSingleFacility(Integer facilityId) {
        // privilege check
        privilegeUtils.privilegeCheck("Room Facility", AppConstants.SELECT);

        // find, map and return
        RoomFacility roomFacility = getRoomFacilityById(facilityId);

        return modelMapper.map(roomFacility, RoomFacilityPayloadDTO.class);
    }

    // add new facility
    @Override
    public void addNewFacility(RoomFacilityPayloadDTO roomFacilityDTO) {
        // privilege check
        privilegeUtils.privilegeCheck("Room Facility", AppConstants.INSERT);

        // check if the room facility is already in database
        validateRoomFacilityUniqueness(roomFacilityDTO, null);

        // map and save
        RoomFacility newFacility = new RoomFacility();
        updateRoomFacilityFields(newFacility, roomFacilityDTO);

        roomFacilityRepository.save(newFacility);
    }

    // update an existing facility
    @Override
    public void updateFacility(RoomFacilityPayloadDTO roomFacilityDTO, Integer facilityId) {
        // privilege check
        privilegeUtils.privilegeCheck("Room Facility", AppConstants.UPDATE);

        // check if the facility exists
        RoomFacility existingFacility = getRoomFacilityById(facilityId);

        // check if the room facility is already in database
        validateRoomFacilityUniqueness(roomFacilityDTO, facilityId);

        // set details and save
        updateRoomFacilityFields(existingFacility, roomFacilityDTO);

        roomFacilityRepository.save(existingFacility);
    }

    // delete an facility - soft delete
    @Override
    public void deleteFacility(Integer facilityId) {
        // privilege check
        privilegeUtils.privilegeCheck("Room Facility", AppConstants.DELETE);

        // check if the facility exists
        RoomFacility existingFacility = getRoomFacilityById(facilityId);

        existingFacility.setStatus(statusService.getDeletedStatus());
        roomFacilityRepository.save(existingFacility);
    }

    // restore room facility
    @Override
    public void restoreRoomFacility(Integer facilityId) {
        // privilege check
        privilegeUtils.privilegeCheck("Room Facility", AppConstants.UPDATE);

        // check if the facility exists
        RoomFacility existingFacility = getRoomFacilityById(facilityId);

        existingFacility.setStatus(statusService.getActiveStatus());
        roomFacilityRepository.save(existingFacility);
    }

    // get room facility by id
    @Override
    public RoomFacility getRoomFacilityById(Integer facilityId) {
        return roomFacilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Room Facility", "facilityId", String.valueOf(facilityId)));
    }

    // Helper method to update room facility fields
    private void updateRoomFacilityFields(RoomFacility roomFacility, RoomFacilityPayloadDTO facilityFormDTO) {
        roomFacility.setName(facilityFormDTO.getName());
        roomFacility.setDescription(facilityFormDTO.getDescription());
        roomFacility.setPrice(facilityFormDTO.getPrice());
        roomFacility.setStatus(statusService.getStatusByName(facilityFormDTO.getStatusName()));
    }

    // Helper method to check if the room facility is in db
    private void validateRoomFacilityUniqueness(RoomFacilityPayloadDTO facilityFormDTOr, Integer facilityId) {

        // Check duplicate facility name
        RoomFacility duplicateFacility = roomFacilityRepository.findByFacilityName(facilityFormDTOr.getName());

        if (duplicateFacility != null && !duplicateFacility.getId().equals(facilityId))
            throw new APIException("Facility with name " + facilityFormDTOr.getName() + " already exists!");
    }
}