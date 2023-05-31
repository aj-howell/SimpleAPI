package com.amigoscode.customer;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component //instantiates for us
public class CustomerRowMapper implements RowMapper<Customer>
{

	@Override
	public Customer mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		Customer customer = new Customer(
				rs.getInt("id"),
				rs.getInt("age"),
				rs.getString("name"),
				rs.getString("email")
			);
		return customer;
	}

}
