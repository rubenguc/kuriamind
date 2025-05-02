import {Modal} from '@/components/ui';
import type {Block} from '@/interfaces';
import {Text} from 'dripsy';
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
    <Modal
      title={t('title')}
      content={<Text>{name}</Text>}
      isOpen={isOpen}
      cancelButtonText={t('cancel')}
      onCancel={onClose}
      acceptButtonText={t('confirm')}
      onAccept={onConfirmDelete}
    />
  );
};
