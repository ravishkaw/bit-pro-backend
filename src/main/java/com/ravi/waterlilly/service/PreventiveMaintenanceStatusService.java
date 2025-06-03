package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.PreventiveMaintenanceStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// service layer of preventive maintenance status
public interface PreventiveMaintenanceStatusService {

    // get all statuses
    List<ReferenceDataDTO> getAllStatus();

    // get status by id
    PreventiveMaintenanceStatus getStatusById(Integer id);

    // set cancelled status
    void setCancelledStatus(Integer id);
}
