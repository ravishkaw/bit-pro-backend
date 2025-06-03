package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Nationality;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.NationalityRepository;
import com.ravi.waterlilly.service.NationalityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of NationalityService.
@Service
@RequiredArgsConstructor
public class NationalityServiceImpl implements NationalityService {
    private final ModelMapper modelMapper;
    private final NationalityRepository nationalityRepository;

    //Get all nationalities
    @Override
    public List<ReferenceDataDTO> getAllNationalities() {

        //Get all nationalities, map and return
        return nationalityRepository.findAll().stream()
                .map((nationalities -> modelMapper.map(nationalities, ReferenceDataDTO.class)))
                .toList();
    }

    // get nationality by id
    @Override
    public Nationality getNationalityById(Integer id) {
        return nationalityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nationality", "id", id.toString()));
    }
}
