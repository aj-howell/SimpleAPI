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