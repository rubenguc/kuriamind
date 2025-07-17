import {createNativeStackNavigator} from '@react-navigation/native-stack';
import {Welcome} from './screens/welcome';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {Blocks} from './screens/blocks';
import type {BottomStackParamList, RootStackParamList} from './interfaces';
// import {storage} from './App';
import {ChartNoAxesCombined, House, SettingsIcon} from 'lucide-react-native';
import {Settings} from './screens/settings';
import {useTranslation} from 'react-i18next';
import {Block} from './screens/block';
import {useDripsyTheme} from 'dripsy';
import {Stats} from './screens/stats';

const BottomStack = createBottomTabNavigator<BottomStackParamList>();

export const BottomStackNavigation = () => {
  const {t} = useTranslation('screens');
  const {theme} = useDripsyTheme();

  return (
    <BottomStack.Navigator
      screenOptions={{
        tabBarStyle: {
          borderColor: '#6666',
        },
        headerStyle: {
          height: 40,
          elevation: 0,
          shadowOpacity: 0,
          borderBottomWidth: 0,
        },
        tabBarActiveTintColor: 'white',
        tabBarInactiveTintColor: 'gray',
        tabBarShowLabel: false,
      }}>
      <BottomStack.Screen
        name="Blocks"
        component={Blocks}
        options={{
          // eslint-disable-next-line react/no-unstable-nested-components
          tabBarIcon: ({color, focused}) => (
            <House color={focused ? theme.colors.primary : color} size={26} />
          ),
          title: t('blocks'),
        }}
      />

      <BottomStack.Screen
        name="Stats"
        component={Stats}
        options={{
          // eslint-disable-next-line react/no-unstable-nested-components
          tabBarIcon: ({color, focused}) => (
            <ChartNoAxesCombined
              color={focused ? theme.colors.primary : color}
              size={26}
            />
          ),
          title: t('stats'),
        }}
      />

      <BottomStack.Screen
        name={t('settings') as 'Settings'}
        component={Settings}
        options={{
          // eslint-disable-next-line react/no-unstable-nested-components
          tabBarIcon: ({color, focused}) => (
            <SettingsIcon
              color={focused ? theme.colors.primary : color}
              size={26}
            />
          ),
        }}
      />
    </BottomStack.Navigator>
  );
};

const Stack = createNativeStackNavigator<RootStackParamList>();

export const RootStack = () => {
  const {t} = useTranslation('screens');
  const isFirstTime = true;
  // const isFirstTime = storage.getBoolean('isFirstTime') ?? true;

  return (
    <Stack.Navigator
      screenOptions={{
        headerShown: false,
      }}>
      {isFirstTime && <Stack.Screen name="Welcome" component={Welcome} />}
      <Stack.Screen name="Home" component={BottomStackNavigation} />
      <Stack.Screen
        options={{
          headerShown: true,
          title: t('block'),
        }}
        name={'Block'}
        component={Block}
      />
    </Stack.Navigator>
  );
};
