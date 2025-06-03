package com.ravi.waterlilly.payload.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

// dto to get to show cards
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomCardForReservationDTO {
    private Long id;
    private Integer number;
    private Integer adultNo;
    private Integer childNo;
    private Integer infantNo;
    private Integer floorNumber;
    private BigDecimal price;
    private Set<String> facilities;
}
