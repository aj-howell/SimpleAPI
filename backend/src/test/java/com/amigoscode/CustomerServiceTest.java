package com.amigoscode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerDAO;
import com.amigoscode.customer.CustomerDTO;
import com.amigoscode.customer.CustomerRegistrationRequest;
import com.amigoscode.customer.CustomerService;
import com.amigoscode.customer.CustomerUpdateRequest;
import com.amigoscode.exceptions.BadDataRequest;
import com.amigoscode.exceptions.DuplicateResourceException;
import com.amigoscode.exceptions.ResourceNotFound;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@Mock
	private CustomerDAO customerDAO; //mock on the underlying class/interface completing the action
	private CustomerService underTest;
	@Mock
	private PasswordEncoder passwordEncoder;
	
	
	@BeforeEach
	void setUp() throws Exception
	{
		underTest = new CustomerService(customerDAO, passwordEncoder, null, null)
				;
	}
	
	@Test
	void testGetAllCustomers()
	{
		underTest.getAllCustomers();
		verify(customerDAO).SelectAllCustomers();
	}

	@Test
	void testGetCustomer()
	{
		int id=20;
		Customer c = new Customer(id, 20, "Kevin", "Kevin@gmail.com","password", "M");
		Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(c));
		CustomerDTO expected = new CustomerDTO(c);
		
		CustomerDTO actual = underTest.getCustomer(id);
		
		
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	void testGetCustomerNot()
	{
		int id=20;
		Customer c = new Customer(id, 20, "Kevin", "Kevin@gmail.com", "password", "M");
		Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());
		
		
		assertThatThrownBy(()-> underTest.getCustomer(id))
		.isInstanceOf(ResourceNotFound.class)
		.hasMessage("customer with id [%s] not found".formatted(id));
	}


	@Test
	void testAddCustomer()
	{
		int id=20;
		CustomerRegistrationRequest c = new CustomerRegistrationRequest(20, "Kevin", "Kevin@gmail.com","password","M");
		
		Mockito.when(customerDAO.existsCustomerWithEmail(c.email())).thenReturn(false);
		
		String passwordHash ="wer443566777kkkll;mnfht";
		Mockito.when(passwordEncoder.encode(c.password())).thenReturn(passwordHash);
		underTest.addCustomer(c);
		
		ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class); 
		//captures the customer that would have been inserted

		verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());
		
		Customer capturedCustomer = customerArgumentCaptor.getValue();
		
		assertThat(capturedCustomer.getId()).isNull();
		assertThat(capturedCustomer.getAge()).isEqualTo(c.age());
		assertThat(capturedCustomer.getEmail()).isEqualTo(c.email());
		assertThat(capturedCustomer.getName()).isEqualTo(c.name());
		assertThat(capturedCustomer.getGender()).isEqualTo(c.gender());
		assertThat(capturedCustomer.getPassword()).isEqualTo(passwordHash);
	}

	@Test
	void testAddCustomerNot()
	{
		int id=20;
		CustomerRegistrationRequest c = new CustomerRegistrationRequest(20, "Kevin", "Kevin@gmail.com","password","M");
		
		Mockito.when(customerDAO.existsCustomerWithEmail(c.email())).thenReturn(true);

		
		
		assertThatThrownBy(()-> underTest.addCustomer(c))
		.isInstanceOf(DuplicateResourceException.class)
		.hasMessage("Email Already Exist");
		
		verify(customerDAO, never()).insertCustomer(any());
	}

	
	@Test
	void testDeleteCustomerById()
	{
		int id = 9;
		Mockito.when(customerDAO.existsCustomerWithId(id)).thenReturn(true);
		
		underTest.deleteCustomerById(id);
		verify(customerDAO).deleteCustomerById(id);
	}
	
	@Test
	void testDeleteCustomerByIdNot()
	{
		int id = 9;
		Mockito.when(customerDAO.existsCustomerWithId(id)).thenReturn(false);
		
		assertThatThrownBy(()-> underTest.deleteCustomerById(id))
		.isInstanceOf(ResourceNotFound.class)
		.hasMessage("customer with id [%s] not found".formatted(id));
		
		verify(customerDAO, never()).deleteCustomerById(id);
	}

	 @Test
	    void canUpdateAllCustomersProperties() {
			int id=20;
			Customer customer = new Customer(id, 20, "Kevin", "Kevin@gmail.com", "password", "M");
		 	
	        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

	        String newEmail = "alexandro@amigoscode.com";

	        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
	                23,"Alexandro", newEmail, "M");

	        Mockito.when(customerDAO.existsCustomerWithEmail(newEmail)).thenReturn(false);

	        // When
	        underTest.updateCustomer(id, updateRequest);

	        // Then
	        ArgumentCaptor<Customer> customerArgumentCaptor =
	                ArgumentCaptor.forClass(Customer.class);

	        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
	        Customer capturedCustomer = customerArgumentCaptor.getValue();

	        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
	        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
	        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
	    }

	    @Test
	    void canUpdateOnlyCustomerName() {
			int id=20;
			Customer customer = new Customer(id, 20, "Kevin", "Kevin@gmail.com","password", "M");
	
	        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

	        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
	                null,"Alexandro", null,"M");

	        underTest.updateCustomer(id, updateRequest);

	 
	        ArgumentCaptor<Customer> customerArgumentCaptor =
	                ArgumentCaptor.forClass(Customer.class);

	        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
	        Customer capturedCustomer = customerArgumentCaptor.getValue();

	        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
	        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
	        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
	    }

	    @Test
	    void canUpdateOnlyCustomerEmail() {
			int id=20;
			Customer customer = new Customer(id, 20, "Kevin", "Kevin@gmail.com","password", "M");
	
	        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

	        String newEmail = "alexandro@amigoscode.com";

	        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
	                null, null,newEmail,"M");

	        Mockito.when(customerDAO.existsCustomerWithEmail(newEmail)).thenReturn(false);

	        // When
	        underTest.updateCustomer(id, updateRequest);

	        // Then
	        ArgumentCaptor<Customer> customerArgumentCaptor =
	                ArgumentCaptor.forClass(Customer.class);

	        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
	        Customer capturedCustomer = customerArgumentCaptor.getValue();

	        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
	        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
	        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
	    }

	    @Test
	    void canUpdateOnlyCustomerAge() {
			int id=20;
			Customer customer = new Customer(id, 20, "Kevin", "Kevin@gmail.com","password", "M");
	    	
	        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

	        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
	        		22,null, null,"M");

	        underTest.updateCustomer(id, updateRequest);

	        ArgumentCaptor<Customer> customerArgumentCaptor =
	                ArgumentCaptor.forClass(Customer.class);

	        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
	        Customer capturedCustomer = customerArgumentCaptor.getValue();

	        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
	        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
	        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
	    }

	    @Test
	    void willThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
			int id=20;
			Customer customer = new Customer(id, 20, "Kevin", "Kevin@gmail.com","password", "M");
	 
	        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

	        String newEmail = "alexandro@amigoscode.com";

	        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
	                null, null,newEmail,"M");

	        Mockito.when(customerDAO.existsCustomerWithEmail(newEmail)).thenReturn(true);

	        
	        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
	                .isInstanceOf(DuplicateResourceException.class)
	                .hasMessage("email already taken");

	        
	        verify(customerDAO, never()).updateCustomer(any());
	    }

	    @Test
	    void willThrowWhenCustomerUpdateHasNoChanges() {
			int id=20;
			Customer customer = new Customer(id, 20, "Kevin", "Kevin@gmail.com","password", "M");
	 
	        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

	        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
	        		customer.getAge(),customer.getName(), customer.getEmail(),customer.getGender());

	        
	        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
	                .isInstanceOf(BadDataRequest.class)
	                .hasMessage("no data changes found");

	       
	        verify(customerDAO, never()).updateCustomer(any());
	    }
	
	

}
