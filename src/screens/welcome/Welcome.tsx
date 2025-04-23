import {Step, useWelcome} from './hooks/useWelcome';
import {useTranslation} from 'react-i18next';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {RootStackParamList} from '@/interfaces';
import {Box, H1, Image, Text, ScrollView, Flex, View} from 'dripsy';
import {Button} from '@/components/ui';
import {Permissions} from './components';

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
    <View
      sx={{
        display: 'flex',
        flex: 1,
      }}>
      <ScrollView sx={{flex: 1, px: '8%'}}>
        {step === Step.WELCOME ? (
          <Box
            sx={{
              display: 'flex',
              gap: 10,
              mt: '5%',
            }}>
            <Flex
              sx={{
                justifyContent: 'center',
              }}>
              <Image
                source={require('@/assets/images/app-icon.png')}
                sx={{
                  width: 120,
                  height: 120,
                }}
                alt="app icon"
              />
            </Flex>
            <H1
              sx={{
                mb: '10%',
                fontSize: '2xl',
                fontWeight: 'bold',
                textAlign: 'center',
              }}>
              {t('title')}
            </H1>
            <Text sx={{fontSize: 'md', mb: '10%', lineHeight: '24'}}>
              {t('description_1')}
            </Text>
            <Text sx={{fontSize: 'md', lineHeight: '24'}}>
              {t('description_2')}
            </Text>
          </Box>
        ) : (
          <Permissions
            onAllPermissionsGranted={() => setAllPermissionsGranted(true)}
          />
        )}
      </ScrollView>

      <Box
        sx={{
          p: 10,
          borderTop: '1px solid #ccc',
        }}>
        <Button isText isDisabled={!canGoNext()} onPress={handleNext}>
          {t(step === Step.WELCOME ? 'next' : 'start_app')}
        </Button>
      </Box>
    </View>
  );
};

export default Welcome;
