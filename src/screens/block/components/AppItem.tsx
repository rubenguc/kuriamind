import type {InstalledApp} from '@/interfaces';
import {Flex, Text, useSx} from 'dripsy';
import {TouchableOpacity} from 'react-native';
import CheckBox from '@react-native-community/checkbox';
import FastImage from 'react-native-fast-image';
import {memo} from 'react';

interface AppsToSelectProps {
  app: InstalledApp;
  isSelected: boolean;
  onSelect: (packageName: string) => void;
}

export const AppItem = memo(
  ({app, isSelected, onSelect}: AppsToSelectProps) => {
    const sx = useSx();

    return (
      <TouchableOpacity
        activeOpacity={0.8}
        onPress={() => onSelect(app.packageName)}
        style={sx({
          p: 8,
        })}>
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
                uri: app.icon,
                priority: FastImage.priority.high,
                cache: FastImage.cacheControl.immutable,
              }}
              style={{
                width: 40,
                height: 40,
              }}
              resizeMode={FastImage.resizeMode.contain}
            />
            <Text>{app.appName}</Text>
          </Flex>
          <CheckBox
            value={isSelected}
            onValueChange={newValue => onSelect(app.packageName)}
          />
        </Flex>
      </TouchableOpacity>
    );
  },
  (prevProps, nextProps) => {
    return (
      prevProps.isSelected === nextProps.isSelected &&
      prevProps.app.packageName === nextProps.app.packageName
    );
  },
);
