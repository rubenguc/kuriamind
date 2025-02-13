import {useEffect, useState} from 'react';
import {
  Actionsheet,
  ActionsheetBackdrop,
  ActionsheetContent,
  ActionsheetDragIndicator,
  ActionsheetDragIndicatorWrapper,
} from '@/components/ui/actionsheet';
import {Box} from '@/components/ui/box';
import {Button, ButtonText} from '@/components/ui/button';
import {HStack} from '@/components/ui/hstack';
import {Pressable} from '@/components/ui/pressable';
import {Text} from '@/components/ui/text';
import {VStack} from '@/components/ui/vstack';
import {useInstalledApps} from '@/providers';
import {Image} from '@/components/ui/image';
import {ScrollView} from 'react-native';
import {Input, InputField} from '@/components/ui/input';

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
    <Actionsheet isOpen={isOpen} onClose={toggle}>
      <ActionsheetBackdrop />
      <ActionsheetContent>
        <ActionsheetDragIndicatorWrapper>
          <ActionsheetDragIndicator />
        </ActionsheetDragIndicatorWrapper>
        <Box className="mt-3 px-2 w-full">
          <Input variant="outline" size="md">
            <InputField
              placeholder="search..."
              onChangeText={setSearchText}
              value={searchText}
            />
          </Input>
        </Box>
        <ScrollView>
          <Box className="py-6">
            <HStack className="flex-wrap gap-4 justify-center w-full">
              {filteredApps.map(app => (
                <Pressable
                  key={app.packageName}
                  className={`p-2 w-1/5 ${
                    isAppSelected(app.packageName)
                      ? 'bg-gray-500/20 rounded-xl'
                      : 'opacity-50'
                  }`}
                  onPress={() => onSelectApp(app.packageName)}>
                  <VStack className="items-center gap-1">
                    <Image
                      source={{
                        uri: app.icon,
                      }}
                      className="h-10 w-10"
                      alt={app.appName}
                    />
                    <Text numberOfLines={1} className="text-wrap text-sm">
                      {app.appName}
                    </Text>
                  </VStack>
                </Pressable>
              ))}
            </HStack>
          </Box>
        </ScrollView>
        <HStack className="gap-5">
          <Button onPress={toggle} variant="outline">
            <ButtonText>Cancel</ButtonText>
          </Button>
          <Button
            onPress={() => {
              onSave(apps);
            }}>
            <ButtonText>Save</ButtonText>
          </Button>
        </HStack>
      </ActionsheetContent>
    </Actionsheet>
  );
};
