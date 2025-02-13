import {ScrollView} from 'react-native';
import {Controller} from 'react-hook-form';
import {useBlock} from './hooks';
import {Box} from '@/components/ui/box';
import {Input, InputField} from '@/components/ui/input';
import {Text} from '@/components/ui/text';
import {Button, ButtonIcon, ButtonText} from '@/components/ui/button';
import {
  Checkbox,
  CheckboxIcon,
  CheckboxIndicator,
  CheckboxLabel,
} from '@/components/ui/checkbox';
import {CheckIcon, PlusIcon} from 'lucide-react-native';
import {VStack} from '@/components/ui/vstack';
import {useToggle} from 'react-use';
import {AppsToSelect} from './components/AppsToSelect';
import {getSelectedApps} from '@/utils/block-utils';
import {useInstalledApps} from '@/providers';
import {HStack} from '@/components/ui/hstack';
import {Image} from '@/components/ui/image';
import {SelectedAppsInBlock} from '@/components/shared';

export const Block = () => {
  const {installedApps} = useInstalledApps();
  const {control, onSubmit, handleSubmit, errors} = useBlock();

  const [isOpen, toggleOpen] = useToggle(false);

  return (
    <ScrollView>
      <Box className="p-10 gap-6">
        <Controller
          control={control}
          render={({field: {onChange, onBlur, value}}) => (
            <VStack>
              <Text>Name</Text>
              <Input variant="outline" size="md" isInvalid={!!errors.name}>
                <InputField
                  placeholder="name"
                  onChangeText={onChange}
                  onBlur={onBlur}
                  value={value}
                />
              </Input>
              {errors.name && <Text>{errors.name.message}</Text>}
            </VStack>
          )}
          name="name"
        />

        <Controller
          control={control}
          render={({field: {onChange, value}}) => (
            <VStack>
              <Button className="w-32 rounded-2xl" onPress={toggleOpen}>
                <ButtonIcon as={PlusIcon} />
                <ButtonText>Add apps</ButtonText>
              </Button>
              <AppsToSelect
                selectedApps={value}
                isOpen={isOpen}
                onSave={packageNames => {
                  onChange(packageNames);
                  toggleOpen();
                }}
                toggle={toggleOpen}
              />
              <Box className="mt-2 p-2 border border-gray-600/20 rounded-xl">
                {value?.length === 0 ? (
                  <Text>No apps selected</Text>
                ) : (
                  <SelectedAppsInBlock
                    packageNames={value}
                    installedApps={installedApps}
                  />
                )}
              </Box>
              {errors.name && <Text>{errors.name.message}</Text>}
            </VStack>
          )}
          name="blockedApps"
        />

        <Controller
          control={control}
          render={({field: {onChange, value}}) => (
            <Checkbox
              isInvalid={false}
              isDisabled={false}
              value={value?.toString()}
              onChange={onChange}
              isChecked={value}>
              <CheckboxIndicator>
                <CheckboxIcon as={CheckIcon} />
              </CheckboxIndicator>
              <CheckboxLabel>Blocks apps</CheckboxLabel>
            </Checkbox>
          )}
          name="blockApps"
        />

        <Controller
          control={control}
          render={({field: {onChange, value}}) => (
            <Checkbox
              isInvalid={false}
              isDisabled={false}
              value={value?.toString()}
              onChange={onChange}
              isChecked={value}>
              <CheckboxIndicator>
                <CheckboxIcon as={CheckIcon} />
              </CheckboxIndicator>
              <CheckboxLabel>Blocks Notifications</CheckboxLabel>
            </Checkbox>
          )}
          name="blockNotifications"
        />

        <Button className="top-10 rounded-2xl" onPress={onSubmit}>
          <ButtonText>Create block</ButtonText>
        </Button>
      </Box>
    </ScrollView>
  );
};
