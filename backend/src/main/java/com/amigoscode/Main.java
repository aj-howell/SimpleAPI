package com.amigoscode;

import java.util.Random;
import java.util.UUID;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.amigoscode.S3.S3Service;
import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRepository;
import com.github.javafaker.Faker;

import software.amazon.awssdk.services.s3.S3Client;

@SpringBootApplication // Configures and finds packages & paths 
public class Main
{

	public static void main(String[] args) throws InterruptedException {
		
		ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class,"hello");
		//printBean(applicationContext);
	}
	
	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) //this saves our entries 
	{
		
		return args ->{
			 var fake = new Faker();
			 Random ran= new Random();
			 String Fname = fake.name().firstName();
			 String Lname = fake.name().lastName();
			 String email = Fname+"."+Lname+"@gmail.com";
			
			 Customer customer = new Customer
			 		(
			 			ran.nextInt(16, 99),
			 			Fname+" "+Lname,
			 			email,
			 			passwordEncoder.encode(UUID.randomUUID().toString()),
			 			fake.demographic().sex()
			 		);			
			
			 customerRepository.save(customer);
		};
		
	}
	
// How to create a Bean
	@Bean("foo") // without string its just beans
	@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Foo getFoo()
	{
		return new Foo("bar");
	}

	record Foo(String Name){}
	
	private static void printBean(ConfigurableApplicationContext ctx)
	{
		String[] beanNames = ctx.getBeanDefinitionNames();
		
		for(String beanName:beanNames)
		{
			System.out.println(beanName);
		}
	}
		
}
