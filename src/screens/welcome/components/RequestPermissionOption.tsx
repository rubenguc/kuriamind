import {TouchableOpacity} from 'react-native';
import {HStack} from '@/components/ui/hstack';
import {Text} from '@/components/ui/text';
import {VStack} from '@/components/ui/vstack';
import {CircleCheckBig} from 'lucide-react-native';

interface RequestPermissionOptionProps {
  title: string;
  description: string;
  isActive: boolean;
  onRequestPermission: () => void;
  Icon: JSX.Element;
}

export const RequestPermissionOption = ({
  isActive,
  onRequestPermission,
  title,
  Icon,
  description,
}: RequestPermissionOptionProps) => {
  return (
    <TouchableOpacity disabled={isActive} onPress={onRequestPermission}>
      <HStack
        className={`items-center px-3 rounded-xl py-4 border-2  bg-gray-950 ${
          isActive ? 'border-custom-green' : 'border-gray-500'
        }`}>
        {Icon}
        <VStack className="flex-1 px-4">
          <Text className="text-white font-bold mb-2">{title}</Text>
          <Text>{description}</Text>
        </VStack>
        <CircleCheckBig size={22} color={isActive ? '#9bec8f' : '#888'} />
      </HStack>
    </TouchableOpacity>
  );
};
