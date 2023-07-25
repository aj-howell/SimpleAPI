import axios from 'axios';

export const getCustomers = async ()=>
{
	try{
		const customers= await axios.get(`${import.meta.data.env.VITE_API_BASE_DATA}/api/v1/customers`);
		console.log(customers);
		return customers;
		
	}catch(err)
	{
		console.log(err);
	}
}
