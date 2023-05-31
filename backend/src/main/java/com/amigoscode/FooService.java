package com.amigoscode;

import org.springframework.stereotype.Service;

@Service
public class FooService
{
	private final Main.Foo foo;
	
	public FooService(Main.Foo foo) // sets the bean that you have to the one of the class
	//this class serves as a business layer for what to do when you have a created a new bean
	{
		this.foo=foo;
	}
	
	String getFooName()
	{
		return this.foo.Name();
	}
}
