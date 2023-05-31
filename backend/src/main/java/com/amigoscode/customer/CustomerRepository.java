package com.amigoscode.customer;

import org.springframework.data.jpa.repository.JpaRepository;


//Already Has Repository Annotation by default
public interface CustomerRepository extends JpaRepository<Customer,Integer> 
//this is what lets us use CRUD operations within controller 
//sum are within JPA but others will be created with the Name of an Object(table)
{
	boolean existsCustomerByEmail(String email);
	void deleteCustomerById(Integer customerId);
	boolean existsCustomerById(Integer Id);
}
