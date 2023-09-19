package com.amigoscode.customer;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amigoscode.S3.S3Buckets;
import com.amigoscode.S3.S3Service;
import com.amigoscode.exceptions.BadDataRequest;
import com.amigoscode.exceptions.DuplicateResourceException;
import com.amigoscode.exceptions.ResourceNotFound;

@Service
public class CustomerService // serves as a way to actually be able to use the "framework/interface of CustomerDAO"
// this serves as the business layer and any protocols or permissions that need to be handled will be done here which will interact with the database
{
	private final CustomerDAO customerDAO; // gives us access to the methods we need
	private final PasswordEncoder passwordEncoder;
	private final S3Service s3Service;
    private final S3Buckets s3Bucket;
	
	public CustomerService(@Qualifier("jdbc")CustomerDAO customerDAO,  PasswordEncoder passwordEncoder, S3Service s3Service, S3Buckets s3Bucket) //qualifiers configure what repository will be used
	{
		this.customerDAO = customerDAO;
		this.passwordEncoder=passwordEncoder;
		this.s3Service=s3Service;
		this.s3Bucket=s3Bucket;
	}
	
	public List<CustomerDTO> getAllCustomers()
	{
		return customerDAO
				.SelectAllCustomers()
				.stream()
				.map(c-> new CustomerDTO(c))
				.collect(Collectors.toList());
	}
	
	public CustomerDTO getCustomer(Integer ID)
	{
		return customerDAO.selectCustomerById(ID)
				.map(c->new CustomerDTO(c))
				.orElseThrow(
						
						() -> new ResourceNotFound(
								"customer with id [%s] not found".formatted(ID)
								));
	}
	
	public void addCustomer(CustomerRegistrationRequest c)
	{
		if(customerDAO.existsCustomerWithEmail(c.email()))
		{
			throw new DuplicateResourceException("Email Already Exist");
		}
		
		customerDAO.insertCustomer(new Customer(c.age(),c.name(),c.email(), passwordEncoder.encode(c.password()), c.gender()));
	}

	public void deleteCustomerById(Integer customerId)
	{
		if(!customerDAO.existsCustomerWithId(customerId))
		{
			throw new ResourceNotFound(
					"customer with id [%s] not found".formatted(customerId));
		}
		
		customerDAO.deleteCustomerById(customerId);
	}

	//check here
	public void updateCustomer(Integer customerId, CustomerUpdateRequest R) 
	{
	      // TODO: for JPA use .getReferenceById(customerId) as it does does not bring object into memory and instead a reference
        Customer customer = customerDAO.selectCustomerById(customerId)
        		.orElseThrow
        		(
        				()->new ResourceNotFound(
								"customer with id [%s] not found".formatted(customerId)
								)
        		);

        boolean changes = false;

        if (R.name() != null && !R.name().equals(customer.getName())) {
            customer.setName(R.name());
            changes = true;
        }

        if (R.age() != null && !R.age().equals(customer.getAge())) {
            customer.setAge(R.age());
            changes = true;
        }

        if (R.email() != null && !R.email().equals(customer.getEmail())) {
            if (customerDAO.existsCustomerWithEmail(R.email())) {
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
            customer.setEmail(R.email());
            changes = true;
        }

        if (!changes) {
           throw new BadDataRequest("no data changes found");
        }

        customerDAO.updateCustomer(customer);
	
			
	}

    public void uploadCustomerPhoto(Integer customerId, MultipartFile file) {
		try {
			boolean ans = customerDAO.existsCustomerWithId(customerId)==true ? true:false;
			if(ans){
			 String imageId = UUID.randomUUID().toString();
			 s3Service.putObject(s3Bucket.getCustomer(), "profile/image/customer/"+customerId+"/"+imageId, file.getBytes());

				customerDAO.uploadCustomerImageID(imageId,customerId);
			}

			else
			{
				throw new ResourceNotFound("customer with id [%s] image was not found".formatted(customerId));
			}

		} catch (IOException e) {
			throw new ResourceNotFound("customer with id [%s] image was not found".formatted(customerId));
		}
  }

    public byte[] downloadCustomerPhoto(Integer customerId) {
			 CustomerDTO customer = customerDAO.selectCustomerById(customerId)
			 				.map(c-> new CustomerDTO(c))
							.orElseThrow(()->new ResourceNotFound("Customer with id [%s] was not found".formatted(customerId)));

			if(customer.image_id==null)
			{
				throw new ResourceNotFound("customer with id [%s] image was not found".formatted(customer.getId()));
			}
			else{
			 return s3Service.getObject("profile/image/customer/"+customer.getId()+"/"+customer.getImage_id(), s3Bucket.getCustomer());
			}
    }
}
