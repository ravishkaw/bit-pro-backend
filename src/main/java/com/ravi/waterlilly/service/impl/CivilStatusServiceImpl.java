package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.CivilStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.CivilStatusRepository;
import com.ravi.waterlilly.service.CivilStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of civil Status Service.
@Service
@RequiredArgsConstructor
public class CivilStatusServiceImpl implements CivilStatusService {
    private final ModelMapper modelMapper;
    private final CivilStatusRepository civilStatusRepository;

    //Fetch all civil status
    @Override
    public List<ReferenceDataDTO> getAllCivilStatus() {
        return civilStatusRepository.findAll().stream()
                .map((civilStatus -> modelMapper.map(civilStatus, ReferenceDataDTO.class)))
                .toList();
    }

    // get civil status by id
    @Override
    public CivilStatus getCivilStatusById(Integer id) {
        return civilStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Civil Status", "id", id.toString()));
    }
}
