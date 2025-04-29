import {Text, View} from 'dripsy';
import {useTranslation} from 'react-i18next';

export const NoStatsData = () => {
  const {t} = useTranslation('stats');

  return (
    <View sx={{display: 'flex', gap: 5}}>
      <Text>{t('no_data')}</Text>
    </View>
  );
};
