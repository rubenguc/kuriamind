import {Box, Flex, H1, Image, Text} from 'dripsy';
import {useTranslation} from 'react-i18next';

export const WelcomeText = () => {
  const {t} = useTranslation('welcome');

  return (
    <Box
      sx={{
        display: 'flex',
        gap: 10,
        mt: '15%',
      }}>
      <Flex
        sx={{
          justifyContent: 'center',
        }}>
        <Image
          source={require('@/assets/images/app-icon.png')}
          sx={{
            width: 180,
            height: 180,
            objectFit: 'contain',
          }}
          alt="app icon"
        />
      </Flex>
      <Box
        sx={{
          mt: '5%',
          display: 'flex',
          alignItems: 'center',
        }}>
        <Text sx={{color: 'gray', fontSize: 'xl', mb: -20}}>
          {t('welcome_to')}
        </Text>
        <H1
          sx={{
            fontSize: '4xl',
          }}>
          {t('kuria_mind')}
        </H1>
      </Box>
      <Text
        sx={{
          color: 'gray',
          fontSize: 'xl',
          mb: '5%',
          lineHeight: '24',
        }}>
        {t('description_1')}
      </Text>
      <Text
        sx={{
          color: 'gray',
          fontSize: 'xl',
          lineHeight: '24',
        }}>
        {t('description_2')}
      </Text>
    </Box>
  );
};
