import {Text, View} from 'dripsy';
import {useTranslation} from 'react-i18next';

export const NoStatsData = () => {
  const {t} = useTranslation('stats');

  return (
    <View
      sx={{
        display: 'flex',
      }}>
      <Text sx={{textAlign: 'center', color: '#ddd'}}>{t('no_data')}</Text>
    </View>
  );
};
