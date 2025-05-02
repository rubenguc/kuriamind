import {Flex, H1, View} from 'dripsy';
import {FC} from 'react';
import {Modal as RNModal} from 'react-native';
import {Button} from './Button';

interface ModalProps {
  title: string;
  content: JSX.Element;
  isOpen: boolean;
  cancelButtonText?: string;
  acceptButtonText?: string;
  onCancel?: () => void;
  onAccept?: () => void;
}

export const Modal: FC<ModalProps> = ({
  title,
  isOpen,
  content,
  cancelButtonText,
  acceptButtonText,
  onCancel,
  onAccept,
}) => {
  return (
    <RNModal visible={isOpen} transparent>
      <View
        sx={{
          flex: 1,
          alignItems: 'center',
          justifyContent: 'center',
          backgroundColor: '#0008',
        }}>
        <View
          sx={{
            backgroundColor: 'background',
            margin: 10,
            borderRadius: 10,
            px: 20,
            py: 10,
            shadowColor: '#000',
            shadowOffset: {
              width: 0,
              height: 2,
            },
            shadowOpacity: 0.25,
            shadowRadius: 4,
            elevation: 5,
          }}>
          <H1 sx={{fontSize: 'lg'}}>{title}</H1>
          <View
            sx={{
              px: 5,
              mb: 30,
            }}>
            {content}
          </View>
          <Flex sx={{justifyContent: 'space-between'}}>
            {cancelButtonText && onCancel && (
              <Button
                variant="outlined"
                onPress={onCancel}
                isText
                sx={{
                  width: '45%',
                  py: 6,
                }}>
                {cancelButtonText}
              </Button>
            )}

            {acceptButtonText && onAccept && (
              <Button
                onPress={onAccept}
                isText
                sx={{
                  width: '45%',
                  py: 6,
                }}>
                {acceptButtonText}
              </Button>
            )}
          </Flex>
        </View>
      </View>
    </RNModal>
  );
};
