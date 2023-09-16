import { Switch,  useColorMode, FormLabel } from '@chakra-ui/react';
import * as React from 'react';

export default function ToggleDarkMode() {

const { colorMode, toggleColorMode } = useColorMode()

  return (
    <>

    {
    //used in conjunction
    }
    <Switch id='ThemeMode' onChange={()=>
        {
            toggleColorMode();
        }
    }></Switch>

        <FormLabel htmlFor='ThemeMode' mb='0'>
        {colorMode === 'dark' ? 'Toggle Light Mode' : 'Toggle Dark Mode'}
      </FormLabel>
    </>  

   );
}