package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.RoomPackage;
import com.ravi.waterlilly.payload.roomPackage.RoomPackageBasicDTO;
import com.ravi.waterlilly.payload.roomPackage.RoomPackagePageResponse;
import com.ravi.waterlilly.payload.roomPackage.RoomPackagePayloadDTO;

import java.math.BigDecimal;
import java.util.List;

// service layer of packages
public interface RoomPackageService {

    // get all packages
    RoomPackagePageResponse getAllRoomPackages(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // get all packages no pagination
    List<RoomPackageBasicDTO> getAllRoomPackagesNoPagination();

    // get single room package
    RoomPackagePayloadDTO getSingleRoomPackage(Integer packageId);

    // add new room package
    void createRoomPackage(RoomPackagePayloadDTO packageDTO);

    // update room package
    void updateRoomPackage(Integer packageId, RoomPackagePayloadDTO packageDTO);

    // delete room package
    void deleteRoomPackage(Integer packageId);

    // restore a room package
    void restoreRoomPackage(Integer packageId);

    // get package by id
    RoomPackage getRoomPackageById(Integer packageId);

    // Helper method to calculate room package price
    BigDecimal calculateRoomPackagePrice(RoomPackage roomPackage);
}
