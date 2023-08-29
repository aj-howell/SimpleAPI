package com.amigoscode;

import org.junit.jupiter.api.Test;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("age")).thenReturn(19);
        when(resultSet.getString("name")).thenReturn("Jamila");
        when(resultSet.getString("email")).thenReturn("jamila@gmail.com");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getString("gender")).thenReturn("F");

        // When
        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        // Then
        Customer expected = new Customer(
                1, 19,"Jamila", "jamila@gmail.com", "password", "F"
        );
        assertThat(actual).isEqualTo(expected);
    }
} 
