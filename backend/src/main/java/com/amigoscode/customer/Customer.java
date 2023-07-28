package com.amigoscode.customer;

import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table
(
	name="customer",
	uniqueConstraints=
	{
		@UniqueConstraint
		(
				name="customer_email_unique",
				columnNames="email"
		)	
	}
)
public class Customer 
{
	//these annotations help map a class to tables in a database (including Entity)
		@Id
		@SequenceGenerator(name="customer_id_seq",
		sequenceName="customer_id_seq",
		allocationSize=1)
		@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="customer_id_seq")
		
	    private Integer id;

	    @Column(nullable = false)
	    private Integer age;

	    @Column(nullable = false)
	    private String name;

	    @Column(nullable = false)
	    private String gender;

		
		
		private String email;
		
		public Customer(){} // default constructor
		
		public Customer(Integer id, Integer age, String name, String email, String gender)
		{
			this.id=id;
			this.age=age;
			this.name=name;
			this.email=email;
			this.gender=gender;
		}
		
		public Customer( Integer age, String name, String email, String gender) // id will automatically be sequenced by the database
		{
			this.age=age;
			this.name=name;
			this.email=email;
			this.gender=gender;
		}
		
		public Integer getId() {
			return id;
		}
		public Integer getAge() {
			return age;
		}
		public String getName() {
			return name;
		}
		public String getEmail() {
			return email;
		}
		
		public String getGender() {
			return gender;
		}

		public void setGender(String gender){
			this.gender = gender;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		
		
		
		@Override
		public int hashCode()
		{
			return Objects.hash(id, name,email, age, gender);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Customer other = (Customer) obj;
			return Objects.equals(age, other.age) && Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(gender, other.gender)
					&& Objects.equals(name, other.name);
		}


}

