package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.InventoryItemType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.InventoryItemTypeRepository;
import com.ravi.waterlilly.service.InventoryItemTypeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// implementation of inventory item type
@Service
@RequiredArgsConstructor
public class InventoryItemTypeServiceImpl implements InventoryItemTypeService {
    private final InventoryItemTypeRepository inventoryItemTypeRepository;
    private final ModelMapper modelMapper;

    // get all inventory item types
    @Override
    public List<ReferenceDataDTO> getAllTypes() {
        return inventoryItemTypeRepository.findAll().stream()
                .map(itemType -> modelMapper.map(itemType, ReferenceDataDTO.class))
                .toList();
    }

    // get inventory item type by id
    @Override
    public InventoryItemType getInventoryItemTypeById(Integer id) {
        return inventoryItemTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item Type", "id", id.toString()));
    }
}
