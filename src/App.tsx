import './global.css';

import React from 'react';
import { GluestackUIProvider } from './components/ui/gluestack-ui-provider';
import { Button, ButtonText } from './components/ui/button';

function App(): React.JSX.Element {
  return (
    <GluestackUIProvider>
      <Button>
        <ButtonText>Left Icon</ButtonText>
      </Button>
    </GluestackUIProvider>
  );
}

export default App;
