package com.amigoscode.customer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

	private final CustomerDAO customerDAO;
	
	public CustomerUserDetailsService(@Qualifier("jdbc") CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return customerDAO.selectCustomerByEmail(username).orElseThrow(
				()->{return new UsernameNotFoundException(username+" has not been found");}
				);
	}

}
