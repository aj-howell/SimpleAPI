/* eslint-disable no-mixed-spaces-and-tabs */
import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    Tag,
    useColorModeValue,
    Button,
    AlertDialog,
    AlertDialogBody,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogContent,
    AlertDialogOverlay,
    useDisclosure,
    Drawer,
  	DrawerBody,
  	DrawerFooter,
  	DrawerHeader,
  	DrawerOverlay,
  	DrawerContent,
  	DrawerCloseButton
} from '@chakra-ui/react';
import React from 'react';
import { deleteCustomer } from '../services/client';
import { errorNotification, successNotification } from '../services/notifcation';
import UpdateCustomerForm from './UpdateCustomerForm';

export default function CardWithImage({id, name, email, age, gender, fetchCustomers}) {
	
	const disclosure = useDisclosure()
	const disclosure2=useDisclosure();
  	const cancelRef = React.useRef()
  	
  	const AddIcon = ()=>
	{
		return "+";
	}

	const CloseIcon = ()=>
	{
		return "X"
	}
  	
	const rand_gender = gender === 'Male' ? "men":"women";
    return (
        <Center py={6}>
            <Box
                maxW={'300px'}
                w={'full'}
                bg={useColorModeValue('white', 'gray.800')}
                boxShadow={'2xl'}
                rounded={'md'}
                overflow={'hidden'}>
                <Image
                    h={'120px'}
                    w={'full'}
                    src={
                        'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
                    }
                    objectFit={'cover'}
                />
                <Flex justify={'center'} mt={-12}>
                    <Avatar
                        size={'xl'}
                        src={
                            `https://randomuser.me/api/portraits/${rand_gender}/${age}.jpg`
                        }
                        alt={'Author'}
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>

                <Box p={6}>
                    <Stack spacing={2} align={'center'} mb={5}>
                        <Tag borderRadius={"full"}>{id}</Tag>
                        <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                            {name}
                        </Heading>
                        <Text color={'gray.500'}>{email}</Text>
                        <Text color={'gray.500'}>Age {age} | {gender}</Text>
                        <Button
						colorScheme='facebook'
						onClick ={disclosure2.onOpen}
						mb={2.5}
						mt={2}
						borderRadius={18}
						>
						Update Customer
						</Button>
					    <Drawer isOpen={disclosure2.isOpen} onClose={disclosure2.onClose}>
					        <DrawerOverlay />
					        <DrawerContent>
					          <DrawerCloseButton />
					          <DrawerHeader>Update customer</DrawerHeader>
					
					          <DrawerBody>
					          
					          <UpdateCustomerForm fetchCustomers ={fetchCustomers} initialValues={{name,age, email}} id={id}/>
					          
					          </DrawerBody>
					
					          <DrawerFooter>
					           <Button leftIcon={<CloseIcon/>}
					           colorScheme='facebook'
					           onClick ={disclosure2.onClose}>
					           Close
					           </Button>
					          </DrawerFooter>
					        </DrawerContent>
					      </Drawer>

                        <Button colorScheme='red' onClick={disclosure.onOpen} borderRadius={18}>Delete Customer</Button>
                     <AlertDialog
        				 isOpen={disclosure.isOpen}
       					 leastDestructiveRef={cancelRef}
        				 onClose={disclosure.onClose}>
        				<AlertDialogOverlay>
          				<AlertDialogContent>
            			<AlertDialogHeader fontSize='lg' fontWeight='bold'>Delete Customer</AlertDialogHeader>
            			<AlertDialogBody>Are you sure? You can't undo this action afterwards.</AlertDialogBody>
            			<AlertDialogFooter>
              			<Button ref={cancelRef} onClick={disclosure.onClose}>Cancel</Button>
              			<Button colorScheme='red' onClick={
							  ()=>
							  {
								  deleteCustomer(id)
								  .then((res)=>
								  {
									  console.log(res);
									  successNotification(`Deleted Customer`,`Customer: ${name} has been deleted`);
									  fetchCustomers();
								  })
								  .catch((err)=>
								  {
									  errorNotification(err.code,err.response.data.message)
									  console.log(err)
								  })
								  .finally(()=>
								  {
									  disclosure.onClose();
								  })
							}
						} ml={3}>Delete</Button>
            			</AlertDialogFooter>
          				</AlertDialogContent>
        				</AlertDialogOverlay>
      				</AlertDialog>
                    </Stack>
                </Box>
            </Box>
        </Center>
    );
}
