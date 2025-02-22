import {
  Actionsheet,
  ActionsheetBackdrop,
  ActionsheetContent,
  ActionsheetDragIndicator,
  ActionsheetDragIndicatorWrapper,
  ActionsheetItem,
  ActionsheetItemText,
} from '@/components/ui/actionsheet';
import {HStack} from '@/components/ui/hstack';
import {Text} from '@/components/ui/text';
import {TouchableOpacity} from 'react-native';
import {useToggle} from 'react-use';
import {SettingOptionWrapper} from './SettingOptionWrapper';

interface SelectableOptionSettingProps<T> {
  text: string;
  actualValue: string;
  options: {
    label: string;
    value: T;
  }[];
  onSelected: (value: T) => void;
}

export const SelectableOptionSetting = <T,>({
  text,
  options,
  onSelected,
  actualValue,
}: SelectableOptionSettingProps<T>) => {
  const [isOpen, toggle] = useToggle(false);

  const onOptionSelected = (value: T) => {
    toggle(false);
    onSelected(value);
  };

  return (
    <>
      <SettingOptionWrapper onPress={toggle}>
        <HStack className="items-center justify-between">
          <Text className="text-white">{text}</Text>
          <Text className="text-custom-pink">{actualValue}</Text>
        </HStack>
      </SettingOptionWrapper>

      <Actionsheet isOpen={isOpen} onClose={toggle}>
        <ActionsheetBackdrop />
        <ActionsheetContent>
          <ActionsheetDragIndicatorWrapper>
            <ActionsheetDragIndicator />
          </ActionsheetDragIndicatorWrapper>
          {options.map((option, index) => (
            <ActionsheetItem
              key={index}
              onPress={() => onOptionSelected(option.value)}>
              <ActionsheetItemText>{option.label}</ActionsheetItemText>
            </ActionsheetItem>
          ))}
        </ActionsheetContent>
      </Actionsheet>
    </>
  );
};
