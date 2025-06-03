package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Designation;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.DesignationRepository;
import com.ravi.waterlilly.service.DesignationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of DesignationService.
@Service
@RequiredArgsConstructor
public class DesignationServiceImpl implements DesignationService {
    private final DesignationRepository designationRepository;
    private final ModelMapper modelMapper;

    //Get all designations
    @Override
    public List<ReferenceDataDTO> getAllDesignations() {

        //Get all designations, map and return
        return designationRepository.findAll().stream()
                .map((designation -> modelMapper.map(designation, ReferenceDataDTO.class)))
                .toList();
    }

    // get designation by id
    @Override
    public Designation getDesignationById(Integer id) {
        return designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation", "id", id.toString()));
    }
}
