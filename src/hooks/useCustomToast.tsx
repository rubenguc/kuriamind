import {
  useToast,
  Toast,
  ToastTitle,
  ToastDescription,
} from '@/components/ui/toast';

interface ToastConfig {
  description: string;
  action: 'success' | 'error';
}

interface Message {
  description: string;
}

export const useCustomToast = () => {
  const toast = useToast();

  const showToast = (config: ToastConfig) => {
    toast.show({
      placement: 'top',
      duration: 3000,
      render: () => (
        <Toast action={config.action} variant="solid">
          <ToastDescription>{config.description}</ToastDescription>
        </Toast>
      ),
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
