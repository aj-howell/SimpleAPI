package com.amigoscode;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.IdNumber;

@SpringBootApplication // Configures and finds packages & paths 
public class Main
{

	public static void main(String[] args) {
		
		ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class,"hello");
		//printBean(applicationContext);
	}
	
	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository) //this saves our entries 
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
						email
						
					);			
//			Customer Alex = new Customer(1, 21, "Alex", "Alex@gmail.com");
//			Customer Josh = new Customer(2, 25, "Josh", "Josh@gmail.com");
//			List<Customer> customers = List.of(customer);
			
			customerRepository.save(customer);
			//customerRepository.findById(1);
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

