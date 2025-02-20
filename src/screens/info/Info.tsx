import {Text} from '@/components/ui/text';
import {Settings} from 'lucide-react-native';
import {useTranslation} from 'react-i18next';
import {ScrollView, View} from 'react-native';
import {InfoOption} from './components';

const Info = ({navigation}) => {
  const {t} = useTranslation('info');

  const OPTIONS = [
    {
      text: t('settings'),
      icon: <Settings size={24} color="#000" />,
      onPress: () => navigation.navigate('Settings'),
    },
  ];

  return (
    <ScrollView>
      {OPTIONS.map((option, index) => (
        <InfoOption
          key={index}
          text={option.text}
          Icon={option.icon}
          onPress={option.onPress}
        />
      ))}
    </ScrollView>
  );
};

export default Info;
