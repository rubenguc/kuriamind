import {Flex, useDripsyTheme, useSx} from 'dripsy';
import {FlatList} from 'react-native';
import {useStats} from './hooks';
import {NoStatsData, UsageItem} from './components';
import {ItemSeparator} from '@/components/shared';
import {Dropdown} from 'react-native-element-dropdown';

export const Stats = () => {
  const {theme} = useDripsyTheme();
  const sx = useSx();
  const {
    statFilter,
    appUsageStats,
    setStatFilter,
    OPTIONS,
    thereIsNoData,
    getStats,
    isLoading,
  } = useStats();

  return (
    <>
      <Flex
        sx={{
          justifyContent: 'flex-end',
          px: 4,
        }}>
        <Dropdown
          value={statFilter}
          containerStyle={sx({
            backgroundColor: '#222',
            color: 'white',
            borderColor: theme.colors.primary,
          })}
          activeColor="#333"
          itemTextStyle={sx({
            color: 'white',
          })}
          inputSearchStyle={sx({
            color: theme.colors.primary,
          })}
          selectedTextStyle={sx({
            color: 'white',
          })}
          style={sx({
            backgroundColor: '#222',
            width: '50%',
            borderColor: theme.colors.primary,
            borderWidth: 1,
            borderRadius: 8,
            height: 50,
            px: 10,
          })}
          iconColor={theme.colors.primary}
          labelField="label"
          valueField="value"
          data={OPTIONS}
          onChange={item => setStatFilter(item.value)}
        />
      </Flex>

      <FlatList
        refreshing={isLoading}
        onRefresh={getStats}
        style={sx({
          marginTop: 30,
          paddingHorizontal: 20,
        })}
        // eslint-disable-next-line react/no-unstable-nested-components
        ListEmptyComponent={() => thereIsNoData && <NoStatsData />}
        data={appUsageStats}
        renderItem={({item}) => <UsageItem appUsageStat={item} />}
        keyExtractor={app => app.appName}
        ItemSeparatorComponent={ItemSeparator}
      />
    </>
  );
};
