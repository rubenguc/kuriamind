import {useTranslation} from 'react-i18next';
import {InputOptionSetting, SelectableOptionSetting} from './components';
import {useSettings} from './hooks';
import {Languages, MessageSquareMore} from 'lucide-react-native';
import {ScrollView} from 'dripsy';

const Settings = () => {
  const {t, i18n} = useTranslation('settings');
  const {settings, updateBlockMessage} = useSettings();

  return (
    <ScrollView
      sx={{
        px: '5%',
      }}>
      <SelectableOptionSetting
        value={t(i18n.language)}
        options={[
          {label: t('en'), value: 'en'},
          {label: t('es'), value: 'es'},
        ]}
        text={t('language')}
        onSelected={i18n.changeLanguage}
        Icon={<Languages size={20} color="#bbb" />}
      />

      <InputOptionSetting
        value={settings.blockMessage}
        text={t('block_message')}
        onSubmit={updateBlockMessage}
        Icon={<MessageSquareMore size={20} color="#bbb" />}
      />
    </ScrollView>
  );
};

export default Settings;
