import {ScrollView} from 'react-native';
import {Button, ButtonText} from '@/components/ui/button';
import {Step, useWelcome} from './hooks/useWelcome';
import {Box} from '@/components/ui/box';
import {Text} from '@/components/ui/text';
import {useTranslation} from 'react-i18next';
import {Heading} from '@/components/ui/heading';
import {Permissions} from './components';

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
          <>
            <Heading className="text-center mb-3 text-3xl">
              {t('title')}
            </Heading>
            <Text>{t('description')}</Text>
          </>
        ) : (
          <Permissions
            onAllPermissionsGranted={() => setAllPermissionsGranted(true)}
          />
        )}
      </ScrollView>
      <Box className="p-4 border-t-black/10 border-t shadow-lg">
        <Button
          className="rounded-2xl"
          size="xl"
          isDisabled={!canGoNext()}
          onPress={handleNext}>
          <ButtonText>Next</ButtonText>
        </Button>
      </Box>
    </Box>
  );
};

export default Welcome;
