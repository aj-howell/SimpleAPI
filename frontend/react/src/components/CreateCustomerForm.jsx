import { Formik, Form, useField,} from 'formik';
import
{
	FormLabel,
	Input,
	Alert,
    AlertIcon,
    Select,
    Box,
    Button,
    Stack
} from '@chakra-ui/react';
import * as Yup from 'yup';
import { saveCustomer } from '../services/client';
import { errorNotification, successNotification } from '../services/notifcation';

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

const MySelect = ({ label, ...props }) => {
  const [field, meta] = useField(props);
  return (
    <Box>
      <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
      <Select {...field} {...props} />
      {meta.touched && meta.error ? (
        <Alert className="error" status={"error"} mt={2}>
         <AlertIcon/>
         {meta.error}
        </Alert>
      ) : null}
    </Box>
  );
};

// And now we can use these
const CreateCustomerForm = ({fetchCustomers}) => {
  return (
    <>
      <Formik
        initialValues={{
          name: '',
          email: '',
          age: 0, // added for our checkbox
          gender: '', // added for our select
          password: ''
        }}
        
        //have to correspond to the labels below
        validationSchema={Yup.object({
          name: Yup.string()
            .max(15, 'Must be 15 characters or less')
            .required('Required'),
          email: Yup.string()
            .email('Invalid email address')
            .required('Required'),
          age: Yup.number()
            .min(16, 'Must be 16 years old or older')
            .max(100)
            .required('Required'),
          gender: Yup.string()
            .oneOf(
              ['Male', 'Female'],
              'Invalid gender'
            )
            .required('Required'),
          password: Yup.string()
          .min(5, "Must be at least 5 characters")
            .max(20, 'Must be 20 characters or less')
            .required('Required')
        })}
        onSubmit={(customer, { setSubmitting }) => {
		setSubmitting(true)
         saveCustomer(customer)
         .then(res => {
		console.log(res);
		successNotification("Customer Created", `customer: ${customer.name} has been created`);
		fetchCustomers();
		})
         .catch(err =>{
			 console.log(err);
			errorNotification(err.code, err.response.data.message);
		})
		.finally(()=>
		{	 
			setSubmitting(false);
		})
		
        }}
      >
       {({isValid,isSubmitting})=>
       {
		return (<Form>
          <Stack spacing={4}>
          <MyTextInput
            label="Name"
            name="name"
            type="text"
            placeholder="Jane"
          />

          <MyTextInput
            label="Email Address"
            name="email"
            type="email"
            placeholder="jane@formik.com"
          />
          
          <MyTextInput
            label="Age"
            name="age"
            type="number"
            placeholder="10"
          />

          <MySelect label="Gender" name="gender">
            <option value="">Select a Gender</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
          </MySelect>
          
         <MyTextInput
            label="Password"
            name="password"
            type="password"
            placeholder="password"
          />
          
          
          <Button type="submit">Submit</Button>
        </Stack>
        </Form>
       )}}
      </Formik>
    </>
  );
};

export default CreateCustomerForm;