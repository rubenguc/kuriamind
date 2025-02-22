import {useCallback} from 'react';
import {FlatList, Linking, ScrollView} from 'react-native';
import {useFocusEffect} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {useBlocks} from './hooks';
import {useInstalledApps} from '@/providers';
import {BlockSaved} from './components/BlockSaved';
import {Box} from '@/components/ui/box';
import {ConfirmDeleteBlock} from './components/ConfirmDeleteBlock';
import {Block} from '@/interfaces';
import {BottomStackParamList} from '@/router';

interface BlocksProps
  extends NativeStackScreenProps<BottomStackParamList, 'Blocks'> {}

export const Blocks = ({navigation, route}: BlocksProps) => {
  const {installedApps} = useInstalledApps();

  const {
    blocks,
    blockToDelete,
    setBlockToDelete,
    isLoading,
    onConfirmDelete,
    getBlocks,
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
        navigation.setParams({shouldRefresh: false});
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
        renderItem={({item}) => (
          <BlockSaved
            block={item}
            allApps={installedApps}
            onEdit={() => onEdit(item)}
            onDelete={() => setBlockToDelete(item)}
          />
        )}
        ItemSeparatorComponent={() => <Box className="h-4" />}
      />
    </>
  );
};
