package com.amigoscode.customer;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
		),
		
		@UniqueConstraint
		(
				name="customer_image_id_unqiue",
				columnNames="image_id"
		)	
	}
)
public class Customer implements UserDetails
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
	    
	    @Column(nullable = false)
	    private String password;

		@Column(unique = true)
	    private String image_id;

		@Column(nullable = false)
		private String email;
		
		public Customer(){} // default constructor
		
		public Customer(Integer id, Integer age, String name, String email, String password, String gender)
		{
			this.id=id;
			this.age=age;
			this.name=name;
			this.email=email;
			this.gender=gender;
			this.password=password;
		}

			public Customer(Integer id, Integer age,String name, String email, String password, String gender,String ImageId)
		{
			this.id=id;
			this.age=age;
			this.image_id=ImageId;
			this.name=name;
			this.email=email;
			this.gender=gender;
			this.password=password;
		}
		
		public Customer( Integer age, String name, String email, String password, String gender) // id will automatically be sequenced by the database
		{
			this.age=age;
			this.name=name;
			this.email=email;
			this.password=password;
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
		
		public String getImage_id() {
			return image_id;
		}

		public void setImage_id(String imageId) {
			this.image_id = imageId;
		}
		

		@Override
		public int hashCode() {
			return Objects.hash(id,image_id,age,password,email,name,gender);
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
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (age == null) {
				if (other.age != null)
					return false;
			} else if (!age.equals(other.age))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (gender == null) {
				if (other.gender != null)
					return false;
			} else if (!gender.equals(other.gender))
				return false;
			if (password == null) {
				if (other.password != null)
					return false;
			} else if (!password.equals(other.password))
				return false;
			if (image_id == null) {
				if (other.image_id != null)
					return false;
			} else if (!image_id.equals(other.image_id))
				return false;
			if (email == null) {
				if (other.email != null)
					return false;
			} else if (!email.equals(other.email))
				return false;
			return true;
		}

		

		@Override
		public String toString() {
			return "Customer {id=" + id + ", age=" + age + ", name=" + name + ", gender=" + gender + ", password="
					+ password + ", ImageId=" + image_id + ", email=" + email + "}";
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			// TODO Auto-generated method stub
			return List.of(new SimpleGrantedAuthority("ROLE_USER"));
		}

		@Override
		public String getPassword() {
			// TODO Auto-generated method stub
			return this.password;
		}

		@Override
		public String getUsername() {
			// TODO Auto-generated method stub
			return email;
		}

		@Override
		public boolean isAccountNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return true;
		}

        public void setPassword(String password) {
			this.password=password;
        }


}

