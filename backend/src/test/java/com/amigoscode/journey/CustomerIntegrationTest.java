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
import com.amigoscode.JWT.AuthenticationRequest;
import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerDTO;
import com.amigoscode.customer.CustomerRegistrationRequest;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders;
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
		String gender = faker.demographic().sex();
		int age = random.nextInt(1,100);
		CustomerRegistrationRequest request = new CustomerRegistrationRequest(age,name,email,"password",gender);
		
		//Send post Request & extracting token
		String jwtToken = webClient.post()
		        .uri("api/v1/customers")
		        .accept(MediaType.APPLICATION_JSON)
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(Mono.just(request), CustomerRegistrationRequest.class)
		        .exchange()
		        .expectStatus()
		        .isOk()
		        .returnResult(Void.class)
		        .getResponseHeaders()
		        .get(HttpHeaders.AUTHORIZATION)
		        .get(0);
		        //.getFirst(HttpHeaders.AUTHORIZATION);	
		//Get everyone from API
		List<CustomerDTO> allCustomers = webClient.get()
		.uri("api/v1/customers")
		.accept(MediaType.APPLICATION_JSON)
		.header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken))
		.exchange()
		.expectStatus()
		.isOk()
		.expectBodyList(new ParameterizedTypeReference<CustomerDTO>(){})
		.returnResult()
		.getResponseBody();
		
		//make sure Customer is preset
		Customer e = new Customer(age,name,email,"password", gender);
		CustomerDTO expected = new CustomerDTO(e);
		
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
		.header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken))
		.exchange()
		.expectStatus()
		.isOk()
		.expectBody(new ParameterizedTypeReference<CustomerDTO>(){})
		.isEqualTo(expected);
		
	}
	
	@Test
	void canDelete()
	{
		// create registration request
		Faker faker = new Faker();
		Name fakerName = faker.name();
		String name= fakerName.fullName();
		String email = fakerName.lastName()+"-"+UUID.randomUUID()+"@gmail.com";
		String gender= faker.demographic().sex();
		
		int age = random.nextInt(1,100);
		CustomerRegistrationRequest request = new CustomerRegistrationRequest(age,name,email,"password",gender);
		
		//Send post Request
		String jwtToken = webClient.post()
		        .uri("api/v1/customers")
		        .accept(MediaType.APPLICATION_JSON)
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(Mono.just(request), CustomerRegistrationRequest.class)
		        .exchange()
		        .expectStatus()
		        .isOk()
		        .returnResult(Void.class)
		        .getResponseHeaders()
		        .get(HttpHeaders.AUTHORIZATION)
		        .get(0);
		
		
		//Get everyone from API
		List<CustomerDTO> allCustomers = 
		webClient.get()
		.uri("api/v1/customers")
		.accept(MediaType.APPLICATION_JSON)
		.header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken))
		.exchange()
		.expectStatus()
		.isOk()
		.expectBodyList(new ParameterizedTypeReference<CustomerDTO>(){})
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
		.header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken))
		.exchange()
		.expectStatus()
		.isOk();
		
		// need to re-validate after a deletion of the token
		String jwtToken2 = webClient.post()
		        .uri("api/v1/customers")
		        .accept(MediaType.APPLICATION_JSON)
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(Mono.just(request), CustomerRegistrationRequest.class)
		        .exchange()
		        .expectStatus()
		        .isOk()
		        .returnResult(Void.class)
		        .getResponseHeaders()
		        .get(HttpHeaders.AUTHORIZATION)
		        .get(0);
		
		//get Customer by Id
		
		webClient
		.get()
		.uri("api/v1/customers/{id}",id)
		.accept(MediaType.APPLICATION_JSON)
		.header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken2))
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
        String gender = faker.demographic().sex();

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                age,name,email,"password",gender
        );

        // send a post request
		String jwtToken = webClient.post()
		        .uri("api/v1/customers")
		        .accept(MediaType.APPLICATION_JSON)
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(Mono.just(request), CustomerRegistrationRequest.class)
		        .exchange()
		        .expectStatus()
		        .isOk()
		        .returnResult(Void.class)
		        .getResponseHeaders()
		        .get(HttpHeaders.AUTHORIZATION)
		        .get(0);
		


        // get all customers
        List<CustomerDTO> allCustomers = webClient.get()
                .uri("api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
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

        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
        		null,newName,null,"password",gender
        );

        webClient.put()
                .uri("api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        CustomerDTO updatedCustomer = webClient.get()
                .uri("api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody();

		Customer C=new Customer(id, age,newName,email,"password", gender);
        CustomerDTO expected = new CustomerDTO(C);
        
        assertThat(updatedCustomer).isEqualTo(expected);
	}
	
	@Test
		void canLogin()
		{
			Faker faker = new Faker();
			Name fakerName = faker.name();
			String name= fakerName.fullName();
			String email = fakerName.lastName()+"-"+UUID.randomUUID()+"@gmail.com";
			String gender= faker.demographic().sex();
			String password = "password";
			
			int age = random.nextInt(1,100);
			CustomerRegistrationRequest customer_request = new CustomerRegistrationRequest(age,name,email,password,gender);
		
			
			String jwtToken=webClient
			.post()
			.uri("api/v1/customers")
			.accept(MediaType.APPLICATION_JSON) //what is the types are acceptable to submit
			.contentType(MediaType.APPLICATION_JSON) //what you are going to submit to
			.body(Mono.just(customer_request), CustomerRegistrationRequest.class)
			.exchange() //perform HTTP requestId
			.expectStatus()
			.isOk()
			.returnResult(Void.class) //return result
			.getResponseHeaders()
			.get(HttpHeaders.AUTHORIZATION) //grab specific header type
			.get(0);
			
			AuthenticationRequest request= new AuthenticationRequest(email, password);
			
			webClient
			.post()
			.uri("api/v1/auth/login")
			.accept(MediaType.APPLICATION_JSON) //what is the types are acceptable to submit
			.contentType(MediaType.APPLICATION_JSON) //what you are going to submit to
			.body(Mono.just(request), AuthenticationRequest.class)
			.exchange() //perform HTTP request
			.expectStatus()
			.isOk();	
		}
	
}
