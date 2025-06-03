package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Nationality;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface of Nationality-related services.
public interface NationalityService {

    //Fetches a list of nationalities.
    List<ReferenceDataDTO> getAllNationalities();

    // get nationality by id
    Nationality getNationalityById(Integer id);
}
