package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.IDType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface for managing id type service.
public interface IDTypeService {

    //Fetches a list of id type.
    List<ReferenceDataDTO> getAllIDTypes();

    // get id type by id
    IDType getIdTypeById(Integer id);
}
