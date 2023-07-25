const UserProfile = ({name,gender, age, imageNum, ...props}) => 
{
	//converted gender into string 
	gender = gender === "MALE" ?  "men": "women";
	return (
		<div>
			<p>{name}</p>
			<img src = {`https://randomuser.me/api/portraits/${gender}/${imageNum}.jpg`}/>
			<p>{age}</p>
			{props.children}
		</div>
	);
}

export default UserProfile;