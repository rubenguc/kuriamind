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
      <TouchableOpacity onPress={toggle}>
        <HStack className="items-center justify-between">
          <Text>{text}</Text>
          <Text>{actualValue}</Text>
        </HStack>
      </TouchableOpacity>

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
