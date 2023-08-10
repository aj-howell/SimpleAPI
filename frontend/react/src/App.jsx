import { Button, ButtonGroup, Text, Wrap, WrapItem } from '@chakra-ui/react'
import SidebarWithHeader from './components/shared/SideBar'
import { useEffect, useState } from 'react'
import {getCustomers} from './services/client.js'
import { Spinner } from '@chakra-ui/react'
import CardWithImage from './components/Card'
import DrawerForm from './components/DrawerForm'

const App = ()=>
{
	//use states allow you to use setters to define them later
	const [customers, setCustomers]= useState([]);
	const [loading, setLoading]= useState(false);
	
	useEffect(()=>
	{
		setLoading(true);
		getCustomers().then(res =>
		{
			setCustomers(res.data);
		})
		.catch(e =>
		{
			console.log(e);
		})
		.finally( () =>
		{
			setLoading(false);
		})
	},[]);
	
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

if(customers.length <= 0)
{
	return (
		<SidebarWithHeader>
			<Text>No Customers available</Text>
			<DrawerForm />
		</SidebarWithHeader>
	);
}

	
return (
		<SidebarWithHeader>
		<DrawerForm />
		<Wrap>
		{
				//within map component or html element that uses the index needs a key
				customers.map((customer, i) =>
				(
					<WrapItem key={i}>
					<CardWithImage key={i}
						id={i}
						name={customer.name}
						age={customer.age}
						email={customer.email}
						gender={customer.gender}
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