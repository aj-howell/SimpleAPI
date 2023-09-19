package com.amigoscode.exceptions;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class DefaultExceptionHandler 
{
	@ExceptionHandler(ResourceNotFound.class)
	public ResponseEntity<ApiError> exceptionHandler(ResourceNotFound e,
			HttpServletRequest request)
	{
		ApiError apiError = new ApiError(request.getRequestURI(),
				e.getMessage(),
				HttpStatus.NOT_FOUND.value(),
				LocalDateTime.now());
		
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);	
	} 
	
	@ExceptionHandler(InsufficientAuthenticationException.class)
	public ResponseEntity<ApiError> exceptionHandler(Exception  e,
			HttpServletRequest request)
	{
		ApiError apiError = new ApiError(request.getRequestURI(),
				e.getMessage(),
				HttpStatus.FORBIDDEN.value(),
				LocalDateTime.now());
				
		return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);	
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> exceptionHandlerAll(Exception  e,
			HttpServletRequest request)
	{
		ApiError apiError = new ApiError(request.getRequestURI(),
				e.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				LocalDateTime.now());
		
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
	} 
	
}
// this is where the other execeptions would live as well & can chain exceptionHandler