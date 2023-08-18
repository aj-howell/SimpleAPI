import axios from 'axios';

const VITE_API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

export const getCustomers = async ()=>
{
	try{
		const customers= await axios.get(`${VITE_API_BASE_URL}/api/v1/customers`);
		//console.log(customers);
		return customers;
		
	}catch(err)
	{
		console.log(err);
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
		const customers = await axios.delete(`${VITE_API_BASE_URL}/api/v1/customers/${id}`);
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
		const customers = await axios.put(`${VITE_API_BASE_URL}/api/v1/customers/${id}`, update); //think of what the response body would actually be instead of specific values
		return customers;		
	}catch(err)
	{
		throw err;
	}
}
