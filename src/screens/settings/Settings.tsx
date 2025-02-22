import { ScrollView } from 'react-native';
import { useTranslation } from 'react-i18next';
import { SelectableOptionSetting } from './components';

const Settings = () => {
  const { t, i18n } = useTranslation('settings');

  return (
    <ScrollView className="px-5 py-8">
      <SelectableOptionSetting
        actualValue={t(i18n.language)}
        options={[
          { label: t('en'), value: 'en' },
          { label: t('es'), value: 'es' },
        ]}
        text={t('language')}
        onSelected={i18n.changeLanguage}
      />
    </ScrollView>
  );
};

export default Settings;
