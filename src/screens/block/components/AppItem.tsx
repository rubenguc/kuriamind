import { Image } from '@/components/ui/image';
import { Text } from '@/components/ui/text';
import { VStack } from '@/components/ui/vstack';
import { InstalledApp } from '@/interfaces';
import { memo } from 'react';
import { Pressable, StyleSheet } from 'react-native';
import FastImage from 'react-native-fast-image';

interface AppsToSelectProps {
  app: InstalledApp;
  isSelected: boolean;
  onSelect: (packageName: string) => void;
}

export const AppItem = memo(
  ({ app, isSelected, onSelect }: AppsToSelectProps) => {
    return (
      <Pressable
        style={itemStyle(isSelected).container}
        onPress={() => onSelect(app.packageName)}>
        <VStack className="items-center gap-2">
          <FastImage
            source={{
              uri: app.icon,
              priority: FastImage.priority.high,
              cache: FastImage.cacheControl.immutable,
            }}
            style={{
              width: 50,
              height: 50
            }}
            resizeMode={FastImage.resizeMode.contain}
          />
          <Text
            numberOfLines={1}
            className="text-sm text-white dark:text-white text-wrap">
            {app.appName}
          </Text>
        </VStack>
      </Pressable>
    );
  },
);

const itemStyle = (isSelected: boolean) =>
  StyleSheet.create({
    container: {
      padding: 8,
      width: '20%',
      opacity: isSelected ? 1 : 0.5,
      backgroundColor: isSelected ? 'rgba(107, 114, 128, 0.2)' : 'transparent',
      borderRadius: 12,
    },
  });
