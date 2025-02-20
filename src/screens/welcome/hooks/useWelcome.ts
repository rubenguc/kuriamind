import {useState} from 'react';
import {storage} from '@/App';

export enum Step {
  WELCOME = 'Welcome',
  PERMISSIONS = 'Permissions',
}

export const useWelcome = ({navigation}) => {
  const [step, setStep] = useState(Step.WELCOME);
  const [allPermissionsGranted, setAllPermissionsGranted] = useState(false);

  const handleNext = () => {
    if (step === Step.WELCOME) {
      return setStep(Step.PERMISSIONS);
    }

    storage.set('isFirstTime', false);
    navigation.navigate('Home');
  };

  const canGoNext = () => {
    if (step === Step.WELCOME) {
      return true;
    }

    return allPermissionsGranted;
  };

  return {
    step,
    handleNext,
    allPermissionsGranted,
    setAllPermissionsGranted,
    canGoNext,
  };
};
