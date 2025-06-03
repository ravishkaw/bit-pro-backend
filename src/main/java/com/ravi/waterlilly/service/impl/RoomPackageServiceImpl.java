package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.RoomPackage;
import com.ravi.waterlilly.model.RoomReservationAmenity;
import com.ravi.waterlilly.payload.roomReservationAmenity.AmenityNameQuantityDTO;
import com.ravi.waterlilly.payload.roomPackage.RoomPackageBasicDTO;
import com.ravi.waterlilly.payload.roomPackage.RoomPackagePageResponse;
import com.ravi.waterlilly.payload.roomPackage.RoomPackagePayloadDTO;
import com.ravi.waterlilly.payload.roomReservationAmenity.AmenityQuantityDTO;
import com.ravi.waterlilly.repository.RoomPackageRepository;
import com.ravi.waterlilly.service.RoomPackageService;
import com.ravi.waterlilly.service.RoomReservationAmenityService;
import com.ravi.waterlilly.service.StatusService;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import jakarta.transaction.Transactional;
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
import java.util.Set;
import java.util.stream.Collectors;

// implementation of package service
@Service
@RequiredArgsConstructor
public class RoomPackageServiceImpl implements RoomPackageService {
    private final RoomPackageRepository roomPackageRepository;
    private final ModelMapper modelMapper;
    private final PrivilegeUtils privilegeUtils;
    private final RoomReservationAmenityService roomReservationAmenityService;
    private final StatusService statusService;

    // get all packages
    @Override
    @Transactional
    public RoomPackagePageResponse getAllRoomPackages(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {
        // Privilege check
        privilegeUtils.privilegeCheck("Package", AppConstants.SELECT);

        // Sorting
        Sort sort = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<RoomPackage> packagePage = StringUtils.hasText(searchQuery)
                ? roomPackageRepository.searchRoomPackages(searchQuery.trim(), pageable)
                : roomPackageRepository.findAll(pageable);

        List<RoomPackageBasicDTO> packageDTOs = packagePage.getContent().stream()
                .map(roomPackage -> getRoomPackageBasicDTO(roomPackage))
                .toList();

        return new RoomPackagePageResponse(
                packageDTOs,
                packagePage.getNumber(),
                packagePage.getSize(),
                packagePage.getTotalElements(),
                packagePage.getTotalPages(),
                packagePage.isLast()
        );
    }

    // get all packages without pagination
    @Override
    public List<RoomPackageBasicDTO> getAllRoomPackagesNoPagination() {

        // Check privileges
        privilegeUtils.privilegeCheck("Package", AppConstants.SELECT);

        return roomPackageRepository.findAll().stream()
                .map(packages -> getRoomPackageBasicDTO(packages))
                .toList();
    }

    // get single room package
    @Override
    public RoomPackagePayloadDTO getSingleRoomPackage(Integer packageId) {
        privilegeUtils.privilegeCheck("Package", AppConstants.SELECT);

        RoomPackage roomPackage = getRoomPackageById(packageId);

        RoomPackagePayloadDTO dto = modelMapper.map(roomPackage, RoomPackagePayloadDTO.class);

        // Convert amenities with quantities
        List<AmenityQuantityDTO> amenityDTOs = roomPackage.getRoomPackageAmenities().stream()
                .map(entry -> {
                    AmenityQuantityDTO amenityDTO = new AmenityQuantityDTO();
                    amenityDTO.setAmenityId(entry.getRoomReservationAmenity().getId());
                    amenityDTO.setQuantity(entry.getQuantity());
                    return amenityDTO;
                })
                .toList();

        dto.setAmenities(amenityDTOs);
        return dto;
    }

    // add new room package
    @Override
    @Transactional
    public void createRoomPackage(RoomPackagePayloadDTO packageDTO) {
        privilegeUtils.privilegeCheck("Package", AppConstants.INSERT);

        // Validate uniqueness
        if (roomPackageRepository.findByName(packageDTO.getName()) != null) {
            throw new APIException("Room Package with name " + packageDTO.getName() + " already exists");
        }

        // Create new package
        RoomPackage roomPackage = new RoomPackage();
        updateRoomPackageFields(packageDTO, roomPackage);
        roomPackageRepository.save(roomPackage);
    }

    // update room package
    @Override
    @Transactional
    public void updateRoomPackage(Integer packageId, RoomPackagePayloadDTO packageDTO) {
        privilegeUtils.privilegeCheck("Package", AppConstants.UPDATE);

        RoomPackage existingPackage = getRoomPackageById(packageId);

        // Validate uniqueness
        RoomPackage duplicate = roomPackageRepository.findByName(packageDTO.getName());
        if (duplicate != null && !duplicate.getId().equals(packageId)) {
            throw new APIException("Room Package with name " + packageDTO.getName() + " already exists");
        }

        // Update fields
        updateRoomPackageFields(packageDTO, existingPackage);
        roomPackageRepository.save(existingPackage);
    }

    // delete room package
    @Override
    @Transactional
    public void deleteRoomPackage(Integer packageId) {
        privilegeUtils.privilegeCheck("Package", AppConstants.DELETE);

        RoomPackage roomPackage = getRoomPackageById(packageId);

        roomPackage.setStatus(statusService.getDeletedStatus());
        roomPackageRepository.save(roomPackage);
    }

    // restore a room package
    @Override
    @Transactional
    public void restoreRoomPackage(Integer packageId) {
        privilegeUtils.privilegeCheck("Package", AppConstants.DELETE);

        RoomPackage roomPackage = getRoomPackageById(packageId);

        roomPackage.setStatus(statusService.getActiveStatus());
        roomPackageRepository.save(roomPackage);
    }

    // get package by id
    @Override
    public RoomPackage getRoomPackageById(Integer packageId) {
        return roomPackageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Room Package", "id", packageId.toString()));
    }

    // Helper method to calculate room package price
    @Override
    public BigDecimal calculateRoomPackagePrice(RoomPackage roomPackage) {
        List<AmenityQuantityDTO> amenities = roomPackage.getRoomPackageAmenities().stream()
                .map(amenity -> {
                            AmenityQuantityDTO amenityQuantity = new AmenityQuantityDTO();
                            amenityQuantity.setAmenityId(amenity.getRoomReservationAmenity().getId());
                            amenityQuantity.setQuantity(amenity.getQuantity());
                            return amenityQuantity;
                        }
                ).toList();

        if (amenities.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return amenities.stream()
                .map(amenity -> {
                    RoomReservationAmenity roomAmenity = roomReservationAmenityService.getAmenityById(amenity.getAmenityId());
                    return roomAmenity.getPrice().multiply(BigDecimal.valueOf(amenity.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Helper method to update room package fields
    private void updateRoomPackageFields(RoomPackagePayloadDTO packageDTO, RoomPackage roomPackage) {
        roomPackage.setName(packageDTO.getName());
        roomPackage.setDescription(packageDTO.getDescription());
        roomPackage.setStatus(statusService.getStatusByName(packageDTO.getStatusName()));

        updateRoomPackageAmenities(packageDTO, roomPackage);
    }

    // Helper method to update room package amenities
    private void updateRoomPackageAmenities(RoomPackagePayloadDTO packageDTO, RoomPackage roomPackage) {
        if (packageDTO.getAmenities() != null) {
            if (roomPackage.getRoomPackageAmenities() != null && !roomPackage.getRoomPackageAmenities().isEmpty()) {
                roomPackage.clearAmenities();
            }
            packageDTO.getAmenities().forEach(amenity -> {
                RoomReservationAmenity roomAmenity = roomReservationAmenityService.getAmenityById(amenity.getAmenityId());
                roomPackage.addAmenity(roomAmenity, amenity.getQuantity());
            });
        }
    }

    // Helper method to convert data into basic dto
    private RoomPackageBasicDTO getRoomPackageBasicDTO(RoomPackage roomPackage) {
        RoomPackageBasicDTO dto = modelMapper.map(roomPackage, RoomPackageBasicDTO.class);
        Set<AmenityNameQuantityDTO> amenityBasicDTOS = roomPackage.getRoomPackageAmenities().stream()
                .map(amenity -> {
                            AmenityNameQuantityDTO amenityBasicDTO = new AmenityNameQuantityDTO();
                            amenityBasicDTO.setAmenityName(amenity.getRoomReservationAmenity().getName());
                            amenityBasicDTO.setQuantity(amenity.getQuantity());
                            return amenityBasicDTO;
                        }
                ).collect(Collectors.toSet());
        dto.setAmenities(amenityBasicDTOS);
        dto.setPrice(calculateRoomPackagePrice(roomPackage));
        return dto;
    }
}