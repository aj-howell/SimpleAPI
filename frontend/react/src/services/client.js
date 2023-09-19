import axios from 'axios';

const VITE_API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

export const getCustomers = async () => {
  try {
    const customers = await axios.get(`${VITE_API_BASE_URL}/api/v1/customers`, getAuthConfig());
    return customers;//actual object
  } catch (err) {
    console.log(err);
    throw err;
  }
}

export const getCustomer = async (id) => {
  try {
    const customer = await axios.get(`${VITE_API_BASE_URL}/api/v1/customers/${id}`, getAuthConfig());
    return customer;
  } catch (err) {
    console.log(err);
    throw err;
  }
}


export const saveCustomer = async(customer)=>
{
		// eslint-disable-next-line no-useless-catch
		try{
		// this will add our customer request into the json file
		const customers= await axios.post(`${VITE_API_BASE_URL}/api/v1/customers`, customer);
		//console.log(customers);
		return customers;
		
	}catch(err)
	{
		throw err;
	}
}

export const deleteCustomer = async(id)=>
{
	// eslint-disable-next-line no-useless-catch	
	try
	{
		const customers = await axios.delete(`${VITE_API_BASE_URL}/api/v1/customers/${id}`,
		getAuthConfig());
		return customers;		
	}catch(err)
	{
		throw err;
	}
}

export const updateCustomer = async(id,update)=>
{
	// eslint-disable-next-line no-useless-catch	
	try
	{
		const customers = await axios.put(`${VITE_API_BASE_URL}/api/v1/customers/${id}`, update,
		getAuthConfig()); //think of what the response body would actually be instead of specific values
		return customers;		
	}catch(err)
	{
		throw err;
	}
}

export const login = async(UsernameAndPassword)=>
{
	// eslint-disable-next-line no-useless-catch	
	try
	{
	  return await axios.post(`${VITE_API_BASE_URL}/api/v1/auth/login`, UsernameAndPassword);
				
	}catch(err)
	{
		throw err;
	}
}

export const updateImage= async(id, formData)=>
{
	try {
		return axios.post(`${VITE_API_BASE_URL}/api/v1/customers/${id}/profile-image`,formData, 
		{
			...getAuthConfig(),
			'Content-Type' : 'multipart/form-data'
		});
	} 
	catch (err) {
		throw err;
	}
}

export const downloadImageURL = (id) => {

	 return  `${VITE_API_BASE_URL}/api/v1/customers/${id}/profile-image`;
	
  }

export const getAuthConfig = ()=>
({headers:
{
	Authorization: `Bearer ${localStorage.getItem("access_token")}`
}})
