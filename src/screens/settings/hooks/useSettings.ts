import {useCustomToast} from '@/hooks';
import {Settings} from '@/interfaces';
import {getNativeSettings, setSetting} from '@/native-modules/settings-module';
import {useEffect, useState} from 'react';
import {useTranslation} from 'react-i18next';

export const useSettings = () => {
  const {t} = useTranslation('settings');
  const {showErrorToast} = useCustomToast();

  const [settings, setSettings] = useState<Settings>({
    blockMessage: '',
  });

  const getSettings = async () => {
    try {
      const _settings = await getNativeSettings();
      setSettings(_settings);
    } catch (error) {
      showErrorToast({
        description: t('error_fetching_settings'),
      });
    }
  };

  const updateSettings = (value: any) => {
    setSettings(prev => ({...prev, ...value}));
  };

  const updateBlockMessage = async (value: string) => {
    try {
      await setSetting('blockMessage', value);
      updateSettings({blockMessage: value});
    } catch (error) {
      showErrorToast({
        description: t('error_updating_block_message'),
      });
    }
  };

  useEffect(() => {
    getSettings();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return {
    settings,
    updateSettings,
    updateBlockMessage,
  };
};
