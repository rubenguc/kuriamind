import {useCustomToast} from '@/hooks';
import type {Settings} from '@/interfaces';
import NativeLocalStorage from '@/specs/NativeLocalStorage';
import {useState} from 'react';
import {useTranslation} from 'react-i18next';
import {parseSettings} from '../utils/settings-utils';

export const useSettings = () => {
  const {t} = useTranslation('settings');
  const {showErrorToast} = useCustomToast();

  const [settings, setSettings] = useState<Settings>(
    parseSettings(JSON.parse(NativeLocalStorage.getAll() || '{}')),
  );

  const updateSettings = (value: any) => {
    setSettings(prev => ({...prev, ...value}));
  };

  const updateBlockMessage = async (value: string) => {
    try {
      NativeLocalStorage.setItem('blockMessage', value);
      updateSettings({blockMessage: value});
    } catch (error) {
      showErrorToast({
        description: t('error_updating_block_message'),
      });
    }
  };

  return {
    settings,
    updateSettings,
    updateBlockMessage,
  };
};
