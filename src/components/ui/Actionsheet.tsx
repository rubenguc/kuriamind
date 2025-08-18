import {FC, useEffect, useRef} from 'react';
import {Flex, Text, View} from 'dripsy';
import {
  ActionSheetRef,
  default as RNActionSheet,
} from 'react-native-actions-sheet';
import {Button} from './Button';

interface ActionsheetProps {
  isOpen: boolean;
  title: string;
  content: JSX.Element;
  cancelButtonText?: string;
  acceptButtonText?: string;
  onCancel?: () => void;
  onAccept?: () => void;
  onClose?: () => void;
}

export const Actionsheet: FC<ActionsheetProps> = ({
  acceptButtonText,
  cancelButtonText,
  content,
  isOpen,
  onAccept,
  onCancel,
  title,
  onClose,
}) => {
  const actionSheetRef = useRef<ActionSheetRef>(null);

  useEffect(() => {
    if (isOpen) {
      actionSheetRef.current?.show();
    } else {
      actionSheetRef.current?.hide();
    }
  }, [isOpen]);

  return (
    <RNActionSheet ref={actionSheetRef} onClose={onClose}>
      <View sx={{backgroundColor: '#111', px: '10%', py: '5%'}}>
        <Flex sx={{flexDirection: 'column'}}>
          <Text
            sx={{
              fontWeight: 'bold',
              fontSize: '2xl',
              mb: '4%',
              borderBottomWidth: 1,
              borderBottomColor: '#7777',
            }}>
            {title}
          </Text>
          <View sx={{mb: '5%'}}>{content}</View>
          <Flex sx={{justifyContent: 'space-between'}}>
            {cancelButtonText && onCancel && (
              <Button
                variant="outlined"
                onPress={onCancel}
                isText
                sx={{
                  width: '45%',
                  py: 5,
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
                  py: 5,
                }}>
                {acceptButtonText}
              </Button>
            )}
          </Flex>
        </Flex>
      </View>
    </RNActionSheet>
  );
};
