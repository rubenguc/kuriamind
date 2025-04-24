import {useEffect, useState} from 'react';
import {useInstalledApps} from '@/providers';
import {useTranslation} from 'react-i18next';
import {AppItem} from './AppItem';
import {FlatList, Modal as RNModal} from 'react-native';
import {Flex, Text, TextInput, View} from 'dripsy';
import {X} from 'lucide-react-native';
import {Button} from '@/components/ui';

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
          renderItem={({item: app}) => (
            <AppItem
              app={app}
              isSelected={isAppSelected(app.packageName)}
              onSelect={onSelectApp}
            />
          )}
        />
        <View>
          <Button onPress={() => onSave(apps)}>
            <Text>{t('save')}</Text>
          </Button>
        </View>
      </View>
    </RNModal>
  );
};
