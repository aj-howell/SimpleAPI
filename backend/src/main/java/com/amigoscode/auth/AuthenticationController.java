package com.amigoscode.auth;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.amigoscode.JWT.AuthenticationRequest;
import com.amigoscode.JWT.AuthenticationResponse;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController{

	private final AuthenticationService service;

	public AuthenticationController(AuthenticationService service)
	{
		this.service=service;
	} 
	
	@PostMapping("/login")
	public ResponseEntity<?> authLogin(@RequestBody AuthenticationRequest request)
	{
	       AuthenticationResponse response = service.Login(request);
	        return ResponseEntity.ok()
	                .header(HttpHeaders.AUTHORIZATION, response.token()) 
	                .body(response); // attach custom response to body
	}
	
}
