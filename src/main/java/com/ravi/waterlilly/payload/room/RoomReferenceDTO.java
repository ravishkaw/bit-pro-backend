package com.ravi.waterlilly.payload.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomReferenceDTO {
    private Long id;
    private String number;
}
