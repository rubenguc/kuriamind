import {useEffect, useState} from 'react';
import {Block} from '@/interfaces';
import {deleteBlock, fetchBlockData} from '@/native-modules/block-module';
import {useCustomToast} from '@/hooks';
import {useToggle} from 'react-use';

export const useBlocks = () => {
  const {showSuccessToast, showErrorToast} = useCustomToast();
  const [isLoading, toggleLoading] = useToggle(false);

  const [blocks, setBlocks] = useState<Block[]>([]);

  const [blockToDelete, setBlockToDelete] = useState<Block | null>(null);

  const getBlocks = async () => {
    try {
      const blocks = await fetchBlockData();
      setBlocks(blocks);
    } catch (error) {
      showErrorToast({
        description: 'Error al obtener los bloques',
      });
    }
  };

  useEffect(() => {
    getBlocks();
  }, []);

  const onConfirmDelete = async () => {
    toggleLoading();
    try {
      await deleteBlock(blockToDelete!.id);

      showSuccessToast({
        description: 'Block deleted',
      });

      setBlockToDelete(null);
      await getBlocks();
    } catch (e) {
      showErrorToast({
        description: 'Something went wrong',
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
  };
};
