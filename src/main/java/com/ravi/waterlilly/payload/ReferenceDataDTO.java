package com.ravi.waterlilly.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// simple dto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceDataDTO {
    private Integer id;
    private String name;
}
