import {ScrollView, View} from 'react-native';
import {Text} from '@/components/ui/text';
import {useTranslation} from 'react-i18next';
import {SelectableOptionSetting} from './components';

const Settings = () => {
  const {t, i18n} = useTranslation('settings');

  return (
    <ScrollView>
      <SelectableOptionSetting
        actualValue={i18n.language}
        options={[
          {label: t('en'), value: 'en'},
          {label: t('es'), value: 'es'},
        ]}
        text={t('language')}
        onSelected={i18n.changeLanguage}
      />
    </ScrollView>
  );
};

export default Settings;
