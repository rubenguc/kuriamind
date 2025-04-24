import {
  createNativeStackNavigator,
  NativeStackScreenProps,
} from '@react-navigation/native-stack';
import {Welcome} from './screens/welcome';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {Blocks} from './screens/blocks';
import {BottomStackParamList, RootStackParamList} from './interfaces';
import {CompositeScreenProps} from '@react-navigation/native';
import {storage} from './App';
import {House, Plus, SettingsIcon} from 'lucide-react-native';
import {TouchableOpacity} from 'react-native';
import {Settings} from './screens/settings';
import {useTranslation} from 'react-i18next';
import type {StackScreenProps} from '@react-navigation/stack';
import {Block} from './screens/block';

const BottomStack = createBottomTabNavigator<BottomStackParamList>();

const EmptyScreen = () => {
  return null;
};

type BottomStackNavigationProps = CompositeScreenProps<
  NativeStackScreenProps<RootStackParamList, 'Home'>,
  StackScreenProps<BottomStackParamList>
>;

export const BottomStackNavigation = ({
  navigation,
}: BottomStackNavigationProps) => {
  const {t} = useTranslation('screens');

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
          title: t('blocks'),
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
              onPress={() => navigation.navigate('Block')}>
              <Plus />
            </TouchableOpacity>
          ),
          tabBarButton: ({children}) => <>{children}</>,
          tabBarItemStyle: {
            width: 41,
            flex: 0,
            alignItems: 'center',
          },
        }}
      />
      <BottomStack.Screen
        name={t('settings') as 'Settings'}
        component={Settings}
        options={{
          tabBarIcon: ({color}) => <SettingsIcon color={color} />,
        }}
      />
    </BottomStack.Navigator>
  );
};

const Stack = createNativeStackNavigator<RootStackParamList>();

export const RooStack = () => {
  const {t} = useTranslation('screens');
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
          title: t('block'),
        }}
        name={'Block'}
        component={Block}
      />
    </Stack.Navigator>
  );
};
