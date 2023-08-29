package com.amigoscode.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository("list") //Database
public class CustomerListDataAccessService implements CustomerDAO
// this class is used to create clients and return actual data 

{
	private static List <Customer> customers; // just belongs to class itself 
	static // sets up our database
	{
		customers = new ArrayList<>();
		Customer Alex = new Customer(1, 21, "Alex", "Alex@gmail.com","password", "M");
		Customer Josh = new Customer(2, 25, "Josh", "Josh@gmail.com","password", "F");
		customers.add(Alex);
		customers.add(Josh);
	}
	@Override
	public List<Customer> SelectAllCustomers() {
		return customers;
	}
	
	@Override
	public Optional<Customer> selectCustomerById(Integer ID) {
		
		return customers
				.stream()
				.filter(c -> c.getId().equals(ID)) // specifies if a customer has that id
				.findFirst(); // get the first customer with that id *should be unique though*
			
	}

	@Override
	public void insertCustomer(Customer C) {
		customers.add(C);
		return;
	}

	@Override
	public boolean existsCustomerWithEmail(String Email)
	{
		return customers.stream().anyMatch( c -> c.getEmail().equals(Email));
		 
	}
	
	@Override
	public boolean existsCustomerWithId(Integer Id)
	{
		return customers.stream().anyMatch( c -> c.getId().equals(Id));
	}
	
	@Override
	public void deleteCustomerById(Integer id)
	{
		customers.remove(customers.get(id));
		
	}

	@Override
	public void updateCustomer(Customer C)
	{
		customers.add(C);
	}

	@Override
	public Optional<Customer> selectCustomerByEmail(String email) {
		return customers
				.stream()
				.filter(c -> c.getUsername().equals(email)) 
				.findFirst(); 
			
	}



}
