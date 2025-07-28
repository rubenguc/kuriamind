import {useCallback, useEffect, useState} from 'react';
import {useInstalledApps} from '@/providers';
import {useTranslation} from 'react-i18next';
import {AppItem} from './AppItem';
import {FlatList, Modal as RNModal} from 'react-native';
import {Flex, TextInput, View} from 'dripsy';
import {X} from 'lucide-react-native';
import {Button} from '@/components/ui';
import {InstalledApp} from '@/interfaces';

interface AppsToSelectProps {
  isOpen: boolean;
  toggle: () => void;
  selectedApps: string[];
  onSave: (apps: string[]) => void;
}

export const AppsToSelect = ({
  isOpen,
  onSave,
  toggle,
  selectedApps,
}: AppsToSelectProps) => {
  const {t} = useTranslation('block');
  const {installedApps} = useInstalledApps();

  const [apps, setApps] = useState<string[]>([]);
  const [searchText, setSearchText] = useState('');

  const onSelectApp = (packageName: string) => {
    setApps(prev =>
      isAppSelected(packageName)
        ? prev.filter(item => item !== packageName)
        : [...prev, packageName],
    );
  };

  const isAppSelected = (packageName: string) => {
    return apps.includes(packageName);
  };

  const filteredApps = installedApps.filter(app =>
    app.appName.toLowerCase().includes(searchText.trim().toLowerCase()),
  );

  useEffect(() => {
    setApps(selectedApps);
  }, [selectedApps]);

  const renderItem = useCallback(
    ({item: app}: {item: InstalledApp}) => (
      <AppItem
        app={app}
        isSelected={apps.includes(app.packageName)}
        onSelect={onSelectApp}
      />
    ),
    [apps, onSelectApp],
  );

  return (
    <RNModal visible={isOpen} transparent animationType="slide">
      <View
        sx={{
          backgroundColor: 'background',
          flex: 1,
          borderTopLeftRadius: 12,
          borderTopRightRadius: 12,
        }}>
        <Flex
          sx={{
            px: 2,
            py: 8,
            justifyContent: 'flex-end',
          }}>
          <X color="#fff" onPress={toggle} />
        </Flex>
        <View>
          <TextInput value={searchText} onChangeText={setSearchText} />
        </View>
        <FlatList
          data={filteredApps}
          keyExtractor={item => item.packageName}
          renderItem={renderItem}
        />
        <View
          sx={{
            p: 10,
          }}>
          <Button onPress={() => onSave(apps)} sx={{py: 10}}>
            {t('save')}
          </Button>
        </View>
      </View>
    </RNModal>
  );
};
