package com.example.easynotes.controller;

import java.text.NumberFormat;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
@ComponentScan(basePackages = { "com.example.easynotes.controller" })
public class CustomerController {

	@Autowired
	CustomerRepository customerRepository;

	// Get All Notes
	@GetMapping("/customers")
	public List<Customer> getAllCustomers() {
		Runtime runtime = Runtime.getRuntime();

		NumberFormat format = NumberFormat.getInstance();

//		StringBuilder sb = new StringBuilder();
//		long maxMemory = runtime.maxMemory();
//		long allocatedMemory = runtime.totalMemory();
//		long freeMemory = runtime.freeMemory();
//
//		sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
//		System.out.println("free memory: " + format.format(freeMemory / 1024) + "<br/>");
//		sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
//		System.out.println("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
//		sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
//		System.out.println("max memory: " + format.format(maxMemory / 1024) + "<br/>");
//		sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");
//		System.out.println("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");
		
		long maxBytes = Runtime.getRuntime().maxMemory();
		System.out.println("Max memory: " + maxBytes / 1024 / 1024 + "M");
		return customerRepository.findAll();
	}

	// Create a new Note
	@PostMapping("/customer")
	public Customer createCustomers(@Valid @RequestBody Customer customer) {
		return customerRepository.save(customer);
	}

	// Get a Single Note
	@GetMapping("/customer/{id}")
	public Customer getCustomersById(@PathVariable(value = "id") Long id) {
		return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("customers", "id", id));
	}

	// Update a Note
	@PutMapping("customer")
	public Customer updateCustomers(@Valid @RequestBody Customer customerDetails) {
		Customer customer = customerRepository.findById(customerDetails.getId())
				.orElseThrow(() -> new ResourceNotFoundException("customer", "id", customerDetails.getId()));
		customer.setFirstName(customerDetails.getFirstName());
		customer.setLastName(customerDetails.getLastName());
		customer.setPhone(customerDetails.getPhone());
		customer.setEmail(customerDetails.getEmail());
		customer.setAddress(customerDetails.getAddress());

		Customer updatedCustomer = customerRepository.save(customer);
		return updatedCustomer;
	}

	// Delete a Note
	@DeleteMapping("/customer/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long customerId) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));

		customerRepository.delete(customer);

		return ResponseEntity.ok().build();
	}
}