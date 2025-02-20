import {TouchableOpacity} from 'react-native';
import {HStack} from '@/components/ui/hstack';
import {Text} from '@/components/ui/text';

interface InfoOptionProps {
  text: string;
  Icon: JSX.Element;
  onPress: () => void;
}

export const InfoOption = ({text, Icon, onPress}: InfoOptionProps) => {
  return (
    <TouchableOpacity onPress={onPress}>
      <HStack className="gap-3 items-center p-4 border-t border-b border-gray-400">
        {Icon}
        <Text>{text}</Text>
      </HStack>
    </TouchableOpacity>
  );
};
