package com.ravi.antdBack.service;

import com.ravi.antdBack.model.Customer;
import com.ravi.antdBack.payload.CustomerDTO;
import com.ravi.antdBack.payload.CustomerResponse;
import com.ravi.antdBack.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepository repository, ModelMapper modelMapper){
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerResponse getAllCustomers() {
        List<Customer> categories =  repository.findAll();

        List<CustomerDTO> customerDTOS =  categories.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .toList();

        CustomerResponse response = new CustomerResponse();
        response.setData(customerDTOS);
        return response;
    }

    @Override
    public CustomerDTO addCustomer(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        Customer savedCustomer = repository.save(customer);
        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }

    @Override
    public CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO) {
        Customer existingCustomer = repository.findById(customerId)
                .orElseThrow(()->new RuntimeException("Customer Not Found!"));

        existingCustomer.setCustomerFirstName(customerDTO.getCustomerFirstName());
        existingCustomer.setCustomerLastName(customerDTO.getCustomerLastName());

        Customer savedCustomer = repository.save(existingCustomer);

        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer existingCustomer = repository.findById(customerId)
                .orElseThrow(()->new RuntimeException("Customer Not Found!"));

        repository.delete(existingCustomer);
    }
}
