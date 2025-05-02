import {useToggle} from 'react-use';
import {SettingOption} from './SettingOption';
import type {SettingOptionProps} from '../interface';
import {Actionsheet} from '@/components/ui';
import {View} from 'dripsy';
import {SelectableOption} from './SelectableOption';

interface SelectableOptionSettingProps<T>
  extends Omit<SettingOptionProps, 'onPress'> {
  options: {
    label: string;
    value: T;
  }[];
  onSelected: (value: T) => void;
}

export const SelectableOptionSetting = <T,>({
  Icon,
  value,
  text,
  options,
  onSelected,
}: SelectableOptionSettingProps<T>) => {
  const [isOpen, toggle] = useToggle(false);

  const onOptionSelected = (newValue: T) => {
    toggle(false);
    onSelected(newValue);
  };

  return (
    <>
      <SettingOption onPress={toggle} Icon={Icon} text={text} value={value} />
      <Actionsheet
        isOpen={isOpen}
        title={text}
        onClose={() => toggle(false)}
        content={
          <View sx={{display: 'flex', gap: 10}}>
            {options.map(option => (
              <SelectableOption
                key={option.value}
                isSelected={option.label === value}
                label={option.label}
                value={option.value}
                onPress={onOptionSelected}
              />
            ))}
          </View>
        }
      />
    </>
  );
};
