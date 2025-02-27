import { useEffect, useState } from 'react';
import {
  Actionsheet,
  ActionsheetBackdrop,
  ActionsheetContent,
  ActionsheetDragIndicator,
  ActionsheetDragIndicatorWrapper,
} from '@/components/ui/actionsheet';
import { Box } from '@/components/ui/box';
import { Button, ButtonText } from '@/components/ui/button';
import { HStack } from '@/components/ui/hstack';
import { useInstalledApps } from '@/providers';
import { FlatList } from 'react-native';
import { Input, InputField } from '@/components/ui/input';
import { useTranslation } from 'react-i18next';
import { AppItem } from './AppItem';

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
  const { t } = useTranslation('block');
  const { installedApps } = useInstalledApps();

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
    <Actionsheet isOpen={isOpen} onClose={toggle} snapPoints={[80]}>
      <ActionsheetBackdrop />
      <ActionsheetContent className="flex flex-col w-full h-full ">
        <ActionsheetDragIndicatorWrapper>
          <ActionsheetDragIndicator />
        </ActionsheetDragIndicatorWrapper>
        <Box className="w-full px-2 mt-3">
          <Input variant="outline" size="md">
            <InputField
              placeholder={t('search_placeholder')}
              onChangeText={setSearchText}
              value={searchText}
            />
          </Input>
        </Box>
        <Box className="w-full  h-[70%]">
          <FlatList
            data={filteredApps}
            keyExtractor={(item) => item.packageName}
            numColumns={5}
            columnWrapperStyle={{ justifyContent: 'center' }}
            contentContainerStyle={{ paddingVertical: 24 }}
            initialNumToRender={20}
            maxToRenderPerBatch={10}
            windowSize={7}
            removeClippedSubviews={true}
            renderItem={({ item: app }) => <AppItem
              app={app}
              isSelected={isAppSelected(app.packageName)}
              onSelect={onSelectApp}
            />}
          />
        </Box>
        <HStack className="gap-5 py-5">
          <Button onPress={toggle} variant="outline">
            <ButtonText>{t('cancel')}</ButtonText>
          </Button>
          <Button
            onPress={() => {
              onSave(apps);
            }}>
            <ButtonText>{t('save')}</ButtonText>
          </Button>
        </HStack>
      </ActionsheetContent>
    </Actionsheet>
  );
};
