package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.InventoryStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// service layer of inventory status
public interface InventoryStatusService {

    // get all status
    List<ReferenceDataDTO> findAll();

    // get inventory status by id
    InventoryStatus getInventoryStatusById(Integer statusId);

    // Helper method to set inventory to in stock
    void setInventoryToInStock(Long inventoryId);

    // Helper method to set inventory out of stock
    void setInventoryToOutOfStock(Long inventoryId);
}
