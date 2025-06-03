package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.InventoryItemType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// service interface of inventory item type
public interface InventoryItemTypeService {

    // get all inventory item types
    List<ReferenceDataDTO> getAllTypes();

    // get inventory item type by id
    InventoryItemType getInventoryItemTypeById(Integer id);
}
