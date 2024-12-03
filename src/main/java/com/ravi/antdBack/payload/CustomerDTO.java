package com.ravi.antdBack.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDTO {
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
}
