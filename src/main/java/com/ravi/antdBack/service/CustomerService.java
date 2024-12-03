package com.ravi.antdBack.service;

import com.ravi.antdBack.payload.CustomerDTO;
import com.ravi.antdBack.payload.CustomerResponse;

public interface CustomerService {
    CustomerResponse getAllCustomers();

    CustomerDTO addCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);
}
