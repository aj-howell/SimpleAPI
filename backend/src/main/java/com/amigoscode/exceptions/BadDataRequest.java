package com.amigoscode.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class BadDataRequest extends RuntimeException
{
	public BadDataRequest(String message)
	{
		super(message);
	}
	
}