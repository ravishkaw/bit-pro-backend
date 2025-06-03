package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.EventPackage;
import com.ravi.waterlilly.payload.eventPackage.EventPackageBasicDTO;
import com.ravi.waterlilly.payload.eventPackage.EventPackagePageResponse;
import com.ravi.waterlilly.payload.eventPackage.EventPackagePayloadDTO;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

// service layer of event package
public interface EventPackageService {

    // get all packages
    @Transactional
    EventPackagePageResponse getAllEventPackages(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // get all packages without pagination
    List<EventPackageBasicDTO> getAllEventPackagesNoPagination();

    // get single event package
    EventPackagePayloadDTO getSinglePackage(Integer packageId);

    // add new event package
    @Transactional
    void createEventPackage(EventPackagePayloadDTO packageDTO);

    // update event package
    @Transactional
    void updateEventPackage(Integer packageId, EventPackagePayloadDTO packageDTO);

    // delete event package
    @Transactional
    void deleteEventPackage(Integer packageId);

    // restore a event package
    @Transactional
    void restoreEventPackage(Integer packageId);

    // get package by id
    EventPackage getEventPackageById(Integer packageId);

    // Helper method to calculate event package price
    BigDecimal calculateEventPackagePrice(EventPackage eventPackage);
}
