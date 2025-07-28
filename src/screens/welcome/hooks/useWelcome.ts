import NativeLocalStorage from '@/specs/NativeLocalStorage';
import {useState} from 'react';

export enum Step {
  WELCOME = 'Welcome',
  PERMISSIONS = 'Permissions',
}

interface UseWelcomeProps {
  onFinish: () => void;
}

export const useWelcome = ({onFinish}: UseWelcomeProps) => {
  const [step, setStep] = useState(Step.WELCOME);
  const [allPermissionsGranted, setAllPermissionsGranted] = useState(false);

  const handleNext = async () => {
    if (step === Step.WELCOME) {
      return setStep(Step.PERMISSIONS);
    }

    if (allPermissionsGranted) {
      NativeLocalStorage.setItem('isFirstTime', 'false');
      onFinish();
    }
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

    setAllPermissionsGranted,
    canGoNext,
  };
};
