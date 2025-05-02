import {JSX} from 'react';
import {useToggle} from 'react-use';
import {Text} from 'dripsy';
import {RequestPermissionOption} from './RequestPermissionOption';
import {Actionsheet} from '@/components/ui';

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
      <RequestPermissionOption
        Icon={Icon}
        description={description}
        isActive={isActive}
        title={title}
        onRequestPermission={toggleOpen}
      />
      <Actionsheet
        title={acceptTitle}
        content={<Text>{acceptDescription}</Text>}
        cancelButtonText={cancelButtonText}
        onCancel={toggleOpen}
        onAccept={onAccept}
        acceptButtonText={acceptButtonText}
        isOpen={isOpen}
      />
    </>
  );
};
