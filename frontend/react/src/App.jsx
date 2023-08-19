import {  Text, Wrap, WrapItem } from '@chakra-ui/react'
import SidebarWithHeader from './components/shared/SideBar'
import { useEffect, useState } from 'react'
import {getCustomers} from './services/client.js'
import { Spinner } from '@chakra-ui/react'
import CardWithImage from './components/Card'
import DrawerForm from './components/DrawerForm'
import { errorNotification} from './services/notifcation'

const App = ()=>
{
	//use states allow you to use setters to define them later
	const [customers, setCustomers]= useState([]);
	const [loading, setLoading]= useState(false);
	const [error, setError]= useState("");
	
	const fetchCustomers = () =>
{
	setLoading(true);
		getCustomers().then(res =>
		{
			setCustomers(res.data);
			
		})
		.catch(err =>
		{
			setError(err.code, err.response.data.message);
			errorNotification(err.code, err.response.data.message)
		})
		.finally( () =>
		{
			setLoading(false);
		})
}
	
	useEffect(()=>
	{
		fetchCustomers();
	},[])
	


if(loading)
{
  return( <SidebarWithHeader>
	<Spinner 
		thickness='4px'
		speed='0.65s'
		emptyColor='gray.200'
		color='blue.500'
		size='xl'/>
	</SidebarWithHeader>)
}

if(error)
{
	//can pass functions as props as well
	return (
		<SidebarWithHeader>
			<DrawerForm fetchCustomers ={fetchCustomers}/> 
			<Text mt={5}>An error has occured</Text>
		</SidebarWithHeader>
	);
}

if(customers.length <= 0)
{
	//can pass functions as props as well
	return (
		<SidebarWithHeader>
		
			<DrawerForm fetchCustomers ={fetchCustomers}/> 
			<Text mt={5}>No Customers available</Text>
		</SidebarWithHeader>
	);
}

return (
		<SidebarWithHeader>
		<DrawerForm fetchCustomers ={fetchCustomers}/>
		<Wrap>
		{
				//within map component or html element that uses an index needs a key
				customers.map((customer, i) =>
				(
					<WrapItem key={i}>
					<CardWithImage key={i}
						id={customer.id}
						name={customer.name}
						age={customer.age}
						email={customer.email}
						gender={customer.gender}
						fetchCustomers={fetchCustomers}
					>
					</CardWithImage>
					</WrapItem>
					
				))
		}
		</Wrap>
		</SidebarWithHeader>
	)
}
export default App