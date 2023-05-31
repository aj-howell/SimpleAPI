package com.amigoscode;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.github.javafaker.Faker;

@Testcontainers
public abstract class AbstractTestcontainersUnitTest
{
	@BeforeAll
	static void beforeAll() // connects flyway to container
	{
		Flyway flyway = Flyway.configure()
				.dataSource(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword())
				.load();
				flyway.migrate();
				System.out.println();
	}
	
	@Container
	protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest") //sets up postgres container info
			.withDatabaseName("amigoscode-dao-unit-test")
			.withUsername("amigoscode")
			.withPassword("password");

	@DynamicPropertySource
	private static void registerDataSourceProperties(DynamicPropertyRegistry registry)
	{
		registry.add("spring.datasource.url", ()-> postgreSQLContainer.getJdbcUrl());
		registry.add("spring.datasource.password", ()-> postgreSQLContainer.getPassword());
		registry.add("spring.datasource.username", ()-> postgreSQLContainer.getUsername());
	}
	
	private static DataSource getDataSource()
	{
		DataSourceBuilder builder = DataSourceBuilder.create()
				.driverClassName(null)
				.url(postgreSQLContainer.getJdbcUrl())
				.username(postgreSQLContainer.getUsername())
				.password(postgreSQLContainer.getPassword());
		
		return builder.build();
	}
	
	public JdbcTemplate getJDBC()
	{
		return new JdbcTemplate(getDataSource());
	}
	
	protected final Faker faker = new Faker();
	
}
