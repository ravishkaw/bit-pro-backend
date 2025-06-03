package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Gender;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.GenderRepository;
import com.ravi.waterlilly.service.GenderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of gender Service.
@Service
@RequiredArgsConstructor
public class GenderServiceImpl implements GenderService {
    private final ModelMapper modelMapper;
    private final GenderRepository genderRepository;

    //Fetch all genders
    @Override
    public List<ReferenceDataDTO> getAllGenders() {
        return genderRepository.findAll().stream()
                .map((genders -> modelMapper.map(genders, ReferenceDataDTO.class)))
                .toList();
    }

    // get gender by id
    @Override
    public Gender getGenderById(Integer id) {
        return genderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gender", "id", id.toString()));
    }
}
