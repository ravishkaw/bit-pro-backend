package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Status;
import com.ravi.waterlilly.repository.StatusRepository;
import com.ravi.waterlilly.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// implementation of status service
@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    // get status by id
    @Override
    public Status getStatusById(Integer id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status", "id", id.toString()));
    }

    // get status by name
    @Override
    public Status getStatusByName(String statusName) {
        Status status = statusRepository.findStatusByName(statusName);
        if (status == null) {
            throw new ResourceNotFoundException("Status", "name", statusName);
        }
        return status;
    }

    // get active status
    @Override
    public Status getActiveStatus() {
        return statusRepository.findStatusByName("Active");
    }

    // get inactive status
    @Override
    public Status getInactiveStatus() {
        return statusRepository.findStatusByName("Inactive");
    }

    // get deleted status
    @Override
    public Status getDeletedStatus() {
        return statusRepository.findStatusByName("Deleted");
    }
}
