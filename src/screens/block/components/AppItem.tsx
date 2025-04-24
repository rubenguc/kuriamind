import {InstalledApp} from '@/interfaces';
import {Flex, Text, useDripsyTheme, useSx} from 'dripsy';
import {TouchableOpacity} from 'react-native';
import {AdvancedCheckbox} from 'react-native-advanced-checkbox';
import FastImage from 'react-native-fast-image';

interface AppsToSelectProps {
  app: InstalledApp;
  isSelected: boolean;
  onSelect: (packageName: string) => void;
}

export const AppItem = ({app, isSelected, onSelect}: AppsToSelectProps) => {
  const {theme} = useDripsyTheme();
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
        <AdvancedCheckbox
          size={18}
          value={isSelected}
          checkedColor={theme.colors.accent}
        />
      </Flex>
    </TouchableOpacity>
  );
};
