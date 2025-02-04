import { View, Text } from 'react-native';
import React from 'react';
import { useNavigation } from '@react-navigation/native';
import { Button, ButtonText } from '../../components/ui/button';

const Home = () => {
  const { navigate } = useNavigation();


  return (
    <View>
      <Button
        onPress={() => navigate('Welcome')}
      >
        <ButtonText>
          to welcome
        </ButtonText>
      </Button>
    </View>
  );
};

export default Home;
