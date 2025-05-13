import './i18n';
import {SafeAreaProvider} from 'react-native-safe-area-context';
import {DefaultTheme, NavigationContainer} from '@react-navigation/native';
import {DripsyProvider} from 'dripsy';
import {RootStack} from './router';
import DripsyTheme from './theme';
import {MMKV} from 'react-native-mmkv';
import {GestureHandlerRootView} from 'react-native-gesture-handler';
import {Toasts} from '@backpackapp-io/react-native-toast';
import {InstalledAppsProvider} from './providers';

export const storage = new MMKV();

const theme: typeof DefaultTheme = {
  ...DefaultTheme,
  colors: {
    ...DefaultTheme.colors,
    background: '#161616',
    card: '#161616',
    text: '#fff',
    border: 'transparent',
  },
};

type MyTheme = typeof DripsyTheme;
declare module 'dripsy' {
  // eslint-disable-next-line @typescript-eslint/no-empty-interface
  interface DripsyCustomTheme extends MyTheme {}
}

function App() {
  return (
    <DripsyProvider theme={DripsyTheme}>
      <SafeAreaProvider>
        <GestureHandlerRootView>
          <InstalledAppsProvider>
            <NavigationContainer theme={theme}>
              <RootStack />
              <Toasts
                defaultStyle={{
                  view: {
                    backgroundColor: '#161616',
                  },
                  text: {
                    color: 'white',
                  },
                }}
              />
            </NavigationContainer>
          </InstalledAppsProvider>
        </GestureHandlerRootView>
      </SafeAreaProvider>
    </DripsyProvider>
  );
}

export default App;
