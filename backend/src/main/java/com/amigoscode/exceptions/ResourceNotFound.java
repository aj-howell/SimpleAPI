package com.amigoscode.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND) // maps correct http response
public class ResourceNotFound extends RuntimeException // creating an exception
{

	public ResourceNotFound(String message) {
		super(message);
	}
	
}
