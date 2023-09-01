import {
  Button,
  Drawer,
  DrawerBody,
  DrawerFooter,
  DrawerHeader,
  DrawerOverlay,
  DrawerContent,
  DrawerCloseButton,
  useDisclosure
} from '@chakra-ui/react';
import RegisterCustomerForm from './RegisterCustomerForm';

const RegisterDrawerForm = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <>
      <Button
        colorScheme='facebook'
        onClick={onOpen}
      >
        Register
      </Button>
      <Drawer size='xl' placement='left' isOpen={isOpen} onClose={onClose}>
        <DrawerOverlay />
        <DrawerContent>
          <DrawerCloseButton />
          <DrawerHeader>Register</DrawerHeader>
          <DrawerBody>
            <RegisterCustomerForm />
          </DrawerBody>
          <DrawerFooter>
            <Button
              colorScheme='facebook'
              onClick={onClose}
            >
              Close
            </Button>
          </DrawerFooter>
        </DrawerContent>
      </Drawer>
    </>
  );
};

export default RegisterDrawerForm;