package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

// event package entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_package")
public class EventPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @OneToMany(mappedBy = "eventPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventPackageHasService> eventPackageHasServices;

    public void addService(EventService service, Integer quantity) {
        EventPackageHasServiceId serviceId = new EventPackageHasServiceId();
        EventPackageHasService packageService = new EventPackageHasService();
        serviceId.setEventPackageId(this.id);
        serviceId.setServiceId(service.getId());

        packageService.setId(serviceId);
        packageService.setEventPackage(this);
        packageService.setEventService(service);
        packageService.setQuantity(quantity);

        this.eventPackageHasServices.add(packageService);
    }

    public void clearServices() {
        this.eventPackageHasServices.clear();
    }
}
