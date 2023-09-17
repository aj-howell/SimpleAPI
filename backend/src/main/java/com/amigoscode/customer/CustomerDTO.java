package com.amigoscode.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerDTO 
{
	public String name;
	public String email;
	public String gender;
	public String username;
	public List<String> roles;
	public Integer age;
	public Integer id;
	public String image_id;
	
	 public CustomerDTO() {
	    }
	
	public CustomerDTO(Customer c) 
	{
		this.name=c.getName();
		this.email=c.getEmail();
		this.gender=c.getGender();
		this.image_id=c.getImage_id();
		this.age=c.getAge();
		this.id=c.getId();
		this.roles= new ArrayList<String>();
		c.getAuthorities().forEach(a->
		{
			roles.add(a.getAuthority());
		});
		this.username=c.getUsername();
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerDTO other = (CustomerDTO) obj;
		return Objects.equals(age, other.age) && Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(gender, other.gender)
				&& Objects.equals(name, other.name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
