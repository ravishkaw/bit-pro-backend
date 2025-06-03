package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.*;
import com.ravi.waterlilly.payload.preventiveMaintenance.PreventiveMaintenancePageResponse;
import com.ravi.waterlilly.payload.preventiveMaintenance.PreventiveMaintenancePayloadDTO;
import com.ravi.waterlilly.payload.preventiveMaintenance.PreventiveMaintenanceTableDataDTO;
import com.ravi.waterlilly.repository.PreventiveMaintenanceRepository;
import com.ravi.waterlilly.service.*;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

// implementation of preventive maintenance service
@Service
@RequiredArgsConstructor
public class PreventiveMaintenanceServiceImpl implements PreventiveMaintenanceService {
    private final PrivilegeUtils privilegeUtils;
    private final PreventiveMaintenanceRepository preventiveMaintenanceRepository;
    private final ModelMapper modelMapper;
    private final PreventiveMaintenanceStatusService preventiveMaintenanceStatusService;
    private final RoomStatusService roomStatusService;
    private final EventVenueStatusService eventVenueStatusService;
    private final RoomService roomService;
    private final EventVenueService eventVenueService;
    private final TaskTargetTypeService taskTargetTypeService;

    // get all maintenances
    @Override
    public PreventiveMaintenancePageResponse getMaintenances(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {

        // check privileges
        privilegeUtils.privilegeCheck("Task", AppConstants.SELECT);

        // sort order and get paginated data
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<PreventiveMaintenance> page = preventiveMaintenanceRepository.findAll(pageable);

        List<PreventiveMaintenanceTableDataDTO> list = page.stream()
                .map(maintenance -> {
                    PreventiveMaintenanceTableDataDTO tableData = modelMapper.map(maintenance, PreventiveMaintenanceTableDataDTO.class);
                    if (maintenance.getTargetType().getName().equalsIgnoreCase("Room")) {
                        tableData.setTargetName(roomService.getRoomById(maintenance.getTargetId()).getNumber().toString());
                    } else if (maintenance.getTargetType().getName().equalsIgnoreCase("Event Venue")) {
                        tableData.setTargetName(eventVenueService.getVenueById(maintenance.getTargetId()).getName());
                    } else {
                        tableData.setTargetName("Unknown");
                    }
                    return tableData;
                })
                .toList();

        return new PreventiveMaintenancePageResponse(
                list,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    // get single maintenance
    @Override
    public PreventiveMaintenancePayloadDTO getSingleMaintenance(Integer id) {

        // check privileges
        privilegeUtils.privilegeCheck("Task", AppConstants.SELECT);

        PreventiveMaintenance maintenance = getMaintenanceById(id);

        return modelMapper.map(maintenance, PreventiveMaintenancePayloadDTO.class);
    }

    // add new record
    @Transactional
    @Override
    public void addNewMaintenance(PreventiveMaintenancePayloadDTO maintenancePayloadDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Task", AppConstants.INSERT);

        // validate if any maintenance is there for the room
        validateMaintenanceUniqueness(maintenancePayloadDTO, null);

        PreventiveMaintenance maintenance = new PreventiveMaintenance();
        updateMaintenanceFields(maintenancePayloadDTO, maintenance);
        preventiveMaintenanceRepository.save(maintenance);
    }

    // update a record
    @Transactional
    @Override
    public void updateMaintenance(PreventiveMaintenancePayloadDTO maintenancePayloadDTO, Integer id) {

        // check privileges
        privilegeUtils.privilegeCheck("Task", AppConstants.UPDATE);

        // get existing record
        PreventiveMaintenance maintenance = getMaintenanceById(id);

        // validate if any maintenance is there for the room
        validateMaintenanceUniqueness(maintenancePayloadDTO, id);

        updateMaintenanceFields(maintenancePayloadDTO, maintenance);
        preventiveMaintenanceRepository.save(maintenance);
    }

    // delete a record ( soft delete- Cancelled status )
    @Override
    public void deleteMaintenance(Integer id) {

        // check privileges
        privilegeUtils.privilegeCheck("Task", AppConstants.DELETE);
        preventiveMaintenanceStatusService.setCancelledStatus(id);
    }

    // get maintenance by id
    @Override
    public PreventiveMaintenance getMaintenanceById(Integer id) {
        return preventiveMaintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record", "id", id.toString()));
    }

    // Helper method to check if any maintenance it there
    private void validateMaintenanceUniqueness(PreventiveMaintenancePayloadDTO payloadDTO, Integer id) {

        // check if any maintenance is there for the room
        boolean maintenanceExists;
        if (id == null) {
            maintenanceExists = preventiveMaintenanceRepository.findPreventiveMaintenance(payloadDTO.getTargetTypeId(), payloadDTO.getTargetId());
        } else {
            maintenanceExists = preventiveMaintenanceRepository.findPreventiveMaintenanceExcludingId(payloadDTO.getTargetTypeId(), payloadDTO.getTargetId(), id);
        }

        if (maintenanceExists) {
            throw new APIException("The room as a upcoming maintenance or on hold maintenance.");
        }
    }

    // Helper method to update maintenance fields
    private void updateMaintenanceFields(PreventiveMaintenancePayloadDTO dto, PreventiveMaintenance maintenance) {
        maintenance.setTargetType(taskTargetTypeService.getTaskTargetTypeById(dto.getTargetTypeId()));
        maintenance.setTargetId(dto.getTargetId());
        maintenance.setMaintenanceType(dto.getMaintenanceType());
        maintenance.setScheduledDate(dto.getScheduledDate());
        maintenance.setCompletedDate(dto.getCompletedDate());
        maintenance.setMaintenanceStatus(preventiveMaintenanceStatusService.getStatusById(dto.getMaintenanceStatusId()));

        updateTargetTypeStatus(maintenance);
    }

    // Helper method to update target type status
    private void updateTargetTypeStatus(PreventiveMaintenance maintenance) {
        String maintenanceStatusName = maintenance.getMaintenanceStatus().getName();

        if ("Room".equalsIgnoreCase(maintenance.getTargetType().getName())) {
            roomStatusService.updateStatusBasedOnMaintenance(maintenance.getTargetId(), maintenanceStatusName);
        } else if ("Event Venue".equalsIgnoreCase(maintenance.getTargetType().getName())) {
            eventVenueStatusService.updateStatusBasedOnMaintenance(maintenance.getTargetId(), maintenanceStatusName);
        }
    }
}

