import {toast, ToastPosition} from '@backpackapp-io/react-native-toast';
import {useDripsyTheme} from 'dripsy';

interface ToastConfig {
  description: string;
  action: 'success' | 'error';
}

interface Message {
  description: string;
}

export const useCustomToast = () => {
  const {theme} = useDripsyTheme();

  const showToast = (config: ToastConfig) => {
    toast[config.action](config.description, {
      position: ToastPosition.TOP,
      styles: {
        view: {
          borderWidth: 1,
          borderColor:
            config.action === 'success' ? theme.colors.accent : 'red',
          borderRadius: 12,
        },
      },
    });
  };

  const showSuccessToast = ({description}: Message) => {
    showToast({
      description,
      action: 'success',
    });
  };

  const showErrorToast = ({description}: Message) => {
    showToast({
      description,
      action: 'error',
    });
  };

  return {
    showSuccessToast,
    showErrorToast,
  };
};
