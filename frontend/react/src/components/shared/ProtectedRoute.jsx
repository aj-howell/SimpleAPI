import { useEffect } from "react"
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const ProtectedRoute = ({children}) =>
{
	const navigate = useNavigate();
	const{isAuthenticated} = useAuth();
	
	useEffect(()=>
	{
		if(!isAuthenticated())
		{
			navigate("/");
		}
	})
	
	return isAuthenticated() ? children : "";
}	
	export default ProtectedRoute;
	