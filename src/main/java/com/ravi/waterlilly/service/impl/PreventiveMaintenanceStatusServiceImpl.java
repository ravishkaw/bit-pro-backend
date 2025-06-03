package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.PreventiveMaintenance;
import com.ravi.waterlilly.model.PreventiveMaintenanceStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.PreventiveMaintenanceRepository;
import com.ravi.waterlilly.repository.PreventiveMaintenanceStatusRepository;
import com.ravi.waterlilly.service.PreventiveMaintenanceStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// implementation of task status service
@Service
@RequiredArgsConstructor
public class PreventiveMaintenanceStatusServiceImpl implements PreventiveMaintenanceStatusService {
    private final ModelMapper modelMapper;
    private final PreventiveMaintenanceStatusRepository statusRepository;
    private final PreventiveMaintenanceRepository preventiveMaintenanceRepository;

    // fetch all status
    @Override
    public List<ReferenceDataDTO> getAllStatus() {
        return statusRepository.findAll().stream()
                .map((status -> modelMapper.map(status, ReferenceDataDTO.class)))
                .toList();
    }

    // set cancelled status
    @Override
    public void setCancelledStatus(Integer id) {
        PreventiveMaintenance maintenance = getMaintenanceById(id);
        setStatus(maintenance, "Cancelled");
    }

    // get status by id
    @Override
    public PreventiveMaintenanceStatus getStatusById(Integer id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance Status", "id", id.toString()));
    }

    // set status
    private void setStatus(PreventiveMaintenance maintenance, String statusName) {
        PreventiveMaintenanceStatus status = statusRepository.findStatusByName(statusName);
        if (status == null) {
            throw new ResourceNotFoundException("Maintenance status", "name", statusName);
        }
        preventiveMaintenanceRepository.save(maintenance);
    }

    // helper method to get maintenance by id
    private PreventiveMaintenance getMaintenanceById(Integer id) {
        return preventiveMaintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record", "id", id.toString()));
    }

}
