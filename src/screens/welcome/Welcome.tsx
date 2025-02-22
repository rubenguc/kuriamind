import {ScrollView} from 'react-native';
import {Button, ButtonText} from '@/components/ui/button';
import {Step, useWelcome} from './hooks/useWelcome';
import {Box} from '@/components/ui/box';
import {Text} from '@/components/ui/text';
import {useTranslation} from 'react-i18next';
import {Heading} from '@/components/ui/heading';
import {Permissions} from './components';
import {VStack} from '@/components/ui/vstack';

const Welcome = ({navigation}) => {
  const {t} = useTranslation('welcome');

  const {
    allPermissionsGranted,
    handleNext,
    setAllPermissionsGranted,
    step,
    canGoNext,
  } = useWelcome({
    navigation,
  });

  return (
    <Box className="flex h-full">
      <ScrollView className="flex-1 px-10 py-5">
        {step === Step.WELCOME ? (
          <VStack className="mt-10 gap-10">
            <Heading className="text-center mb-3 text-3xl font-bold">
              {t('title')}
            </Heading>
            <Text className="text-white text-lg">{t('description_1')}</Text>
            <Text className="text-white text-lg">{t('description_2')}</Text>
          </VStack>
        ) : (
          <Permissions
            onAllPermissionsGranted={() => setAllPermissionsGranted(true)}
          />
        )}
      </ScrollView>
      <Box className="p-3 border-t-black/10 border-t shadow-lg">
        <Button
          className="rounded-2xl"
          size="xl"
          isDisabled={!canGoNext()}
          onPress={handleNext}>
          <ButtonText>
            {t(step === Step.PERMISSIONS ? 'next' : 'start_apps')}
          </ButtonText>
        </Button>
      </Box>
    </Box>
  );
};

export default Welcome;
