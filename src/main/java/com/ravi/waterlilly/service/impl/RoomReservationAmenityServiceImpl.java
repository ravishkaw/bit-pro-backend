package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.RoomReservationAmenity;
import com.ravi.waterlilly.model.Status;
import com.ravi.waterlilly.payload.roomReservationAmenity.AmenityQuantityDTO;
import com.ravi.waterlilly.payload.roomReservationAmenity.RoomReservationAmenityPageResponse;
import com.ravi.waterlilly.payload.roomReservationAmenity.RoomReservationAmenityTableData;
import com.ravi.waterlilly.payload.roomReservationAmenity.RoomReservationAmenityPayloadDTO;
import com.ravi.waterlilly.repository.RoomReservationAmenityRepository;
import com.ravi.waterlilly.service.RoomReservationAmenityCategoryService;
import com.ravi.waterlilly.service.RoomReservationAmenityService;
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
import java.util.List;

// implementation of room reservation amenity service
@Service
@RequiredArgsConstructor
public class RoomReservationAmenityServiceImpl implements RoomReservationAmenityService {
    private final RoomReservationAmenityRepository roomReservationAmenityRepository;
    private final ModelMapper modelMapper;
    private final PrivilegeUtils privilegeUtils;
    private final StatusService statusService;
    private final RoomReservationAmenityCategoryService categoryService;

    // get all room reservation amenity
    @Override
    public RoomReservationAmenityPageResponse getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {

        // privilege check
        privilegeUtils.privilegeCheck("Room Reservation Amenity", AppConstants.SELECT);

        // Determine the sorting order and sort by
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Create a Pageable object for pagination and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<RoomReservationAmenity> amenityPage = StringUtils.hasText(searchQuery)
                ? roomReservationAmenityRepository.searchReservationAmenity(searchQuery.trim(), pageable)
                : roomReservationAmenityRepository.findAll(pageable);

        // Extract the list
        List<RoomReservationAmenity> amenityList = amenityPage.getContent();

        // Map to table data
        List<RoomReservationAmenityTableData> dto = amenityList.stream()
                .map(amenity -> modelMapper.map(amenity, RoomReservationAmenityTableData.class))
                .toList();

        // Create a response object of room type data
        return new RoomReservationAmenityPageResponse(
                dto,
                amenityPage.getNumber(),
                amenityPage.getSize(),
                amenityPage.getTotalElements(),
                amenityPage.getTotalPages(),
                amenityPage.isLast()
        );
    }

    // get all without pagination
    @Override
    public List<RoomReservationAmenityTableData> getRoomReservationAmenities() {
        return roomReservationAmenityRepository.findAll().stream()
                .map(amenities -> modelMapper.map(amenities, RoomReservationAmenityTableData.class))
                .toList();
    }

    // get single reservation amenity
    @Override
    public RoomReservationAmenityPayloadDTO getSingleAmenity(Integer amenityId) {
        // privilege check
        privilegeUtils.privilegeCheck("Room Reservation Amenity", AppConstants.SELECT);

        RoomReservationAmenity amenity = getAmenityById(amenityId);

        return modelMapper.map(amenity, RoomReservationAmenityPayloadDTO.class);
    }

    // add new amenity
    @Override
    public void addRoomReservationAmenity(RoomReservationAmenityPayloadDTO amenityPayloadDTO) {
        // privilege check
        privilegeUtils.privilegeCheck("Room Reservation Amenity", AppConstants.INSERT);

        // check amenity is in db
        validateAmenityUniqueness(amenityPayloadDTO, null);

        // save
        RoomReservationAmenity amenity = new RoomReservationAmenity();
        updateAmenityFields(amenityPayloadDTO, amenity);
        roomReservationAmenityRepository.save(amenity);
    }

    // update an amenity
    @Override
    public void updateRoomReservationAmenity(RoomReservationAmenityPayloadDTO amenityPayloadDTO, Integer amenityId) {
        // privilege check
        privilegeUtils.privilegeCheck("Room Reservation Amenity", AppConstants.UPDATE);

        // get existing amenity
        RoomReservationAmenity existingAmenity = getAmenityById(amenityId);

        // check amenity is in db
        validateAmenityUniqueness(amenityPayloadDTO, amenityId);

        // map and save
        updateAmenityFields(amenityPayloadDTO, existingAmenity);
        roomReservationAmenityRepository.save(existingAmenity);
    }

    // delete amenity ( change status )
    @Override
    public void deleteRoomReservationAmenity(Integer amenityId) {
        // privilege check
        privilegeUtils.privilegeCheck("Room Reservation Amenity", AppConstants.DELETE);

        // get existing amenity
        RoomReservationAmenity existingAmenity = getAmenityById(amenityId);

        // get deleted status and save
        Status status = statusService.getDeletedStatus();
        existingAmenity.setStatus(status);
        roomReservationAmenityRepository.save(existingAmenity);
    }

    // restore amenity ( change status )
    @Override
    public void restoreRoomReservationAmenity(Integer amenityId) {
        // privilege check
        privilegeUtils.privilegeCheck("Room Reservation Amenity", AppConstants.UPDATE);

        // get existing amenity
        RoomReservationAmenity existingAmenity = getAmenityById(amenityId);

        // get active status and save
        Status status = statusService.getActiveStatus();
        existingAmenity.setStatus(status);
        roomReservationAmenityRepository.save(existingAmenity);
    }

    // get amenity by id
    @Override
    public RoomReservationAmenity getAmenityById(Integer amenityId) {
        return roomReservationAmenityRepository.findById(amenityId)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity", "amenityId", String.valueOf(amenityId)));
    }

    // Helper method to calculate amenity price
    @Override
    public BigDecimal calculateAmenityPrice(List<AmenityQuantityDTO> amenities) {
        if (amenities == null || amenities.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return amenities.stream()
                .map(amenity -> {
                    RoomReservationAmenity roomAmenity = getAmenityById(amenity.getAmenityId());
                    return roomAmenity.getPrice().multiply(BigDecimal.valueOf(amenity.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    // Helper method to update room amenity fields
    private void updateAmenityFields(RoomReservationAmenityPayloadDTO amenityPayloadDTO, RoomReservationAmenity amenity) {
        amenity.setName(amenityPayloadDTO.getName());
        amenity.setDescription(amenityPayloadDTO.getDescription());
        amenity.setPrice(amenityPayloadDTO.getPrice());
        amenity.setCategory(categoryService.getCategoryById(amenityPayloadDTO.getCategoryId()));
        amenity.setStatus(statusService.getStatusByName(amenityPayloadDTO.getStatusName()));
    }

    // check amenity is in the db
    private void validateAmenityUniqueness(RoomReservationAmenityPayloadDTO amenityPayloadDTO, Integer amenityId) {
        // check existing amenity
        RoomReservationAmenity duplicateAmenity = roomReservationAmenityRepository.findByName(amenityPayloadDTO.getName());

        if (duplicateAmenity != null && !duplicateAmenity.getId().equals(amenityId))
            throw new APIException("Amenity with name " + amenityPayloadDTO.getName() + " already exists");
    }
}
