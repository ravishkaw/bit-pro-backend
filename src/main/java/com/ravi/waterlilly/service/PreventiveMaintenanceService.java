package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.PreventiveMaintenance;
import com.ravi.waterlilly.payload.preventiveMaintenance.PreventiveMaintenancePageResponse;
import com.ravi.waterlilly.payload.preventiveMaintenance.PreventiveMaintenancePayloadDTO;

// service layer of preventive maintenance
public interface PreventiveMaintenanceService {

    // get all maintenances
    PreventiveMaintenancePageResponse getMaintenances(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // get single maintenance record
    PreventiveMaintenancePayloadDTO getSingleMaintenance(Integer id);

    // get maintenance by id
    PreventiveMaintenance getMaintenanceById(Integer id);

    // add new maintenance record
    void addNewMaintenance(PreventiveMaintenancePayloadDTO maintenancePayloadDTO);

    // update maintenance record
    void updateMaintenance(PreventiveMaintenancePayloadDTO maintenancePayloadDTO, Integer id);

    // delete maintenance record
    void deleteMaintenance(Integer id);
}
