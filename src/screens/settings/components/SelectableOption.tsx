import {createThemedComponent, Text, useDripsyTheme} from 'dripsy';
import {TouchableOpacity as RNTouchableOpacity} from 'react-native';

const TouchableOpacity = createThemedComponent(RNTouchableOpacity);

interface SelectableOptionProps<T> {
  label: string;
  value: T;
  onPress: (value: T) => void;
  isSelected: boolean;
}

export const SelectableOption = <T,>({
  label,
  value,
  onPress,
  isSelected,
}: SelectableOptionProps<T>) => {
  const {theme} = useDripsyTheme();
  return (
    <TouchableOpacity
      sx={{
        borderRadius: 8,
        px: 10,
        py: 4,
        backgroundColor: isSelected ? theme.colors.primary : 'transparent',
      }}
      onPress={() => onPress(value)}>
      <Text>{label}</Text>
    </TouchableOpacity>
  );
};
