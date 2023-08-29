package com.amigoscode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerJPADataAccessService;
import com.amigoscode.customer.CustomerRepository;
import com.github.javafaker.Faker;

class CustomerJPADataAccessServiceTest
{
	private CustomerJPADataAccessService underTest;
	
	private AutoCloseable mock;
	
	@Mock
	private CustomerRepository customerRepository;
	
	@BeforeEach
	void setUp() throws Exception
	{
		mock = MockitoAnnotations.openMocks(this);
		underTest = new CustomerJPADataAccessService(customerRepository);
	}

	@Test
	void selectAllCustomer()
	{
		underTest.SelectAllCustomers();
		
		verify(customerRepository).findAll();
	}
	
	@Test
	void selectCustomerById()
	{
		int id=2;
		
		underTest.selectCustomerById(id);
		
		verify(customerRepository).findById(id);
	}
	
	
	
	@AfterEach
	void tearDown() throws Exception
	{
		mock.close();
	}
	

	
	@Test
    void insertCustomer() {
		Faker faker = new Faker();
		
		Customer customer = new Customer(
				20,
				faker.name().fullName(),
				faker.internet().safeEmailAddress()+"-"+UUID.randomUUID(),
				"password", faker.demographic().sex()
						);
		
		underTest.insertCustomer(customer);
		
		verify(customerRepository).save(customer);
    }

    @Test
    void existsCustomerWithEmail()
    {		
    	Faker faker = new Faker();
    	String email=faker.internet().safeEmailAddress()+"-"+UUID.randomUUID();
    	
    	underTest.existsCustomerWithEmail(email);
    	verify(customerRepository).existsCustomerByEmail(email);
    	
    }

    @Test
    void existsCustomerWithId()
    {
    	int id=1;
    	
    	underTest.existsCustomerWithId(id);
    	verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById()
    {
		int id=1;
		
		underTest.deleteCustomerById(id);
		
		verify(customerRepository).deleteCustomerById(id);
    }

    @Test
    void updateCustomer()
    {	
		Faker faker = new Faker();
		
		Customer customer = new Customer(
				20,
				faker.name().fullName(),
				faker.internet().safeEmailAddress()+"-"+UUID.randomUUID(),
				"password", faker.demographic().sex()
						);
    	
        underTest.updateCustomer(customer);
        verify(customerRepository).save(customer);
    }
}

