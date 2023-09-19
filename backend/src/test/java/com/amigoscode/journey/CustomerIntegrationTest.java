package com.amigoscode.journey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.testcontainers.shaded.com.google.common.io.Files;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
<<<<<<< Updated upstream
=======
import org.springframework.web.reactive.function.BodyInserters;
>>>>>>> Stashed changes

import com.amigoscode.JWT.AuthenticationRequest;
import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerDTO;
import com.amigoscode.customer.CustomerRegistrationRequest;
import com.amigoscode.customer.CustomerUpdateRequest;
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
		expected.image_id=null;
		
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
		.isNotFound(); //placeholder for 404 status code 
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
        List<Customer> allCustomers = webClient.get()
                .uri("api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken))
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
        		null,newName,null,gender
        );

        webClient.put()
                .uri("api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        Customer updatedCustomer = webClient.get()
                .uri("api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer expected = new Customer(
                id, age,newName,email,"password", gender
        );
        
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

   @Test
    void canUploadAndDownloadProfilePictures() throws IOException {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        int age = random.nextInt(1,100);

        String gender = faker.demographic().sex();

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(age,name,email,"password",gender);

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
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        CustomerDTO customerDTO = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .findFirst()
                .orElseThrow();

        assertThat(customerDTO.getImage_id()).isNullOrEmpty();

        Resource image = new ClassPathResource(
                "%s.jpeg".formatted(gender.toLowerCase())
        );

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", image);

       webClient.post()
                .uri("api/v1/customers" + "/{customerId}/profile-image", customerDTO.getId())
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();
        // Then the profile image id should be populated

        // get customer by id
        String ImageId = webClient.get()
                .uri("api/v1/customers" + "/{id}", customerDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody()
				.image_id;

        assertThat(ImageId).isNotNull();
		System.out.println("this is image id: "+ImageId);

        // download image for customer
        byte[] downloadedImage = webClient.get()
                .uri("api/v1/customers" + "/{customerId}/profile-image", customerDTO.getId())
                .accept(MediaType.IMAGE_JPEG)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(byte[].class)
                .returnResult()
                .getResponseBody();

        byte[] actual = Files.toByteArray(image.getFile());

        assertThat(actual).isEqualTo(downloadedImage);

    }

}
