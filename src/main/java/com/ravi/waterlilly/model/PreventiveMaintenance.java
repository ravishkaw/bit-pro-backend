package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// preventive maintenance entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "preventive_maintenance")
public class PreventiveMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "target_type_id")
    private TaskTargetType targetType;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "maintenance_type")
    private String maintenanceType;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @ManyToOne
    @JoinColumn(name = "maintenance_status_id")
    private PreventiveMaintenanceStatus maintenanceStatus;
}
