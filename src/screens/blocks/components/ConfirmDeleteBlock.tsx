import {
  AlertDialog,
  AlertDialogBackdrop,
  AlertDialogBody,
  AlertDialogContent,
  AlertDialogFooter,
  AlertDialogHeader,
} from '@/components/ui/alert-dialog';
import {Button, ButtonText} from '@/components/ui/button';
import {Heading} from '@/components/ui/heading';
import {Text} from '@/components/ui/text';
import {Block} from '@/interfaces';
import {useTranslation} from 'react-i18next';

interface ConfirmDeleteBlockProps {
  block: Block;
  isOpen: boolean;
  onClose: () => void;
  onConfirmDelete: () => void;
  isLoading: boolean;
}

export const ConfirmDeleteBlock = ({
  block,
  isOpen,
  onClose,
  onConfirmDelete,
  isLoading,
}: ConfirmDeleteBlockProps) => {
  const {t} = useTranslation('delete_block');

  const {name} = block;

  return (
    <AlertDialog isOpen={isOpen} size="md">
      <AlertDialogBackdrop />
      <AlertDialogContent>
        <AlertDialogHeader>
          <Heading className="text-typography-950 font-semibold" size="md">
            {t('title')}
          </Heading>
        </AlertDialogHeader>
        <AlertDialogBody className="mt-3 mb-4">
          <Text className="text-lg text-white">{name}</Text>
        </AlertDialogBody>
        <AlertDialogFooter>
          <Button
            disabled={isLoading}
            isDisabled={isLoading}
            variant="outline"
            onPress={onClose}
            size="sm">
            <ButtonText>{t('cancel')}</ButtonText>
          </Button>
          <Button
            size="sm"
            disabled={isLoading}
            isDisabled={isLoading}
            onPress={onConfirmDelete}
            className="bg-custom-green">
            <ButtonText>{t('delete')}</ButtonText>
          </Button>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
};
