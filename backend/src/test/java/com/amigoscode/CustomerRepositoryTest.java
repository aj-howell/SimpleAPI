package com.amigoscode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerJDBCDataAccessService;
import com.amigoscode.customer.CustomerRepository;
import com.amigoscode.customer.CustomerRowMapper;

@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainersUnitTest
{
	@Autowired
	private CustomerRepository underTest;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@BeforeEach
	void setUp() throws Exception
	{
		//System.out.println(applicationContext.getBeanDefinitionCount());
	}


	@Test
	void existsCustomerByEmail()
	{
		String email =faker.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer = new Customer(
				20,
				faker.name().fullName(),
				email,
				faker.demographic().sex()
						);
		underTest.save(customer);
		
	Customer test = underTest
		.findAll()
		.stream()
		.filter(c->c.getEmail().matches(email))
		.findFirst()
		.orElseThrow(); //this gives an expection rather than being null itself
	
		boolean result = underTest.existsCustomerByEmail(test.getEmail());
		
		assertThat(result).isTrue();
	
	}
	
	
	@Test
	void existsCustomerByEmailFalse()
	{
		String email =faker.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer = new Customer(
				20,
				faker.name().fullName(),
				email,
				faker.demographic().sex()
						);
		underTest.save(customer);
		
	
	
		boolean result = underTest.existsCustomerByEmail("aj@gmail.com");
		
		assertThat(result).isFalse();
	
	}
	
	
	@Test
	void existsCustomerById()
	{
		String email =faker.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer = new Customer(
				20,
				faker.name().fullName(),
				email,
				faker.demographic().sex()
						);
		
		underTest.save(customer);
		
		Integer id = underTest
				.findAll()
				.stream()
				.filter(c->c.getEmail().matches(email))
				.map(c->c.getId())
				.findFirst()
				.orElseThrow(); //this gives an expection rather than being null itself
			
		boolean test=underTest.existsById(id);
		
		assertThat(test).isTrue();	
	}
	
	@Test
	void existsCustomerByIdFalse()
	{
		boolean result = underTest.existsCustomerById(0);
		
		assertThat(result).isFalse();
	
	}

//	@Test
//	void test() {
//		fail("Not yet implemented");
//	}
}
