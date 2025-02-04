import './global.css';

import React from 'react';
import { GluestackUIProvider } from './components/ui/gluestack-ui-provider';
import { Navigation } from './router';

function App(): React.JSX.Element {
  return (
    <GluestackUIProvider>
      <Navigation />
    </GluestackUIProvider>
  );
}

export default App;
