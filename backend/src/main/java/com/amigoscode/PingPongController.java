package com.amigoscode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController 
{
	//PingPong class
	record PingPong(String result){};
	
	@GetMapping("/ping")
	public PingPong getPingPong()
	{
		System.out.println("PONGGGGGG");
		return new PingPong("HELLO");
	}
}
