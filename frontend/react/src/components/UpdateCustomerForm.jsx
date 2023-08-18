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
import { saveCustomer, updateCustomer } from '../services/client';
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
const UpdateCustomerForm = ({fetchCustomers, initialValues, id}) => {
  return (
    <>
      <Formik
        initialValues={initialValues}
        
        //have to correspond to the labels below
        validationSchema={Yup.object({
          name: Yup.string()
            .max(15, 'Must be 15 characters or less')
            .required(),
          email: Yup.string()
            .email('Invalid email address')
            .required(),
          age: Yup.number()
            .min(16, 'Must be 16 years old or older')
            .max(100)
            .required()
            
        })}
        onSubmit={(update, { setSubmitting }) => {
		setSubmitting(true)
         updateCustomer(id, update)
         .then(res => {
		console.log(res);
		successNotification("Customer updated", `customer: ${update.name} has been updated`);
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
          
          <Button type="submit">Submit</Button>
        </Stack>
        </Form>
       )}}
      </Formik>
    </>
  );
};

export default UpdateCustomerForm;