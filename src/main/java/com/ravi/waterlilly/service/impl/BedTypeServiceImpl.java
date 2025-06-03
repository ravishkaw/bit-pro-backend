package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.BedType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.BedTypeRepository;
import com.ravi.waterlilly.service.BedTypeService;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// implementation of bed type service
@Service
@RequiredArgsConstructor
public class BedTypeServiceImpl implements BedTypeService {
    private final ModelMapper modelMapper;
    private final BedTypeRepository bedTypeRepository;
    private final PrivilegeUtils privilegeUtils;

    // get all bed types
    @Override
    public List<ReferenceDataDTO> getAllBedTypes() {
        // check privileges
        privilegeUtils.privilegeCheck("Bed Type", AppConstants.SELECT);

        return bedTypeRepository.findAll().stream()
                .map(bedType -> modelMapper.map(bedType, ReferenceDataDTO.class))
                .toList();
    }

    // get a single bed type
    @Override
    public ReferenceDataDTO getSingleBedType(Integer bedTypeId) {

        // check privileges
        privilegeUtils.privilegeCheck("Bed Type", AppConstants.SELECT);

        BedType bedType = bedTypeRepository.findById(bedTypeId)
                .orElseThrow(() -> new APIException("BedType not found"));

        return modelMapper.map(bedType, ReferenceDataDTO.class);
    }

    // add new bed type
    @Override
    public void addNewBedType(ReferenceDataDTO bedTypeDTO) {
        // check privileges
        privilegeUtils.privilegeCheck("Bed Type", AppConstants.INSERT);

        // check bed type exists
        validateBedTypeUniqueness(bedTypeDTO, null);

        BedType bedType = modelMapper.map(bedTypeDTO, BedType.class);
        bedTypeRepository.save(bedType);
    }

    // update existing bed type
    @Override
    public void updateBedType(ReferenceDataDTO bedTypeDTO, Integer bedTypeId) {

        // get existing bed type
        BedType existingBedType = bedTypeRepository.findById(bedTypeId)
                .orElseThrow(() -> new APIException("BedType not found"));

        // check bed type exists
        validateBedTypeUniqueness(bedTypeDTO, bedTypeId);

        modelMapper.map(bedTypeDTO, existingBedType);
        bedTypeRepository.save(existingBedType);
    }

    // delete bed type
    @Override
    public void deleteBedType(Integer bedTypeId) {

        // get existing bed type
        BedType existingBedType = bedTypeRepository.findById(bedTypeId)
                .orElseThrow(() -> new APIException("BedType not found"));

        // hard delete bed type
        bedTypeRepository.delete(existingBedType);
    }

    // get bed type by id
    @Override
    public BedType getBedTypeById(Integer bedTypeId) {
        return bedTypeRepository.findById(bedTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("BedTyp", "id", bedTypeId.toString()));
    }

    // Helper method to check bed type exists in db
    private void validateBedTypeUniqueness(ReferenceDataDTO bedTypeDTO, Integer bedTypeId) {

        // check name
        BedType existingBedType = bedTypeRepository.findBedTypeByName(bedTypeDTO.getName());
        if (existingBedType != null && !existingBedType.getId().equals(bedTypeId))
            throw new APIException("BedType with name " + bedTypeDTO.getName() + " already exists");
    }
}
