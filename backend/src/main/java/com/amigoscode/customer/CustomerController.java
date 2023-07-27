package com.amigoscode.customer;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/customers")

public class CustomerController 
/* a class that uses customerService which contains methods that will return queries 
 * that are then turned into JSON objects when mapped to the Web page */
{
	private final CustomerService customerService;
	
	
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping //get is less specified(still need path string) & Request More direct path
	public List<Customer> getAllCustomers()
	{
		return customerService.getAllCustomers();
	}
	
	@GetMapping("{customerId}") //Path Variable = may change -> CustomerID
	public Customer getCustomer(@PathVariable("customerId") Integer Id)
	{
		return customerService.getCustomer(Id);
	}
	
	@PostMapping
	public void registerCustomer(@RequestBody CustomerRegistrationRequest c)
	{
		customerService.addCustomer(c);
	}
	
	@DeleteMapping("{customerId}")
	public void deleteCustomer(
	        @PathVariable("customerId") Integer customerId) {
	    customerService.deleteCustomerById(customerId);
	}
	
	@PutMapping("{customerId}")
	public void updateCustomer(
	        @PathVariable("customerId") Integer customerId,
	        @RequestBody CustomerUpdateRequest updateRequest) {
	    customerService.updateCustomer(customerId, updateRequest);
	}
}
