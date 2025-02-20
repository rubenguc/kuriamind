import './global.css';
import './i18n';
import {GluestackUIProvider} from '@/components/ui/gluestack-ui-provider';
import {RooStack} from '@/router';
import {SafeAreaProvider} from 'react-native-safe-area-context';
import {InstalledAppsProvider} from './providers';
import {NavigationContainer} from '@react-navigation/native';
import {MMKV} from 'react-native-mmkv';

export const storage = new MMKV();

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
