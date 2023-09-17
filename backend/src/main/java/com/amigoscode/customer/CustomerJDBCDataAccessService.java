package com.amigoscode.customer;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

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
				SELECT * 
				FROM customer
				ORDER BY id ASC
				LIMIT 50
				""";

		
		//grabs all customers from database then maps each characteristic to a new customer to then store into a list
		return  jdbctemplate.query(sql, customerRowMapper)
				.stream()
				.toList();
	}
	
	@Override
	public Optional<Customer> selectCustomerById(Integer ID)
	{
		var sql = 
				"""
				SELECT id, age, name, email,  password, gender, image_id
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
					INSERT INTO customer(age,name, email, password, gender) VALUES(?,?,?,?,?)
				""";
		int update=jdbctemplate.update(sql, C.getAge(), C.getName(), C.getEmail(), C.getPassword(),C.getGender());
		System.out.println("jdbctemplate update = "+update);
	}

	@Override
	public boolean existsCustomerWithEmail(String Email)
	{
		var sql = """
					SELECT count(id)
				    FROM customer
				    WHERE email = ?
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

	@Override
	public Optional<Customer> selectCustomerByEmail(String email) {
		var sql = 
				"""
				SELECT id, age, name, email,  password, gender, image_id
				FROM customer
				WHERE email = ?
				""";
		return  jdbctemplate.query(sql, customerRowMapper, email)
				.stream()
				.findFirst();
	}

	@Override
	public void uploadCustomerImageID(String ImageId, Integer customerID) {
				var sql = """
					UPDATE customer SET image_id = ? WHERE id = ?
				  """;
			 jdbctemplate.update(sql, ImageId, customerID);
	}

}
