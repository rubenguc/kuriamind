import {HStack} from '@/components/ui/hstack';
import {Text} from '@/components/ui/text';
import {VStack} from '@/components/ui/vstack';
import {ChevronRight} from 'lucide-react-native';
import {TouchableOpacity} from 'react-native';

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
        className={`items-center px-3 rounded-xl py-4 ${
          isActive ? 'bg-green-500' : 'bg-gray-400'
        }`}>
        {Icon}
        <VStack className="flex-1 px-4">
          <Text>{title}</Text>
          <Text>{description}</Text>
        </VStack>
        <ChevronRight size={16} />
      </HStack>
    </TouchableOpacity>
  );
};
