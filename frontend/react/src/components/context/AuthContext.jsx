import { createContext, useContext, useEffect, useState } from "react";
import { login as performLogin, getCustomer } from '../../services/client.js';
import jwtDecode from 'jwt-decode';

const AuthContext = createContext({});

export const useAuth = () => {
	return useContext(AuthContext);
};

const AuthProvider = ({ children }) => {
	const [customer, setCustomer] = useState(null);
	
	useEffect(()=>
	{
		let token = localStorage.getItem("access_token");
		if(token)
		{
			let decoded=jwtDecode(token)
			setCustomer(
				{
					username: decoded.sub,
					roles: decoded.scopes
				});
		}
	},[])
	
	const login = async (UsernameAndPassword) => {
		try {
			const res = await performLogin(UsernameAndPassword);
			const jwtToken = res.headers["authorization"];
			localStorage.setItem("access_token", jwtToken);
			
			const decoded = jwtDecode(jwtToken);
			
			console.log("hello "+decoded.sub);
			setCustomer(
				{
					username: decoded.sub,
					roles: decoded.scopes
				});

			return res;
		} catch (err) {
			console.log(err);
			throw err;
		}
	};
	
	const isAuthenticated = ()=>
	{
		
		const token= localStorage.getItem("access_token");
		
		if(!token)
		{
			return false;
		}
		
		const {exp: expiration} = jwtDecode(token);
		
		if(Date.now() > expiration*1000)
		{
			logout();
			return false;
		}
		
		return true;
	}

	const logout = () => {
		setCustomer(null);
		localStorage.removeItem("access_token");
	}

	return (
		<AuthContext.Provider value={{
			customer,
			login,
			logout,
			isAuthenticated
		}}>
			{children}
		</AuthContext.Provider>
	);
};

export default AuthProvider;

