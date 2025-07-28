import {Flex, Text, View} from 'dripsy';
import type {AppUsageStat} from '../interface';
import type {FC} from 'react';
import FastImage from 'react-native-fast-image';
import {useTranslation} from 'react-i18next';

interface UsageItemProps {
  appUsageStat: AppUsageStat;
}

export const UsageItem: FC<UsageItemProps> = ({appUsageStat}) => {
  const {t} = useTranslation('stats');
  const {appBlockCount, appName, icon, notificationBlockCount} = appUsageStat;

  return (
    <View sx={{p: 8}}>
      <Flex
        sx={{
          justifyContent: 'space-between',
          alignItems: 'center',
        }}>
        <Flex
          sx={{
            alignItems: 'center',
            gap: 10,
          }}>
          <FastImage
            source={{
              uri: icon,
              priority: FastImage.priority.high,
              cache: FastImage.cacheControl.immutable,
            }}
            style={{
              width: 50,
              height: 50,
            }}
            resizeMode={FastImage.resizeMode.contain}
          />
          <View sx={{display: 'flex', gap: 2}}>
            <Text sx={{fontWeight: 'bold', fontSize: 'lg'}}>{appName}</Text>
            <Text sx={{color: 'gray'}}>{`${t(
              'blockedCount',
            )}:  ${appBlockCount}`}</Text>
            <Text sx={{color: 'gray'}}>{`${t(
              'blockedNotifications',
            )}: ${notificationBlockCount}`}</Text>
          </View>
        </Flex>
      </Flex>
    </View>
  );
};
