package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.PreventiveMaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// repository layer of preventive maintenance status
@Repository
public interface PreventiveMaintenanceStatusRepository extends JpaRepository<PreventiveMaintenanceStatus, Integer> {

    // find status by name
    @Query("SELECT s FROM PreventiveMaintenanceStatus s WHERE s.name=?1")
    PreventiveMaintenanceStatus findStatusByName(String name);
}
