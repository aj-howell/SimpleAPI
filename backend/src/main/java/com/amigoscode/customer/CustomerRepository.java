package com.amigoscode.customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


//Already Has Repository Annotation by default
public interface CustomerRepository extends JpaRepository<Customer,Integer> 
//this is what lets us use CRUD operations within controller 
//sum are within JPA but others will be created with the Name of an Object(table)
{
	boolean existsCustomerByEmail(String email);
	void deleteCustomerById(Integer customerId);
	boolean existsCustomerById(Integer Id);
	Optional<Customer> findCustomerByEmail(String email);
	
	@Modifying
	@Query("UPDATE Customer c SET c.image_id=?1 WHERE c.id =?2")
	int updateImageId(String id, Integer customerID);
}
