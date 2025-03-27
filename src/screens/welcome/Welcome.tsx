import {ScrollView} from 'react-native';
import {Button, ButtonText} from '@/components/ui/button';
import {Step, useWelcome} from './hooks/useWelcome';
import {Box} from '@/components/ui/box';
import {Text} from '@/components/ui/text';
import {useTranslation} from 'react-i18next';
import {Heading} from '@/components/ui/heading';
import {Permissions} from './components';
import {VStack} from '@/components/ui/vstack';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {RootStackParamList} from '@/interfaces';
import {HStack} from '@/components/ui/hstack';
import {Image} from '@/components/ui/image';

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
    <Box className="flex h-full">
      <ScrollView className="flex-1 px-10 py-5">
        {step === Step.WELCOME ? (
          <VStack className="gap-10 mt-10">
            <HStack className="justify-center">
              <Image
                source={require('@/assets/images/app-icon.png')}
                size="xl"
                alt="app icon"
              />
            </HStack>
            <Heading className="mb-3 text-3xl font-bold text-center">
              {t('title')}
            </Heading>
            <Text className="text-lg text-white">{t('description_1')}</Text>
            <Text className="text-lg text-white">{t('description_2')}</Text>
          </VStack>
        ) : (
          <Permissions
            onAllPermissionsGranted={() => setAllPermissionsGranted(true)}
          />
        )}
      </ScrollView>
      <Box className="p-3 border-t shadow-lg border-t-black/10">
        <Button
          className="rounded-2xl"
          size="xl"
          isDisabled={!canGoNext()}
          onPress={handleNext}>
          <ButtonText>
            {t(step === Step.WELCOME ? 'next' : 'start_app')}
          </ButtonText>
        </Button>
      </Box>
    </Box>
  );
};

export default Welcome;
