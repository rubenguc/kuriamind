import {useToggle} from 'react-use';
import {SettingOptionWrapper} from './SettingOptionWrapper';
import {HStack} from '@/components/ui/hstack';
import {Text} from '@/components/ui/text';
import {
  Modal,
  ModalBackdrop,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
} from '@/components/ui/modal';
import {Heading} from '@/components/ui/heading';
import {X} from 'lucide-react-native';
import {Button, ButtonText} from '@/components/ui/button';
import {useState} from 'react';
import {Input, InputField} from '@/components/ui/input';
import {useTranslation} from 'react-i18next';
import {
  AlertDialog,
  AlertDialogBackdrop,
  AlertDialogBody,
  AlertDialogContent,
  AlertDialogFooter,
  AlertDialogHeader,
} from '@/components/ui/alert-dialog';

interface InputOptionSettingProps<T> {
  text: string;
  actualValue: string;
  onSubmit: (value: T) => Promise<void>;
  isNotEmpty?: boolean;
}

export const InputOptionSetting = ({
  actualValue = '',
  onSubmit,
  text,
  isNotEmpty = false,
}: InputOptionSettingProps<string>) => {
  const {t} = useTranslation('settings');
  const [isToggled, toggle] = useToggle(false);
  const [inputText, setInputText] = useState(actualValue);

  const onTextSubmit = async () => {
    // Implement your logic here
    toggle();
    onSubmit(inputText);
  };

  const isValidText = () => {
    if (isNotEmpty) return inputText.trim().length > 0;
    return true;
  };

  return (
    <>
      <SettingOptionWrapper onPress={toggle}>
        <HStack className="items-center justify-between">
          <Text className="text-white">{text}</Text>
          <Text className="text-custom-pink">{actualValue}</Text>
        </HStack>
      </SettingOptionWrapper>

      <AlertDialog
        isOpen={isToggled}
        onClose={() => {
          toggle(false);
        }}
        size="md">
        <AlertDialogBackdrop />
        <AlertDialogContent>
          <AlertDialogHeader>
            <Heading size="md" className="text-typography-950">
              {text}
            </Heading>
            <ModalCloseButton>
              <X className="stroke-background-400 group-[:hover]/modal-close-button:stroke-background-700 group-[:active]/modal-close-button:stroke-background-900 group-[:focus-visible]/modal-close-button:stroke-background-900" />
            </ModalCloseButton>
          </AlertDialogHeader>
          <AlertDialogBody className="py-5">
            <Input variant="outline" size="md" isInvalid={!isValidText()}>
              <InputField
                placeholder="name"
                onChangeText={setInputText}
                value={inputText}
              />
            </Input>
          </AlertDialogBody>

          <AlertDialogFooter>
            <Button isDisabled={!isValidText()} onPress={onTextSubmit}>
              <ButtonText>{t('modify')}</ButtonText>
            </Button>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
};
