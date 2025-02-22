import './global.css';
import './i18n';
import {GluestackUIProvider} from '@/components/ui/gluestack-ui-provider';
import {RooStack} from '@/router';
import {SafeAreaProvider} from 'react-native-safe-area-context';
import {InstalledAppsProvider} from './providers';
import {DefaultTheme, NavigationContainer} from '@react-navigation/native';
import {MMKV} from 'react-native-mmkv';

export const storage = new MMKV();

const theme: typeof DefaultTheme = {
  ...DefaultTheme,
  colors: {
    ...DefaultTheme.colors,
    background: '#24222f',
    card: '#24222f',
    text: '#fff',
    border: 'transparent',
  },
};

function App() {
  return (
    <GluestackUIProvider mode="dark">
      <SafeAreaProvider>
        <InstalledAppsProvider>
          <NavigationContainer theme={theme}>
            <RooStack />
          </NavigationContainer>
        </InstalledAppsProvider>
      </SafeAreaProvider>
    </GluestackUIProvider>
  );
}

export default App;
