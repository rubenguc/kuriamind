import {useCallback} from 'react';
import {
  type CompositeScreenProps,
  useFocusEffect,
} from '@react-navigation/native';
import {useBlocks} from './hooks';
import {useInstalledApps} from '@/providers';
import type {
  Block,
  BottomStackParamList,
  RootStackParamList,
} from '@/interfaces';
import type {BottomTabScreenProps} from '@react-navigation/bottom-tabs';
import type {StackScreenProps} from '@react-navigation/stack';
import {Flex, Text, useSx} from 'dripsy';
import {ConfirmDeleteBlock} from './components/ConfirmDeleteBlock';
import {Button} from '@/components/ui';
import {useTranslation} from 'react-i18next';
import {Plus} from 'lucide-react-native';
import {BlockItem} from './components/BlockItem';
import {FlatList} from 'react-native';
import {ItemSeparator} from '@/components/shared';

type BlocksProps = CompositeScreenProps<
  BottomTabScreenProps<BottomStackParamList, 'Blocks'>,
  StackScreenProps<RootStackParamList>
>;

export const Blocks = ({navigation, route}: BlocksProps) => {
  const {t} = useTranslation('blocks');
  const sx = useSx();
  const {installedApps} = useInstalledApps();

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
        navigation.setParams({shouldRefresh: false});
      }
    }, [route.params?.shouldRefresh]),
  );

  return (
    <>
      <Flex
        sx={{
          mt: '3%',
          mr: '4%',
          justifyContent: 'flex-end',
          px: 4,
        }}>
        <Button sx={{px: 10}} onPress={() => navigation.navigate('Block')}>
          <Flex sx={{gap: 4}}>
            <Plus size={20} color="white" />
            <Text>{t('add')}</Text>
          </Flex>
        </Button>
      </Flex>

      <FlatList
        onRefresh={getBlocks}
        refreshing={isLoading}
        style={sx({
          marginTop: 20,
          paddingHorizontal: 20,
        })}
        data={blocks}
        renderItem={({item}) => (
          <BlockItem
            block={item}
            allApps={installedApps}
            onEdit={() => onEdit(item)}
            onDelete={() => setBlockToDelete(item)}
            onChangeStatus={() => onChangeStatus(item)}
          />
        )}
        keyExtractor={block => block.id}
        ItemSeparatorComponent={ItemSeparator}
      />

      <ConfirmDeleteBlock
        isOpen={!!blockToDelete}
        onClose={() => setBlockToDelete(null)}
        block={blockToDelete || ({} as Block)}
        isLoading={isLoading}
        onConfirmDelete={onConfirmDelete}
      />
    </>
  );
};
