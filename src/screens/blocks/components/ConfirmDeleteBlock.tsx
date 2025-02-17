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
  const {name} = block;

  return (
    <AlertDialog isOpen={isOpen} size="md">
      <AlertDialogBackdrop />
      <AlertDialogContent>
        <AlertDialogHeader>
          <Heading className="text-typography-950 font-semibold" size="md">
            Are you sure you want to delete {name}?
          </Heading>
        </AlertDialogHeader>
        <AlertDialogBody className="mt-3 mb-4">
          <Text size="sm">
            Deleting the post will remove it permanently and cannot be undone.
            Please confirm if you want to proceed.
          </Text>
        </AlertDialogBody>
        <AlertDialogFooter className="">
          <Button
            disabled={isLoading}
            isDisabled={isLoading}
            variant="outline"
            onPress={onClose}
            size="sm">
            <ButtonText>Cancel</ButtonText>
          </Button>
          <Button
            size="sm"
            disabled={isLoading}
            isDisabled={isLoading}
            onPress={onConfirmDelete}>
            <ButtonText>Delete</ButtonText>
          </Button>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
};
