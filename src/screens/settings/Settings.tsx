import {ScrollView} from 'react-native';
import {useTranslation} from 'react-i18next';
import {InputOptionSetting, SelectableOptionSetting} from './components';
import {useSettings} from './hooks';

const Settings = () => {
  const {t, i18n} = useTranslation('settings');

  const {settings, updateBlockMessage} = useSettings();

  return (
    <ScrollView className="px-5 py-8">
      <SelectableOptionSetting
        actualValue={t(i18n.language)}
        options={[
          {label: t('en'), value: 'en'},
          {label: t('es'), value: 'es'},
        ]}
        text={t('language')}
        onSelected={i18n.changeLanguage}
      />

      <InputOptionSetting
        actualValue={settings.blockMessage}
        text={t('block_message')}
        onSubmit={updateBlockMessage}
      />
    </ScrollView>
  );
};

export default Settings;
