import {Block, BlockToSave} from '@/interfaces';
import {NativeModules} from 'react-native';

const {BlockModule} = NativeModules;

export const fetchBlockData = async (): Promise<Block[]> => {
  const jsonData = await BlockModule.getAllBlocks();
  return JSON.parse(jsonData) as Block[];
};

export const saveBlock = async (block: BlockToSave) => {
  const data = JSON.stringify(block);
  await BlockModule.saveBlock(data);
};

export const updateBlock = async (block: Block) => {
  const data = JSON.stringify(block);
  await BlockModule.updateBlock(data);
};

export const changeBlockStatus = async (blockId: string) => {
  await BlockModule.changeBlockStatus(blockId);
};

export const deleteBlock = async (blockId: string) => {
  await BlockModule.deleteBlock(blockId);
};
