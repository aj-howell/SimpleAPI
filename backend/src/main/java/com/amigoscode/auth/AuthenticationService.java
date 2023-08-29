package com.amigoscode.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.amigoscode.JWT.AuthenticationRequest;
import com.amigoscode.JWT.AuthenticationResponse;
import com.amigoscode.JWT.JWTUtil;
import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerDTO;

@Service
public class AuthenticationService
{
	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	
	public AuthenticationService(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		
	}
	
	public AuthenticationResponse Login(AuthenticationRequest request)
	{
		 Authentication authentication = authenticationManager.authenticate(
		            new UsernamePasswordAuthenticationToken(
		                    request.username(),
		                    request.password()
		            )
		    );
		 
		    Customer principal = (Customer) authentication.getPrincipal();
		    CustomerDTO customerDTO = new CustomerDTO(principal);
		    String token = jwtUtil.issueToken(customerDTO.getName(), customerDTO.getRoles());
		    return new AuthenticationResponse(token);
	}
}
