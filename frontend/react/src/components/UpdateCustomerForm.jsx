import {
  Alert,
  AlertIcon,
  Box,
  Button,
  FormLabel,
  Image,
  Input,
  Select,
  Stack,
  VStack
} from '@chakra-ui/react';
import { Form, Formik, useField, } from 'formik';
import React, { useCallback } from 'react';
import { useDropzone } from 'react-dropzone';
import * as Yup from 'yup';
import { downloadImageURL, updateCustomer, updateImage } from '../services/client';
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

const MyDropzone=({id, fetchCustomers})=>{
  const onDrop = useCallback(acceptedFiles => {
    const formData= new FormData();
    // "file" bc it has to match what the client sends
    formData.append("file", acceptedFiles[0])
    updateImage(id, formData)
    .then(()=>
    {
      successNotification("Successful Upload", "You have successfully uploaded a file");
      fetchCustomers();
    })
    .catch(()=>
    {
      errorNotification("Unsucessful Upload", "You did not successfully upload a file");
    });
  }, [])
  const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

  return (
    <Box {...getRootProps()}
    w={'100%'}
    textAlign={'Center'}
    border={'dashed'}
    borderColor={'gray.200'}
    rounded={'md'}
    borderRadius={'3xl'}>
      <input {...getInputProps()} />
      {
        isDragActive ?
          <p>Drop the picture here ...</p> :
          <p>Drag 'n' drop some picture here, or click to select files</p>
      }
    </Box>
  )
}

// And now we can use these
const UpdateCustomerForm = ({fetchCustomers, initialValues, id}) => {
  return (
    <>
    <VStack spacing={'5'} mb={'5'}>
      <Image
      borderRadius={'full'}
      boxSize={'150px'}
      objectFit={'cover'}
      src={downloadImageURL(id)}
      />
      <MyDropzone id={id} fetchCustomers={fetchCustomers}/>
    </VStack>

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