package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.IDType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.IDTypeRepository;
import com.ravi.waterlilly.service.IDTypeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of id types Service.
@Service
@RequiredArgsConstructor
public class IDTypeServiceImpl implements IDTypeService {
    private final IDTypeRepository idTypeRepository;
    private final ModelMapper modelMapper;

    //Fetch all id types
    @Override
    public List<ReferenceDataDTO> getAllIDTypes() {
        return idTypeRepository.findAll().stream()
                .map((status -> modelMapper.map(status, ReferenceDataDTO.class)))
                .toList();
    }

    // get id type by id
    @Override
    public IDType getIdTypeById(Integer id) {
        return idTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ID Type", "id", id.toString()));
    }
}
