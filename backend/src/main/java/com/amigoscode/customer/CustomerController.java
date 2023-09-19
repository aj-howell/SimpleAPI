package com.amigoscode.customer;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.amigoscode.JWT.JWTUtil;

@RestController
@RequestMapping("api/v1/customers")

public class CustomerController 
/* a class that uses customerService which contains methods that will return queries 
 * that are then turned into JSON objects when mapped to the Web page */
{
	private final CustomerService customerService;
	private final JWTUtil jwtUtil;
	
	public CustomerController(CustomerService customerService,JWTUtil jwtUtil) {
		this.customerService = customerService;
		this.jwtUtil = jwtUtil;
		
	}

	@GetMapping //get is less specified(still need path string) & Request More direct path
	public List<CustomerDTO> getAllCustomers()
	{
		return customerService.getAllCustomers();
	}

	@GetMapping("{customerId}") //Path Variable = may change -> CustomerID
	public CustomerDTO getCustomer(@PathVariable("customerId") Integer Id)
	{
		return customerService.getCustomer(Id);
	}
	
	@PostMapping
	public ResponseEntity<?> registerCustomer(@RequestBody CustomerRegistrationRequest c) 
	{
		customerService.addCustomer(c);
		String jwtToken = jwtUtil.issueToken(c.email(), "ROLE_USER");
		return ResponseEntity.ok()
		.header(HttpHeaders.AUTHORIZATION, jwtToken) // we aren't returning anything to the body when we are registering 
		.build();
	}
	
	@DeleteMapping("{customerId}")
	public void deleteCustomer(
            @PathVariable Integer customerId) {
	    customerService.deleteCustomerById(customerId);
	}
	
	@PutMapping("{customerId}")
	public void updateCustomer(
            @PathVariable Integer customerId,
	        @RequestBody CustomerUpdateRequest updateRequest) {
	    customerService.updateCustomer(customerId, updateRequest);
	}

	@PostMapping( value="{customerId}/profile-image",
	consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
    public void uploadPhoto(@PathVariable Integer customerId, @RequestParam("file") MultipartFile file)
    {
		customerService.uploadCustomerPhoto(customerId,file);
    }

	
	@GetMapping("{customerId}/profile-image")
    public byte[] downloadPhoto(@PathVariable Integer customerId)
    {
		return customerService.downloadCustomerPhoto(customerId);
    }
}
