import {createNativeStackNavigator} from '@react-navigation/native-stack';
import {Welcome} from './screens/welcome';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {Blocks} from './screens/blocks';
import {Block} from './screens/block';
import {Block as IBlock} from './interfaces';
import {NavigatorScreenParams} from '@react-navigation/native';

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
      <BottomStack.Screen name="Blocks" component={Blocks} />
    </BottomStack.Navigator>
  );
};

const Stack = createNativeStackNavigator();

export type RootStackParamList = {
  Home: NavigatorScreenParams<BottomStackParamList>;
  Block: {block?: IBlock};
  Welcome: undefined;
};

export const RooStack = () => {
  return (
    <Stack.Navigator
      screenOptions={{
        headerShown: false,
      }}>
      <Stack.Screen name="Home" component={BottomStackNavigation} />
      <Stack.Screen
        options={{
          headerShown: true,
        }}
        name="Block"
        component={Block}
      />
      <Stack.Screen name="Welcome" component={Welcome} />
    </Stack.Navigator>
  );
};
