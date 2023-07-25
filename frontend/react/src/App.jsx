import { Button, ButtonGroup } from '@chakra-ui/react'
import SidebarWithHeader from './shared/SideBar'
import { useEffect } from 'react'
import {getCustomers} from './services/client.js'

const App = ()=>
{
	useEffect(()=>
	{
		getCustomers().then(res =>
		{
			console.log(res);
		})
		.catch(e =>
		{
			console.log(e);
		})
	},[]);
	
	return (
		<SidebarWithHeader>
			<Button>click me</Button>
		</SidebarWithHeader>
	)
}

export default App