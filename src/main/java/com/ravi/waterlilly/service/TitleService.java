package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Title;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

//  Service interface for managing title
public interface TitleService {

    // get all titles
    List<ReferenceDataDTO> getAllTitles();

    // get title by id
    Title getTitleById(Integer id);
}
