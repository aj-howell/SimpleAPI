import React from 'react'
import ReactDOM from 'react-dom/client'
import Customer from './Customer.jsx'
import './index.css'
import { ChakraProvider } from '@chakra-ui/react'
import { createStandaloneToast } from '@chakra-ui/react'
import {createBrowserRouter,RouterProvider} from 'react-router-dom'
import Login from './components/login/Login.jsx'
import AuthProvider from './components/context/AuthContext.jsx'
import ProtectedRoute from './components/shared/ProtectedRoute.jsx'
import Home from './Home.jsx'
const router = createBrowserRouter([
	{
		path: "/",
		element:  <Login/>
	},
	
	{
		path: "/dashboard/customer",
		element: <ProtectedRoute><Customer/></ProtectedRoute>
	},
	
	{
		path: "/dashboard/home",
		element: <ProtectedRoute><Home/></ProtectedRoute>
	}
	
])

const {ToastContainer} = createStandaloneToast()

ReactDOM
.createRoot(document.getElementById('root'))
.render(
  <React.StrictMode>
    <ChakraProvider>
    <ToastContainer/>
    <AuthProvider>
   <RouterProvider router={router} />
    </AuthProvider>
     </ChakraProvider>
  </React.StrictMode>,
)
