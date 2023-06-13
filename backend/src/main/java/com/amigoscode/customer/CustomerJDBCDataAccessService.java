package com.amigoscode.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO
{

	private final JdbcTemplate jdbctemplate;
	private final CustomerRowMapper customerRowMapper;

	public CustomerJDBCDataAccessService(JdbcTemplate jdbctemplate, CustomerRowMapper customerRowMapper)
	{
		this.jdbctemplate = jdbctemplate;
		this.customerRowMapper=customerRowMapper;
	}

	@Override
	public List<Customer> SelectAllCustomers() {
		var sql = 
				"""
				SELECT id, age, name, email 
				FROM customer
				""";
		
		//grabs all customers from database then maps each characteristic to a new customer to then store into a list
		return  jdbctemplate.query(sql, customerRowMapper);
		
	
	}
	
	@Override
	public Optional<Customer> selectCustomerById(Integer ID)
	{
		var sql = 
				"""
				SELECT id, age, name, email
				FROM customer
				WHERE id = ?
				""";
		return  jdbctemplate.query(sql, customerRowMapper, ID)
				.stream()
				.findFirst();
	}

	@Override
	public void insertCustomer(Customer C) {
		var sql="""
					INSERT INTO customer(age,name, email) VALUES(?,?,?)
				""";
		int update=jdbctemplate.update(sql, C.getAge(), C.getName(), C.getEmail());
		System.out.println("jdbctemplate update = "+update);
	}

	@Override
	public boolean existsCustomerWithEmail(String Email)
	{
		var sql = """
					SELECT count(id)
				    FROM customer
				    WHERE name = ?
				  """;
		return jdbctemplate.queryForObject(sql, Integer.class, Email) != 0;
	}


	@Override
	public boolean existsCustomerWithId(Integer Id)
	{
		var sql = """
					SELECT count(id)
					FROM customer
					WHERE id = ?
				  """;
		return jdbctemplate.queryForObject(sql, Integer.class, Id) != 0;
	}

	@Override
	public void deleteCustomerById(Integer id)
	{
		var sql = """
					DELETE
					FROM customer
					WHERE id = ?
				  """;
		jdbctemplate.update(sql, id);
		
	}

	@Override
	public void updateCustomer(Customer c)
	{
		if(c.getAge()!=null)
		{
			var sql = """
					UPDATE customer SET age = ? WHERE id = ?
				  """;
			 jdbctemplate.update(sql, c.getAge(), c.getId());
			 System.out.println("age changed to: "+c.getAge());
		}
		
		if(c.getEmail()!=null)
		{
			var sql = """
					UPDATE customer SET email = ? WHERE id = ?
				  """;
			jdbctemplate.update(sql, c.getEmail(), c.getId());
			System.out.println("email changed to: "+c.getEmail());
		}
		
		if(c.getName()!=null)
		{
			var sql = """
					UPDATE customer SET name = ? WHERE id = ?
				  """;
			jdbctemplate.update(sql, c.getName(), c.getId());
			System.out.println("name changed to: "+c.getName());
		}
		
		
		
	}

}
