import {useState} from 'react';
import {storage} from '@/App';

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

  const handleNext = () => {
    if (step === Step.WELCOME) {
      return setStep(Step.PERMISSIONS);
    }

    if (allPermissionsGranted) {
      storage.set('isFirstTime', false);
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
