package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Child;
import com.ravi.waterlilly.payload.child.ChildPageResponse;
import com.ravi.waterlilly.payload.child.ChildPayloadDTO;

import java.util.List;

// service layer of child
public interface ChildService {
    // get all children
    ChildPageResponse getAllChildren(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // Get all children without pagination
    List<ChildPayloadDTO> getAllChildrenNoPagination();

    // get single child
    ChildPayloadDTO getAChild(Long childId);

    // add a new child
    void addChild(ChildPayloadDTO childDTO);

    // update a child
    void updateChild(ChildPayloadDTO childDTO, Long childId);

    // delete a child
    void deleteChild(Long childId);

    // get child by id
    Child getChildById(Long childId);
}