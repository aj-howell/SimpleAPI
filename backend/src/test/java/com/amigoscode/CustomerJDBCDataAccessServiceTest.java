package com.amigoscode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerJDBCDataAccessService;
import com.amigoscode.customer.CustomerRowMapper;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainersUnitTest
{
	private CustomerJDBCDataAccessService underTest;
	private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();
	
	@BeforeEach
	void setUp() throws Exception
	{
		underTest = new CustomerJDBCDataAccessService(
				getJDBC()
				,customerRowMapper
				);
	}

	@Test
	void SelectAllCustomers()
	{
		Customer customer = new Customer(
				20,
				faker.name().fullName(),
				faker.internet().safeEmailAddress()+"-"+UUID.randomUUID(),
				"password", faker.demographic().sex()
						);
		
		underTest.insertCustomer(customer);
		
		
		List<Customer> Ecustomers=underTest.SelectAllCustomers();
		
		assertThat(Ecustomers).isNotEmpty();
	}
	
	@Test
	void WillReturnEmptyWhenSelectAllCustomers()
	{
		int id = 0; // an id that no one will be assigned too & sequence starts at 0
		var actual = underTest.selectCustomerById(id);
		
		assertThat(actual).isEmpty();
	}
	
	@Test
	void selectCustomerById()
	{
		String email = faker.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer = new Customer(
				20,
				faker.name().fullName(),
				email,
				"password", faker.demographic().sex()
						);
		
		underTest.insertCustomer(customer);
		
		int id=underTest.SelectAllCustomers()
		.stream()
		.filter(c -> c.getEmail().matches(email))
		.map(c -> c.getId())
		.findFirst()
		.orElseThrow();
		//check where the customer exist by email the grab the id
		
		Optional<Customer> testCustomer = underTest.selectCustomerById(id);
		
		
		assertThat(testCustomer).isPresent().hasValueSatisfying(c -> {
			assertThat(c.getId().equals(id));
			assertThat(c.getEmail().equals(email));
			assertThat(c.getGender().equals(customer.getGender()));
			assertThat(c.getName().equals(customer.getName()));
			
		});
		
	}
	
	@Test
	void insertCustomer()
	{
		String email = faker.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer = new Customer(
				20,
				faker.name().fullName(),
				email,
				"password", faker.demographic().sex()
						);
		underTest.insertCustomer(customer);
		
		Optional<Customer> actual=
		underTest.SelectAllCustomers()
		.stream()
		.filter(c->c.getEmail().matches(email))
		.findFirst();
		
		assertThat(actual).isPresent().hasValueSatisfying(c->
		{
			assertThat(c.getEmail().equals(customer.getEmail()));
		});
	
	}
	
	@Test
	void existsCustomerWithEmail()
	{
		String email = faker.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer = new Customer(
				20,
				faker.name().fullName(),
				email,
				"password", faker.demographic().sex()
						);
		underTest.insertCustomer(customer);
		
		String actual=
		underTest.SelectAllCustomers()
		.stream()
		.filter(c->c.getEmail().matches(email))
		.map(c->c.getEmail())
		.findFirst()
		.orElseThrow();
		
		boolean val = underTest.existsCustomerWithEmail(actual);
		
		assertThat(val).isTrue(); // email does exist
		
		
	}
	
	@Test
	void existsPersonWithEmailReturnsFalseWhenDoesNotExists()
	{
		String email = faker.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer = new Customer(
				20,
				faker.name().fullName(),
				email,
				"password", faker.demographic().sex()
				);
		underTest.insertCustomer(customer);
		
		// an email that we've not put into the database yet
		String email2="aj@gmail.com";
		
		boolean val = underTest.existsCustomerWithEmail(email2);
		
		assertThat(val).isFalse(); // email does exist
		
		
	}
	
	@Test
	void existsCustomerWithId()
	{
		String email = faker.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer = new Customer(
				20,
				faker.name().fullName(),
				email,
				"password", faker.demographic().sex()
						);
		underTest.insertCustomer(customer);
		
		int actual=underTest.SelectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().matches(email))
				.map(c -> c.getId())
				.findFirst()
				.orElseThrow();
		
		boolean val = underTest.existsCustomerWithId(actual);
		
		assertThat(val).isTrue(); // Age does exist
	}
	
	@Test
	void existsPersonWithIdWillReturnFalseWhenIdNotPresent()
	{
	
		//sequence starts at 1
		boolean val = underTest.existsCustomerWithId(0);
		
		assertThat(val).isFalse(); // Age does exist
	}
	
	@Test
	void deleteCustomerById()
	{
		String email = faker.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer = new Customer(
				20,
				faker.name().fullName(),
				email,
				"password", faker.demographic().sex()
						);
		underTest.insertCustomer(customer);
		
		int actual=
		underTest.SelectAllCustomers()
		.stream()
		.filter(c->c.getEmail().matches(email))
		.map(c->c.getId())
		.findFirst()
		.orElseThrow();
		
		underTest.deleteCustomerById(actual);
		boolean val = underTest.existsCustomerWithId(actual);
		
		assertThat(val).isFalse(); // Id does exist
	}
	
	 @Test
	    void updateCustomerName() {
	        // Given
	        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
	        Customer customer = new Customer(
	        		20,
	                faker.name().fullName(),
	                email,
	                "password", faker.demographic().sex()
	        );

	        underTest.insertCustomer(customer);

	        int id = underTest.SelectAllCustomers()
	                .stream()
	                .filter(c -> c.getEmail().equals(email))
	                .map(Customer::getId)
	                .findFirst()
	                .orElseThrow();

	        var newName = "foo";

	        // When age is name
	        Customer update = new Customer();
	        update.setId(id);
	        update.setName(newName);

	        underTest.updateCustomer(update);

	        // Then
	        Optional<Customer> actual = underTest.selectCustomerById(id);

	        assertThat(actual).isPresent().hasValueSatisfying(c -> {
	            assertThat(c.getId()).isEqualTo(id);
	            assertThat(c.getName()).isEqualTo(newName); // change
	            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
	            assertThat(c.getGender().equals(customer.getGender()));
	            assertThat(c.getAge()).isEqualTo(customer.getAge());
	        });
	    }


	    @Test
	    void updateCustomerEmail() {
	        // Given
	        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
	        Customer customer = new Customer(
	        		20,
	                faker.name().fullName(),
	                email,
	               "password", faker.demographic().sex()
	        );

	        underTest.insertCustomer(customer);

	        int id = underTest.SelectAllCustomers()
	                .stream()
	                .filter(c -> c.getEmail().equals(email))
	                .map(c->c.getId())
	                .findFirst()
	                .orElseThrow();

	        var newEmail = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();;

	        // When email is changed
	        Customer update = new Customer();
	        update.setId(id);
	        update.setEmail(newEmail);

	        underTest.updateCustomer(update);

	        // Then
	        Optional<Customer> actual = underTest.selectCustomerById(id);

	        assertThat(actual).isPresent().hasValueSatisfying(c -> {
	            assertThat(c.getId()).isEqualTo(id);
	            assertThat(c.getEmail()).isEqualTo(newEmail); // change
	            assertThat(c.getName()).isEqualTo(customer.getName());
	            assertThat(c.getGender()).isEqualTo(customer.getGender());
	            assertThat(c.getAge()).isEqualTo(customer.getAge());
	        });
	    }

	    @Test
	    void updateCustomerAge() {
	        // Given
	        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
	        Customer customer = new Customer(
	        		 20,
	                faker.name().fullName(),
	                email,
	                "password", faker.demographic().sex()
	               
	        );

	        underTest.insertCustomer(customer);

	        int id = underTest.SelectAllCustomers()
	                .stream()
	                .filter(c -> c.getEmail().equals(email))
	                .map(Customer::getId)
	                .findFirst()
	                .orElseThrow();

	        var newAge = 100;

	        // When age is changed
	        Customer update = new Customer();
	        update.setId(id);
	        update.setAge(newAge);

	        underTest.updateCustomer(update);

	        // Then
	        Optional<Customer> actual = underTest.selectCustomerById(id);

	        assertThat(actual).isPresent().hasValueSatisfying(c -> {
	            assertThat(c.getId()).isEqualTo(id);
	            assertThat(c.getAge()).isEqualTo(newAge); // change
	            assertThat(c.getName()).isEqualTo(customer.getName());
	            assertThat(c.getGender().equals(customer.getGender()));
	            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
	        });
	    }

	    @Test
	    void willUpdateAllPropertiesCustomer() {
	        // Given
	        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
	        String gender = faker.demographic().sex();
	        Customer customer = new Customer(
	        		 20,
	                faker.name().fullName(),
	                email,
	                "password", gender
	        );

	        underTest.insertCustomer(customer);

	        int id = underTest.SelectAllCustomers()
	                .stream()
	                .filter(c -> c.getEmail().equals(email))
	                .map(Customer::getId)
	                .findFirst()
	                .orElseThrow();

	        // When update with new name, age and email
	        Customer update = new Customer();
	        update.setId(id);
	        update.setName("foo");
	        update.setEmail(UUID.randomUUID().toString());
	        update.setAge(22);
	        update.setGender(gender);

	        underTest.updateCustomer(update);

	        // Then
	        Optional<Customer> actual = underTest.selectCustomerById(id);

	        assertThat(actual).isPresent().hasValue(update);
	    }

	    @Test
	    void willNotUpdateWhenNothingToUpdate() {
	        // Given
	        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
	        Customer customer = new Customer(
	        		20,
	                faker.name().fullName(),
	                email,
	                "password", faker.demographic().sex()
	        );

	        underTest.insertCustomer(customer);

	        int id = underTest.SelectAllCustomers()
	                .stream()
	                .filter(c -> c.getEmail().equals(email))
	                .map(Customer::getId)
	                .findFirst()
	                .orElseThrow();

	        // When update without no changes
	        Customer update = new Customer();
	        update.setId(id);

	        underTest.updateCustomer(update);

	        // Then
	        Optional<Customer> actual = underTest.selectCustomerById(id);

	        assertThat(actual).isPresent().hasValueSatisfying(c -> {
	            assertThat(c.getId()).isEqualTo(id);
	            assertThat(c.getAge()).isEqualTo(customer.getAge());
	            assertThat(c.getName()).isEqualTo(customer.getName());
	            assertThat(c.getGender().equals(customer.getGender()));
	            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
				assertThat(c.getPassword()).isEqualTo(customer.getPassword());
	        });
	    }
	
	@Test
	void canUploadCustomerPhoto()
	{
		String imageID="2222";
		String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
	        Customer customer = new Customer(
	        		20,
	                faker.name().fullName(),
	                email,
	                "password", faker.demographic().sex()
	        );

		underTest.insertCustomer(customer);

		Integer customerId= underTest.SelectAllCustomers()
		 .stream()
		 .filter(c->c.getEmail().matches(email))
		 .map(c->c.getId())
		 .findFirst()
		 .orElseThrow();
			
		underTest.uploadCustomerImageID(imageID, customerId);

		Optional<Customer> actual=underTest.selectCustomerById(customerId); 

		assertThat(actual).isPresent().hasValueSatisfying(c -> {
	            assertThat(c.getId()).isEqualTo(customerId);
	            assertThat(c.getAge()).isEqualTo(customer.getAge());
	            assertThat(c.getName()).isEqualTo(customer.getName());
	            assertThat(c.getGender().equals(customer.getGender()));
	            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
				assertThat(c.getPassword()).isEqualTo(customer.getPassword());
				assertThat(c.getImage_id()).isEqualTo(imageID);
	        });
		
	}
}
