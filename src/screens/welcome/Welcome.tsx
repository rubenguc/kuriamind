import {Step, useWelcome} from './hooks/useWelcome';
import {useTranslation} from 'react-i18next';
import type {NativeStackScreenProps} from '@react-navigation/native-stack';
import type {RootStackParamList} from '@/interfaces';
import {Box, ScrollView} from 'dripsy';
import {Button} from '@/components/ui';
import {Permissions} from './components';
import LinearGradient from 'react-native-linear-gradient';
import {WelcomeText} from './components/WelcomeText';

type WelcomeProps = NativeStackScreenProps<RootStackParamList, 'Welcome'>;

const Welcome = ({navigation}: WelcomeProps) => {
  const {t} = useTranslation('welcome');

  const {handleNext, setAllPermissionsGranted, step, canGoNext} = useWelcome({
    onFinish: () =>
      navigation.replace('Home', {
        screen: 'Blocks',
      }),
  });

  return (
    <LinearGradient
      colors={['#1D71B8', '#000']}
      end={{
        x: 0.5,
        y: 0.5,
      }}
      style={{flex: 1}}>
      <ScrollView sx={{flex: 1, px: '8%'}}>
        {step === Step.WELCOME ? (
          <WelcomeText />
        ) : (
          <Permissions
            onAllPermissionsGranted={() => setAllPermissionsGranted(true)}
          />
        )}
      </ScrollView>

      <Box
        sx={{
          p: 10,
        }}>
        <Button
          isText
          isDisabled={!canGoNext()}
          onPress={handleNext}
          sx={{
            py: 10,
          }}>
          {t(step === Step.WELCOME ? 'next' : 'start_app')}
        </Button>
      </Box>
    </LinearGradient>
  );
};

export default Welcome;
