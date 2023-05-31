package com.amigoscode.journey;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRegistrationRequest;
import com.amigoscode.customer.CustomerUpdateRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest
{
	@Autowired
	private WebTestClient webClient;
	
	public final static Random random = new Random();
	
	@Test
	void canRegisterCustomer()
	{
		// create registartion request
		Faker faker = new Faker();
		Name fakerName = faker.name();
		String name= fakerName.fullName();
		String email = fakerName.lastName()+"-"+UUID.randomUUID()+"@gmail.com";
		int age = random.nextInt(1,100);
		CustomerRegistrationRequest request = new CustomerRegistrationRequest(age,name,email);
		
		//Send post Request
		webClient.post()
		.uri("api/v1/customers")
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(request),CustomerRegistrationRequest.class)
		.exchange()
		.expectStatus().isOk();
		
		//Get everyone from API
		List<Customer> allCustomers = webClient.get()
		.uri("api/v1/customers")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus()
		.isOk()
		.expectBodyList(new ParameterizedTypeReference<Customer>(){})
		.returnResult()
		.getResponseBody();
		
		//make sure Customer is preset
		Customer expected = new Customer(age,name,email);
		
		assertThat(allCustomers)
		.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
		.contains(expected);
		
		int id = allCustomers.stream()
				.filter(c->c.getEmail().matches(email))
				.map(c->c.getId())
				.findFirst()
				.orElseThrow();
		
		expected.setId(id);
		
		//get Customer by Id
		webClient.get()
		.uri("api/v1/customers/{id}",id)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus()
		.isOk()
		.expectBody(new ParameterizedTypeReference<Customer>(){})
		.isEqualTo(expected);
		
	}
	
	@Test
	void canDelete()
	{
		// create registartion request
		Faker faker = new Faker();
		Name fakerName = faker.name();
		String name= fakerName.fullName();
		String email = fakerName.lastName()+"-"+UUID.randomUUID()+"@gmail.com";
		int age = random.nextInt(1,100);
		CustomerRegistrationRequest request = new CustomerRegistrationRequest(age,name,email);
		
		//Send post Request
		webClient.post()
		.uri("api/v1/customers")
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(request),CustomerRegistrationRequest.class)
		.exchange()
		.expectStatus().isOk();
		
		//Get everyone from API
		List<Customer> allCustomers = webClient.get()
		.uri("api/v1/customers")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus()
		.isOk()
		.expectBodyList(new ParameterizedTypeReference<Customer>(){})
		.returnResult()
		.getResponseBody();
		
		
		int id = allCustomers.stream()
				.filter(c->c.getEmail().matches(email))
				.map(c->c.getId())
				.findFirst()
				.orElseThrow();
		
		webClient
		.delete()
		.uri("api/v1/customers/{id}",id)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus()
		.isOk();
		//get Customer by Id
		
		webClient.get()
		.uri("api/v1/customers/{id}",id)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus()
		.isNotFound();
	}
	
	@Test
	void canUpdate()
	{
		 // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@amigoscode.com";
        int age = random.nextInt(1,100);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                age,name, email
        );

        // send a post request
        webClient.post()
                .uri("api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webClient.get()
                .uri("api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        int id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(c->c.getId())
                .findFirst()
                .orElseThrow();

        // update customer

        String newName = "Ali";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
        		null,newName,null
        );

        webClient.put()
                .uri("api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        Customer updatedCustomer = webClient.get()
                .uri("api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer expected = new Customer(
                id, age,newName, email
        );
        
        assertThat(updatedCustomer).isEqualTo(expected);
	}
	
}
