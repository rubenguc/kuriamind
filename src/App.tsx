import './global.css';

import {GluestackUIProvider} from '@/components/ui/gluestack-ui-provider';
import {RooStack} from '@/router';
import {SafeAreaProvider} from 'react-native-safe-area-context';
import {InstalledAppsProvider} from './providers';
import {NavigationContainer} from '@react-navigation/native';

function App() {
  return (
    <GluestackUIProvider>
      <SafeAreaProvider>
        <InstalledAppsProvider>
          <NavigationContainer>
            <RooStack />
          </NavigationContainer>
        </InstalledAppsProvider>
      </SafeAreaProvider>
    </GluestackUIProvider>
  );
}

export default App;
