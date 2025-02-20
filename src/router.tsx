import {createNativeStackNavigator} from '@react-navigation/native-stack';
import {Welcome} from './screens/welcome';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {Blocks} from './screens/blocks';
import {Block} from './screens/block';
import {Block as IBlock} from './interfaces';
import {NavigatorScreenParams} from '@react-navigation/native';
import {storage} from './App';
import {House, Info as InfoIcon} from 'lucide-react-native';
import {Info} from './screens/info';
import {Settings} from './screens/settings';

const BottomStack = createBottomTabNavigator();

export type BottomStackParamList = {
  Blocks: {
    shouldRefresh?: boolean;
  };
};

export const BottomStackNavigation = () => {
  return (
    <BottomStack.Navigator
      screenOptions={{
        headerStyle: {
          height: 40,
          elevation: 0,
          shadowOpacity: 0,
          borderBottomWidth: 0,
        },
      }}>
      <BottomStack.Screen
        name="Blocks"
        component={Blocks}
        options={{
          tabBarIcon: ({color}) => <House color={color} />,
        }}
      />
      <BottomStack.Screen
        name="More"
        component={Info}
        options={{
          tabBarIcon: ({color}) => <InfoIcon color={color} />,
        }}
      />
    </BottomStack.Navigator>
  );
};

const Stack = createNativeStackNavigator();

export type RootStackParamList = {
  Home: NavigatorScreenParams<BottomStackParamList>;
  Block: {block?: IBlock};
  Welcome: undefined;
  Settings: undefined;
};

export const RooStack = () => {
  const isFirstTime = storage.getBoolean('isFirstTime') ?? true;

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
        }}
        name="Block"
        component={Block}
      />
      <Stack.Screen
        options={{
          headerShown: true,
        }}
        name="Settings"
        component={Settings}
      />
    </Stack.Navigator>
  );
};
