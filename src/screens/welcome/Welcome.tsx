import { View } from 'react-native';
import React from 'react';
import { useNavigation } from '@react-navigation/native';
import { Button, ButtonText } from '../../components/ui/button';

const Welcome = () => {
  const { navigate } = useNavigation();


  return (
    <View>
      <Button
        onPress={() => navigate('HomeTabs')}
      >
        <ButtonText>
          to welcome
        </ButtonText>
      </Button>
    </View>
  );
};

export default Welcome;
