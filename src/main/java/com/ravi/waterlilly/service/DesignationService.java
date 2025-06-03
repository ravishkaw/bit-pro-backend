package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Designation;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface of Designation-related services.
public interface DesignationService {

    //Fetches a list of designations.
    List<ReferenceDataDTO> getAllDesignations();

    // get designation by id
    Designation getDesignationById(Integer id);
}
