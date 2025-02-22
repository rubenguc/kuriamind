import {PropsWithChildren} from 'react';
import {TouchableOpacity} from 'react-native';
import {Box} from '@/components/ui/box';

interface SettingOptionWrapperProps {
  onPress: () => void;
}

export const SettingOptionWrapper = ({
  children,
  onPress,
}: PropsWithChildren<SettingOptionWrapperProps>) => {
  return (
    <TouchableOpacity activeOpacity={0.9} onPress={onPress}>
      <Box className="bg-gray-950 px-2 py-3 rounded-xl">{children}</Box>
    </TouchableOpacity>
  );
};
