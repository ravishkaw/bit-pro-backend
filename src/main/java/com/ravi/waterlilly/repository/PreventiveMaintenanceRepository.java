package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.PreventiveMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// repository layer of preventive maintenance
@Repository
public interface PreventiveMaintenanceRepository extends JpaRepository<PreventiveMaintenance, Integer> {

    // find if there any existing maintenance
    @Query("SELECT COUNT (pm) > 0 FROM PreventiveMaintenance pm WHERE " +
            "pm.targetType.id = ?1 AND pm.targetId = ?2 AND " +
            "pm.maintenanceStatus.name NOT IN ('Cancelled', 'Completed')")
    boolean findPreventiveMaintenance(Integer targetTpeId, Long targetId);

    // find if there any existing maintenance excluding id
    @Query("SELECT COUNT (pm) > 0 FROM PreventiveMaintenance pm WHERE " +
            "pm.targetType.id = ?1 AND pm.targetId = ?2 AND pm.id != ?3 AND " +
            "pm.maintenanceStatus.name NOT IN ('Cancelled', 'Completed')")
    boolean findPreventiveMaintenanceExcludingId(Integer targetTypeId, Long targetId, Integer maintenanceId);
}
