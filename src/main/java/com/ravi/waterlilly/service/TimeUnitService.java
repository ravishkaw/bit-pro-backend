package com.ravi.waterlilly.service;

import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface for managing time units.
public interface TimeUnitService {

    //Fetches a list of time units.
    List<ReferenceDataDTO> getAllTimeUnits();
}