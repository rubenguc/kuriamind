import {JSX} from 'react';
import {TouchableOpacity} from 'react-native';
import {HStack} from '@/components/ui/hstack';
import {Text} from '@/components/ui/text';
import {VStack} from '@/components/ui/vstack';
import {CircleCheckBig} from 'lucide-react-native';
import {useToggle} from 'react-use';
import {
  Actionsheet,
  ActionsheetBackdrop,
  ActionsheetContent,
  ActionsheetDragIndicator,
  ActionsheetDragIndicatorWrapper,
} from '@/components/ui/actionsheet';
import {Button, ButtonText} from '@/components/ui/button';

interface RequestPermissionOptionWithAcceptProps {
  title: string;
  description: string;
  isActive: boolean;
  onRequestPermission: () => void;
  Icon: JSX.Element;
  acceptTitle: string;
  acceptDescription: string;
  acceptButtonText: string;
  cancelButtonText: string;
}

export const RequestPermissionAcceptOption = ({
  isActive,
  onRequestPermission,
  title,
  Icon,
  description,
  acceptTitle,
  acceptDescription,
  acceptButtonText,
  cancelButtonText,
}: RequestPermissionOptionWithAcceptProps) => {
  const [isOpen, toggleOpen] = useToggle(false);

  const onAccept = () => {
    onRequestPermission();
    toggleOpen();
  };

  return (
    <>
      <TouchableOpacity disabled={isActive} onPress={toggleOpen}>
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
      <Actionsheet isOpen={isOpen}>
        <ActionsheetBackdrop />
        <ActionsheetContent>
          <ActionsheetDragIndicatorWrapper>
            <ActionsheetDragIndicator />
          </ActionsheetDragIndicatorWrapper>
          <VStack>
            <Text className="font-bold text-2xl ">{acceptTitle}</Text>
            <Text className="text-gray-200 py-5">{acceptDescription}</Text>
            <HStack className="justify-between gap-4">
              <Button
                onPress={toggleOpen}
                variant="outline"
                className="bg-gray-500 text-white  rounded-2xl flex-1">
                <ButtonText>{cancelButtonText}</ButtonText>
              </Button>
              <Button
                onPress={onAccept}
                className="bg-custom-green text-white  rounded-2xl flex-1">
                <ButtonText>{acceptButtonText}</ButtonText>
              </Button>
            </HStack>
          </VStack>
        </ActionsheetContent>
      </Actionsheet>
    </>
  );
};
