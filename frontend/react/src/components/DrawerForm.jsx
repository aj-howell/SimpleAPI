import {
    Button,
      Drawer,
  DrawerBody,
  DrawerFooter,
  DrawerHeader,
  DrawerOverlay,
  DrawerContent,
  DrawerCloseButton,
  Input,
  useDisclosure
} from '@chakra-ui/react';
import CreateCustomerForm from './CreateCustomerForm';

const AddIcon = ()=>
{
	return "+";
}

const CloseIcon = ()=>
{
	return "X"
}

const DrawerForm = ()=>
{
	const {isOpen,onOpen,onClose} = useDisclosure();
	return <>	
	<Button leftIcon={<AddIcon/>}
	colorScheme='facebook'
	onClick ={onOpen}
	>
	Create Customer
	</Button>
    <Drawer isOpen={isOpen} onClose={onClose}>
        <DrawerOverlay />
        <DrawerContent>
          <DrawerCloseButton />
          <DrawerHeader>Create new customer</DrawerHeader>

          <DrawerBody>
          
          <CreateCustomerForm/>
          
          </DrawerBody>

          <DrawerFooter>
           <Button leftIcon={<CloseIcon/>}
           colorScheme='facebook'
           onClick ={onClose}>
           Close
           </Button>
          </DrawerFooter>
        </DrawerContent>
      </Drawer>
    </>
}

export default DrawerForm;