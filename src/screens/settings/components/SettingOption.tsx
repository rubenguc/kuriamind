import {PropsWithChildren} from 'react';
import {TouchableOpacity} from 'react-native';
import {Flex, Text} from 'dripsy';
import {ChevronRight} from 'lucide-react-native';
import {SettingOptionProps} from '../interface';

export const SettingOption = ({
  Icon,
  value,
  text,
  onPress,
}: PropsWithChildren<SettingOptionProps>) => {
  return (
    <TouchableOpacity activeOpacity={0.9} onPress={onPress}>
      <Flex
        sx={{
          alignItems: 'center',
          justifyContent: 'space-between',
          borderBottomWidth: 1,
          px: 4,
          py: 20,
          borderBottomColor: '#7777',
        }}>
        <Flex
          sx={{
            alignItems: 'center',
            gap: 8,
          }}>
          {Icon}
          <Text
            sx={{
              fontWeight: 'bold',
            }}>
            {text}
          </Text>
        </Flex>
        <Flex
          sx={{
            alignItems: 'center',
            justifyContent: 'flex-end',
            gap: 8,
          }}>
          <Text
            sx={{
              color: 'primary',
            }}>
            {value}
          </Text>

          <ChevronRight color="#bbb" />
        </Flex>
      </Flex>
    </TouchableOpacity>
  );
};
