package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.TimeUnitRepository;
import com.ravi.waterlilly.service.TimeUnitService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of TimeUnit Service.
@Service
@RequiredArgsConstructor
public class TimeUnitServiceImpl implements TimeUnitService {
    private final ModelMapper modelMapper;
    private final TimeUnitRepository timeUnitRepository;

    //Fetch all time units
    @Override
    public List<ReferenceDataDTO> getAllTimeUnits() {
        return timeUnitRepository.findAll().stream()
                .map((timeUnit -> modelMapper.map(timeUnit, ReferenceDataDTO.class)))
                .toList();
    }
}