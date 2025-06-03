package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Gender;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface for managing gender service.
public interface GenderService {

    //Fetches a list of genders.
    List<ReferenceDataDTO> getAllGenders();

    // get gender by id
    Gender getGenderById(Integer id);
}
