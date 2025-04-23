import './i18n';
import {SafeAreaProvider} from 'react-native-safe-area-context';
import {DefaultTheme, NavigationContainer} from '@react-navigation/native';
import {DripsyProvider} from 'dripsy';
import {RooStack} from './router';
import DripsyTheme from './theme';
import {MMKV} from 'react-native-mmkv';
import {GestureHandlerRootView} from 'react-native-gesture-handler';
import {Toasts} from '@backpackapp-io/react-native-toast';

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
          <NavigationContainer theme={theme}>
            <RooStack />
            <Toasts />
          </NavigationContainer>
        </GestureHandlerRootView>
      </SafeAreaProvider>
    </DripsyProvider>
  );
}

export default App;
