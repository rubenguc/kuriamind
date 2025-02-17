import {Button, ButtonIcon} from '@/components/ui/button';
import {useBlocks} from './hooks';
import {PlusIcon} from 'lucide-react-native';
import {FlatList, ScrollView} from 'react-native';
import {useInstalledApps} from '@/providers';
import {BlockSaved} from './components/BlockSaved';
import {Box} from '@/components/ui/box';
import {ConfirmDeleteBlock} from './components/ConfirmDeleteBlock';
import {Block} from '@/interfaces';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {RootStackParamList, BottomStackParamList} from '@/router';
import {useFocusEffect} from '@react-navigation/native';
import {useCallback} from 'react';

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
        // Limpia el parámetro para que no vuelva a ejecutar el fetch si regresas a Blocks sin necesidad
        navigation.setParams({shouldRefresh: false});
      }
    }, [route.params?.shouldRefresh]),
  );

  return (
    <>
      <Button
        size="lg"
        className="rounded-full p-3 absolute bottom-8 right-8 shadow-xl z-30"
        onPress={() =>
          navigation.navigate('Block', {
            block: undefined,
          })
        }>
        <ButtonIcon as={PlusIcon} />
      </Button>

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
