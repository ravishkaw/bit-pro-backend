package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.CivilStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface for managing civil status service.
public interface CivilStatusService {

    //Fetches a list of Room status.
    List<ReferenceDataDTO> getAllCivilStatus();

    // get civil status by id
    CivilStatus getCivilStatusById(Integer id);
}
