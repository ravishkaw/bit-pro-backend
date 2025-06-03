package com.ravi.waterlilly.payload.room;

import com.ravi.waterlilly.payload.roomType.RoomTypePayloadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// dto to get to show cards
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomCardDTO {
    private Long id;
    private Integer number;
    private Integer adultNo;
    private Integer childNo;
    private Integer infantNo;
    private Integer floorNumber;
    private String photo;
    private BigDecimal price;
    private String description;
    private String statusName;
    private RoomTypePayloadDTO roomType;
}
