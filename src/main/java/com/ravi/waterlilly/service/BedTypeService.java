package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.BedType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// service layer of bed type
public interface BedTypeService {

    // get all bed types
    List<ReferenceDataDTO> getAllBedTypes();

    // get a single bed type
    ReferenceDataDTO getSingleBedType(Integer bedTypeId);

    // add new bed type
    void addNewBedType(ReferenceDataDTO bedTypeDTO);

    // update existing bed type
    void updateBedType(ReferenceDataDTO bedTypeDTO, Integer bedTypeId);

    // delete bed type
    void deleteBedType(Integer bedTypeId);

    // get bed type by id
    BedType getBedTypeById(Integer bedTypeId);
}
