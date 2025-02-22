import { useCallback } from 'react';
import { FlatList, } from 'react-native';
import {
  CompositeScreenProps,
  useFocusEffect,
} from '@react-navigation/native';
import { useBlocks } from './hooks';
import { useInstalledApps } from '@/providers';
import { BlockSaved } from './components/BlockSaved';
import { Box } from '@/components/ui/box';
import { ConfirmDeleteBlock } from './components/ConfirmDeleteBlock';
import { Block, BottomStackParamList, RootStackParamList } from '@/interfaces';
import type { BottomTabScreenProps } from '@react-navigation/bottom-tabs';
import type { StackScreenProps } from '@react-navigation/stack';


type BlocksProps = CompositeScreenProps<
  BottomTabScreenProps<BottomStackParamList, 'Blocks'>,
  StackScreenProps<RootStackParamList>
>;


export const Blocks = ({ navigation, route }: BlocksProps) => {
  const { installedApps } = useInstalledApps();

  const {
    blocks,
    blockToDelete,
    setBlockToDelete,
    isLoading,
    onConfirmDelete,
    getBlocks,
    onChangeStatus,
  } = useBlocks();

  const onEdit = (block: Block) => {
    navigation.navigate('Block', {
      block,
    });
  };

  useFocusEffect(
    useCallback(() => {
      if (route.params?.shouldRefresh) {
        getBlocks();
        navigation.setParams({ shouldRefresh: false });
      }
    }, [route.params?.shouldRefresh]),
  );

  return (
    <>
      <ConfirmDeleteBlock
        isOpen={!!blockToDelete}
        onClose={() => setBlockToDelete(null)}
        block={blockToDelete || ({} as Block)}
        isLoading={isLoading}
        onConfirmDelete={onConfirmDelete}
      />

      <FlatList
        style={{
          marginTop: 30,
          paddingHorizontal: 20,
        }}
        data={blocks}
        keyExtractor={block => block.id}
        renderItem={({ item }) => (
          <BlockSaved
            block={item}
            allApps={installedApps}
            onEdit={() => onEdit(item)}
            onDelete={() => setBlockToDelete(item)}
            onChangeStatus={() => onChangeStatus(item)}
          />
        )}
        ItemSeparatorComponent={() => <Box className="h-4" />}
      />
    </>
  );
};
