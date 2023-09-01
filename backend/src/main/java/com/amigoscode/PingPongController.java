package com.amigoscode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController 
{
	private static int count = 0;
	
	//PingPong class
	record PingPong(String result){};
	
	@GetMapping("/ping")
	public PingPong getPingPong()
	{
		System.out.println("PONG");
		count++;
		return new PingPong("HELLOOOOOOOOOOO : "+count);
		
	}
}
