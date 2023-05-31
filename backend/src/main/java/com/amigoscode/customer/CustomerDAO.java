package com.amigoscode.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO
{
	List<Customer> SelectAllCustomers();
	Optional<Customer> selectCustomerById(Integer ID);
	void insertCustomer(Customer C);
	boolean existsCustomerWithEmail(String Email);
	boolean existsCustomerWithId(Integer Id);
	void deleteCustomerById(Integer id);
	void updateCustomer(Customer c);

}
