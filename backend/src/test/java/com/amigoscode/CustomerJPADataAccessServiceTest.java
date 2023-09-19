package com.amigoscode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    void selectAllCustomer() {
        Page<Customer> page = mock(Page.class);
        List<Customer> customers = List.of(new Customer());
        when(page.getContent()).thenReturn(customers);
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(page);
        // When
        List<Customer> expected = underTest.SelectAllCustomers();

        // Then
        assertThat(expected).isEqualTo(customers);
        ArgumentCaptor<Pageable> pageArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(customerRepository).findAll(pageArgumentCaptor.capture());
        assertThat(pageArgumentCaptor.getValue()).isEqualTo(Pageable.ofSize(50));
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

	@Test
	void canUploadCustomerPhoto()
	{
		String imageID="2222";
		Integer customerId=1;

		underTest.uploadCustomerImageID(imageID, customerId);

		verify(customerRepository).updateImageId(imageID, customerId);
	}
}

