import {useState} from 'react';
import {useToggle} from 'react-use';
import {SettingOption} from './SettingOption';
import {useTranslation} from 'react-i18next';
import {P, TextInput} from 'dripsy';
import type {SettingOptionProps} from '../interface';
import {Modal} from '@/components/ui';

interface InputOptionSettingProps<T>
  extends Omit<SettingOptionProps, 'onPress'> {
  onSubmit: (value: T) => Promise<void>;
  isNotEmpty?: boolean;
}

export const InputOptionSetting = ({
  value = '',
  onSubmit,
  text,
  isNotEmpty = false,
  Icon,
}: InputOptionSettingProps<string>) => {
  const {t} = useTranslation('settings');
  const [isToggled, toggle] = useToggle(false);
  const [inputText, setInputText] = useState(value);

  const onTextSubmit = async () => {
    toggle();
    onSubmit(inputText);
  };

  return (
    <>
      <SettingOption onPress={toggle} text={text} value={value} Icon={Icon} />
      <Modal
        isOpen={isToggled}
        title={text}
        content={
          <>
            <P>{t('block_message_description')}</P>
            <TextInput
              value={inputText}
              onChangeText={setInputText}
              sx={{
                borderWidth: 1,
                borderColor: 'white',
                borderRadius: 8,
              }}
            />
          </>
        }
        cancelButtonText={t('cancel')}
        acceptButtonText={t('modify')}
        onCancel={toggle}
        onAccept={onTextSubmit}
      />
    </>
  );
};
