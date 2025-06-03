package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.EventPackage;
import com.ravi.waterlilly.model.EventService;
import com.ravi.waterlilly.payload.eventPackage.EventPackageBasicDTO;
import com.ravi.waterlilly.payload.eventPackage.EventPackageServiceNameDTO;
import com.ravi.waterlilly.payload.eventPackage.EventPackagePageResponse;
import com.ravi.waterlilly.payload.eventPackage.EventPackagePayloadDTO;
import com.ravi.waterlilly.payload.eventService.EventServiceQuantityDTO;
import com.ravi.waterlilly.repository.EventPackageRepository;
import com.ravi.waterlilly.service.EventPackageService;
import com.ravi.waterlilly.service.EventServicesService;
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

// implementation of package service to handle event package related operations.
@Service
@RequiredArgsConstructor
public class EventPackageServiceImpl implements EventPackageService {
    private final EventPackageRepository eventPackageRepository;
    private final ModelMapper modelMapper;
    private final PrivilegeUtils privilegeUtils;
    private final EventServicesService eventServicesService;
    private final StatusService statusService;

    // get all packages
    @Transactional
    @Override
    public EventPackagePageResponse getAllEventPackages(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Package", AppConstants.SELECT);

        Sort sort = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<EventPackage> packagePage = StringUtils.hasText(searchQuery)
                ? eventPackageRepository.searchEventPackages(searchQuery.trim(), pageable)
                : eventPackageRepository.findAll(pageable);

        List<EventPackageBasicDTO> packageDTOs = packagePage.getContent().stream()
                .map(this::getEventPackageBasicDTO)
                .toList();

        return new EventPackagePageResponse(
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
    public List<EventPackageBasicDTO> getAllEventPackagesNoPagination() {

        // check privileges
        privilegeUtils.privilegeCheck("Event Package", AppConstants.SELECT);

        return eventPackageRepository.findAll().stream()
                .map(this::getEventPackageBasicDTO)
                .toList();
    }

    // get single event package
    @Override
    public EventPackagePayloadDTO getSinglePackage(Integer packageId) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Package", AppConstants.SELECT);

        EventPackage eventPackage = getEventPackageById(packageId);

        EventPackagePayloadDTO dto = modelMapper.map(eventPackage, EventPackagePayloadDTO.class);

        List<EventServiceQuantityDTO> serviceDTOs = eventPackage.getEventPackageHasServices().stream()
                .map(entry -> {
                    EventServiceQuantityDTO serviceDTO = new EventServiceQuantityDTO();
                    serviceDTO.setEventServiceId(entry.getEventService().getId());
                    serviceDTO.setQuantity(entry.getQuantity());
                    return serviceDTO;
                })
                .toList();

        dto.setEventServices(serviceDTOs);
        return dto;
    }

    // add new event package
    @Transactional
    @Override
    public void createEventPackage(EventPackagePayloadDTO packageDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Package", AppConstants.INSERT);

        validatePackageUniqueness(null, packageDTO);

        EventPackage eventPackage = new EventPackage();
        updateEventPackageFields(packageDTO, eventPackage);
        eventPackageRepository.save(eventPackage);
    }


    // update event package
    @Transactional
    @Override
    public void updateEventPackage(Integer packageId, EventPackagePayloadDTO packageDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Package", AppConstants.UPDATE);

        EventPackage existingPackage = getEventPackageById(packageId);

        validatePackageUniqueness(packageId, packageDTO);

        updateEventPackageFields(packageDTO, existingPackage);
        eventPackageRepository.save(existingPackage);
    }


    // delete event package
    @Transactional
    @Override
    public void deleteEventPackage(Integer packageId) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Package", AppConstants.DELETE);

        EventPackage eventPackage = getEventPackageById(packageId);

        eventPackage.setStatus(statusService.getDeletedStatus());
        eventPackageRepository.save(eventPackage);
    }

    // restore a event package
    @Transactional
    @Override
    public void restoreEventPackage(Integer packageId) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Package", AppConstants.DELETE);

        EventPackage eventPackage = getEventPackageById(packageId);

        eventPackage.setStatus(statusService.getActiveStatus());
        eventPackageRepository.save(eventPackage);
    }

    // get package by id
    @Override
    public EventPackage getEventPackageById(Integer packageId) {
        return eventPackageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Event Package", "id", packageId.toString()));
    }

    // Helper method to calculate event package price
    @Override
    public BigDecimal calculateEventPackagePrice(EventPackage eventPackage) {
        List<EventServiceQuantityDTO> services = eventPackage.getEventPackageHasServices().stream()
                .map(entry -> {
                    EventServiceQuantityDTO serviceDTO = new EventServiceQuantityDTO();
                    serviceDTO.setEventServiceId(entry.getEventService().getId());
                    serviceDTO.setQuantity(entry.getQuantity());
                    return serviceDTO;
                })
                .toList();

        if (services.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return services.stream()
                .map(service -> {
                    EventService eventService = eventServicesService.getEventServiceById(service.getEventServiceId());
                    return eventService.getPricePerUnit().multiply(BigDecimal.valueOf(service.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Helper method to update event package fields
    private void updateEventPackageFields(EventPackagePayloadDTO packageDTO, EventPackage eventPackage) {
        eventPackage.setName(packageDTO.getName());
        eventPackage.setDescription(packageDTO.getDescription());
        eventPackage.setStatus(statusService.getStatusByName(packageDTO.getStatusName()));

        updateEventPackageServices(packageDTO, eventPackage);
    }

    // Helper method to update event package services
    private void updateEventPackageServices(EventPackagePayloadDTO packageDTO, EventPackage eventPackage) {
        eventPackage.clearServices();
        if (packageDTO.getEventServices() != null) {
            packageDTO.getEventServices().forEach(service -> {
                EventService eventService = eventServicesService.getEventServiceById(service.getEventServiceId());
                eventPackage.addService(eventService, service.getQuantity());
            });
        }
    }

    // validation
    private void validatePackageUniqueness(Integer packageId, EventPackagePayloadDTO packageDTO) {
        EventPackage duplicate = eventPackageRepository.findByName(packageDTO.getName());
        if (duplicate != null && !duplicate.getId().equals(packageId)) {
            throw new APIException("Event Package with name " + packageDTO.getName() + " already exists");
        }
    }

    // Helper method to convert event package to basic DTO
    private EventPackageBasicDTO getEventPackageBasicDTO(EventPackage eventPackage) {
        EventPackageBasicDTO dto = modelMapper.map(eventPackage, EventPackageBasicDTO.class);

        // Map services
        Set<EventPackageServiceNameDTO> serviceBasicDTOs = eventPackage.getEventPackageHasServices().stream()
                .map(service -> {
                    EventPackageServiceNameDTO serviceBasicDTO = new EventPackageServiceNameDTO();
                    serviceBasicDTO.setServiceName(service.getEventService().getName());
                    serviceBasicDTO.setQuantity(service.getQuantity());
                    return serviceBasicDTO;
                }).collect(Collectors.toSet());
        dto.setServices(serviceBasicDTOs);
        dto.setPrice(calculateEventPackagePrice(eventPackage));
        return dto;
    }
}