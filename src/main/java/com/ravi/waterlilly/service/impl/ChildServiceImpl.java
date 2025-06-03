package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Child;
import com.ravi.waterlilly.payload.child.ChildPageResponse;
import com.ravi.waterlilly.payload.child.ChildPayloadDTO;
import com.ravi.waterlilly.payload.child.ChildTableDataDTO;
import com.ravi.waterlilly.repository.ChildRepository;
import com.ravi.waterlilly.service.ChildService;
import com.ravi.waterlilly.service.GenderService;
import com.ravi.waterlilly.service.GuestService;
import com.ravi.waterlilly.service.NationalityService;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

// implementation of child service
@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final ChildRepository childRepository;
    private final ModelMapper modelMapper;
    private final PrivilegeUtils privilegeUtils;
    private final NationalityService nationalityService;
    private final GenderService genderService;
    private final GuestService guestService;


    // get all children
    @Override
    public ChildPageResponse getAllChildren(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {
        // privilege check
        privilegeUtils.privilegeCheck("Child", AppConstants.SELECT);

        // Create sort object
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Create pageable object
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);

        // Get page of children
        Page<Child> childPage = StringUtils.hasText(searchQuery)
                ? childRepository.searchChildren(searchQuery.trim(), pageable)
                : childRepository.findAll(pageable);

        // Convert to DTOs
        List<ChildTableDataDTO> childTableDataDTOs = childPage.getContent().stream()
                .map(child -> modelMapper.map(child, ChildTableDataDTO.class))
                .toList();

        // Create and return response
        return new ChildPageResponse(
                childTableDataDTOs,
                childPage.getNumber(),
                childPage.getSize(),
                childPage.getTotalElements(),
                childPage.getTotalPages(),
                childPage.isLast()
        );
    }

    // get all children without pagination
    @Override
    public List<ChildPayloadDTO> getAllChildrenNoPagination() {

        // check privileges
        privilegeUtils.privilegeCheck("Child", AppConstants.SELECT);

        return childRepository.findAll().stream()
                .map(child -> modelMapper.map(child, ChildPayloadDTO.class))
                .toList();
    }

    // get single child
    @Override
    public ChildPayloadDTO getAChild(Long childId) {
        // privilege check
        privilegeUtils.privilegeCheck("Child", AppConstants.SELECT);

        Child child = getChildById(childId);

        return modelMapper.map(child, ChildPayloadDTO.class);
    }

    // add new child
    @Override
    public void addChild(ChildPayloadDTO childDTO) {
        // privilege check
        privilegeUtils.privilegeCheck("Child", AppConstants.INSERT);

        // Create and save new child
        Child newChild = new Child();
        updateChildFields(newChild, childDTO);
        childRepository.save(newChild);
    }

    // update a child
    @Override
    public void updateChild(ChildPayloadDTO childDTO, Long childId) {
        // privilege check
        privilegeUtils.privilegeCheck("Child", AppConstants.UPDATE);

        // Check if child exists
        Child existingChild = getChildById(childId);

        // Update and save child
        updateChildFields(existingChild, childDTO);
        childRepository.save(existingChild);
    }

    // delete a child
    @Override
    public void deleteChild(Long childId) {
        // privilege check
        privilegeUtils.privilegeCheck("Child", AppConstants.DELETE);

        // Check if child exists
        Child existingChild = getChildById(childId);

        // Delete child
        childRepository.delete(existingChild);
    }

    // get child by id
    @Override
    public Child getChildById(Long childId) {
        return childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "childId", childId));
    }


    // Helper method to update child fields
    private void updateChildFields(Child child, ChildPayloadDTO childDTO) {
        child.setFullName(childDTO.getFullName());
        child.setDob(childDTO.getDob());
        child.setGender(genderService.getGenderById(childDTO.getGenderId()));
        child.setNationality(nationalityService.getNationalityById(childDTO.getNationalityId()));
        child.setGuest(guestService.getGuestById(childDTO.getGuestId()));
    }
}