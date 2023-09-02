import {
  Button,
  Flex,
  FormLabel,
  Heading,
  Input,
  Link,
  Stack,
  Image,
  Text,
  Alert,
  AlertIcon,
  Box,
  useDisclosure
} from '@chakra-ui/react';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import { useAuth } from '../context/AuthContext';
import { errorNotification } from '../../services/notifcation';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import RegisterDrawerForm from '../register/RegisterDrawerForm';

const MyTextInput = ({ label, ...props }) => {
  // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
  // which we can spread on <input>. We can use field meta to show an error
  // message if the field is invalid and it has been touched (i.e. visited)
  const [field, meta] = useField(props);
  return (
    <Box>
      <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
      <Input className="text-input" {...field} {...props} />
      {meta.touched && meta.error ? (
        <Alert className="error" status={"error"} mt={2}>
         <AlertIcon/>
         {meta.error}
        </Alert>
      ) : null}
    </Box>
  );
};

const LoginForm =()=>
{
	const{login}=useAuth();
	const navigate =useNavigate();
	return(
		<Formik 
		initialValues={{username:'',password:''}}
		onSubmit={(values,{setSubmitting})=>
		{
			setSubmitting(true);
			
			login(values).
			then(res=>
			{
				console.log("successfully login", res);
				navigate("/dashboard")
			})
			.catch(err=>
			{
				errorNotification(err.code, "Entered Wrong Credentials")
			})
			.finally(()=>
			{
				setSubmitting=false;
			})
		}}
		validateOnMount={true}
		validationSchema={
			Yup.object(
			{
				username: Yup.string()
				.email()
				.required("please provide an email"),
				password: Yup.string()
				.max(20,"Password must be 20 characters of less")
				.required("please provide a passowrd")
			})}
		>
		{({isValid,isSubmitting})=>
			(
				<Form>
					<Stack spacing={15}>
						<MyTextInput label={"Email"}
						name={"username"}
						type={"email"}
						placeholder={"john.doe@gmail.com"}/>
						
						<MyTextInput label={"Password"}
						name={"password"}
						type={"password"}/>
						
						
						<Button 
						type='submit'>
						Login
						</Button>
					</Stack>
				</Form>
			)}
		</Formik>
	);
}

const Login = ()=> {
  
  const{customer}=useAuth();
  const navigate= useNavigate();
  const {isOpen,onOpen,onClose} = useDisclosure();
  
  useEffect(()=>
  {
	 if(customer)
	 {
		 navigate("/dashboard/customer")
	 } 
  })
  return (
    <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
      <Flex p={8} flex={1} alignItems={'center'} justifyContent={'center'}>
        <Stack spacing={4} w={'full'} maxW={'md'}>
        <Image src={"https://user-images.githubusercontent.com/40702606/210880158-e7d698c2-b19a-4057-b415-09f48a746753.png"} boxSize='100px'
        alt={"Amigoscode Logo"}/>
          <Heading fontSize={'2xl'}>Sign in to your account</Heading>
          <LoginForm/>
       	  <RegisterDrawerForm/>
        </Stack>
      </Flex>
      <Flex flex={1}
      p={10}
      flexDirection={"column"}
      alignItems={"center"}
      justifyContent={"center"}
      bgGradient={{sm: 'linear(to-r,blue.600,purple.600)'}}>
        <Text fontSize={"6xl"} color={"white"} mb={"5px"} fontWeight={"bold"}>
        <Link href="https://github.com/aj-howell/SimpleAPI">View Source</Link>
        </Text>
        <Image
          alt={'Login Image'}
          objectFit={'scale-down'}
          src={
            'https://user-images.githubusercontent.com/40702606/215539167-d7006790-b880-4929-83fb-c43fa74f429e.png'
          }
        />
      </Flex>
    </Stack>
  );
}

export default Login;
