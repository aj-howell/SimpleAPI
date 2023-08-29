package com.amigoscode.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository("jpa") // Queries to DB
public class CustomerJPADataAccessService implements CustomerDAO 
//this takes queries and spits out/takes in data
//methods are implemented from the DAO which are representing calls that make queries 
{
	private final CustomerRepository customerRepository;
	
	public CustomerJPADataAccessService(CustomerRepository customerRepository) {
		super();
		this.customerRepository = customerRepository;
	}	
	
	//Methods that interact with the database 
	@Override
	public List<Customer> SelectAllCustomers() {
		// TODO Auto-generated method stub
		return customerRepository
				.findAll()
				.stream()
				.sorted((c1, c2)->c1.getId().compareTo(c2.getId()))
				.toList();//finds every instance from our table 
	}

	@Override
	public Optional<Customer> selectCustomerById(Integer ID) {
		// TODO Auto-generated method stub
		return customerRepository.findById(ID);
	}

	@Override
	public void insertCustomer(Customer C) {
		customerRepository.save(C);
		
	}

	@Override
	public boolean existsCustomerWithEmail(String Email) {
		return customerRepository.existsCustomerByEmail(Email);
	
	}
		
	@Override
	public boolean existsCustomerWithId(Integer Id)
	{
		return customerRepository.existsCustomerById(Id);
	}


	@Override
	public void deleteCustomerById(Integer id)
	{
	
			customerRepository.deleteCustomerById(id);
		
	}

	@Override
	public void updateCustomer(Customer C)
	{
		customerRepository.save(C);
	}

	@Override
	public Optional<Customer> selectCustomerByEmail(String email) {
		// TODO Auto-generated method stub
		return customerRepository.findCustomerByEmail(email);
	}

	


}
