import {useEffect, useState} from 'react';
import {Block} from '@/interfaces';
import {
  changeBlockStatus,
  deleteBlock,
  fetchBlockData,
} from '@/native-modules/block-module';
import {useCustomToast} from '@/hooks';
import {useToggle} from 'react-use';
import {useTranslation} from 'react-i18next';

export const useBlocks = () => {
  const {t} = useTranslation('blocks');
  const {showSuccessToast, showErrorToast} = useCustomToast();
  const [isLoading, toggleLoading] = useToggle(false);

  const [blocks, setBlocks] = useState<Block[]>([]);
  const [blockToDelete, setBlockToDelete] = useState<Block | null>(null);

  const getBlocks = async () => {
    try {
      const blocksData = await fetchBlockData();
      setBlocks(blocksData);
    } catch (error) {
      showErrorToast({
        description: t('error_fetching_blocks'),
      });
    }
  };

  useEffect(() => {
    getBlocks();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const onConfirmDelete = async () => {
    toggleLoading();
    try {
      await deleteBlock(blockToDelete!.id);

      showSuccessToast({
        description: t('block_deleted_successfully'),
      });

      setBlockToDelete(null);
      await getBlocks();
    } catch (e) {
      showErrorToast({
        description: t('block_deleted_error'),
      });
    }
    toggleLoading();
  };

  const onChangeStatus = async (block: Block) => {
    toggleLoading();
    const isActive = block.isActive;
    try {
      await changeBlockStatus(block.id);

      showSuccessToast({
        description: t(
          isActive
            ? 'block_disabled_successfully'
            : 'block_activated_successfully',
        ),
      });

      await getBlocks();
    } catch (e) {
      showErrorToast({
        description: t(
          isActive ? 'block_disabled_error' : 'block_activated_error',
        ),
      });
    }
    toggleLoading();
  };

  return {
    blocks,
    blockToDelete,
    setBlockToDelete,
    onConfirmDelete,
    isLoading,
    getBlocks,
    onChangeStatus,
  };
};
