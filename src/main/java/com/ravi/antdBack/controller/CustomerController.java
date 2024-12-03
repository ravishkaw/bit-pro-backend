package com.ravi.antdBack.controller;

import com.ravi.antdBack.payload.CustomerDTO;
import com.ravi.antdBack.payload.CustomerResponse;
import com.ravi.antdBack.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CustomerController {

    private final CustomerService userService;

    public CustomerController(CustomerService userService){
        this.userService = userService;
    }


    @GetMapping("/")
    public String greeting() {
        return "Hello User";
    }

    @GetMapping("/customers")
    public ResponseEntity<CustomerResponse> getAllCustomers(){
        CustomerResponse response = userService.getAllCustomers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/customer")
    public ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerDTO customerDTO){
        CustomerDTO savedCustomerDTO = userService.addCustomer(customerDTO);
        return new ResponseEntity<>(savedCustomerDTO, HttpStatus.CREATED);
    }

    @PutMapping("/customer/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO){
        CustomerDTO updatedCustomer = userService.updateCustomer(customerId, customerDTO);
        return new ResponseEntity<>(updatedCustomer,HttpStatus.OK);
    }

    @DeleteMapping("/customer/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId){
        userService.deleteCustomer(customerId);
        return new ResponseEntity<>("Customer Deleted",HttpStatus.OK);
    }
}
