import {createNativeStackNavigator} from '@react-navigation/native-stack';
import {Welcome} from './screens/welcome';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {Blocks} from './screens/blocks';
import {Block} from './screens/block';
import {Block as IBlock} from './interfaces';
import {NavigatorScreenParams} from '@react-navigation/native';
import {storage} from './App';
import {EditIcon, House, Info as InfoIcon, Plus} from 'lucide-react-native';
import {Info} from './screens/info';
import {Settings} from './screens/settings';
import {HStack} from './components/ui/hstack';
import {TouchableOpacity, View} from 'react-native';
import {Button, ButtonIcon} from './components/ui/button';
import {Text} from './components/ui/text';
import {Box} from './components/ui/box';

const BottomStack = createBottomTabNavigator();

export type BottomStackParamList = {
  Blocks: {
    shouldRefresh?: boolean;
  };
};

const EmptyScreen = () => {
  return null;
};

export const BottomStackNavigation = ({navigation}) => {
  return (
    <BottomStack.Navigator
      screenOptions={{
        tabBarStyle: {
          borderColor: '#555',
        },
        headerStyle: {
          height: 40,
          elevation: 0,
          shadowOpacity: 0,
          borderBottomWidth: 0,
        },
        tabBarActiveTintColor: 'white',
        tabBarInactiveTintColor: 'gray',
      }}>
      <BottomStack.Screen
        name="Blocks"
        component={Blocks}
        options={{
          tabBarIcon: ({color}) => <House color={color} />,
        }}
      />
      <BottomStack.Screen
        name="BlockAction"
        component={EmptyScreen}
        options={{
          tabBarLabel: '',
          tabBarIcon: () => (
            <TouchableOpacity
              activeOpacity={0.95}
              onPress={() =>
                navigation.navigate('Block', {
                  block: undefined,
                })
              }>
              <Box className="bg-custom-green p-4 rounded-2xl">
                <Plus />
              </Box>
            </TouchableOpacity>
          ),
          tabBarButton: ({children}) => <>{children}</>,
          tabBarItemStyle: {
            // backgroundColor: 'green',
            width: 41,
            flex: 0,
            alignItems: 'center',
          },
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
