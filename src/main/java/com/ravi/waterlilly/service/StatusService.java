package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Status;

// service layer of status service
public interface StatusService {
    // get status by id
    Status getStatusById(Integer id);

    // get status by name
    Status getStatusByName(String statusName);

    // get active status
    Status getActiveStatus();

    // get inactive status
    Status getInactiveStatus();

    // get deleted status
    Status getDeletedStatus();
}
