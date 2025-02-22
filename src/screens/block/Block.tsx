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
import {useInstalledApps} from '@/providers';
import {SelectedAppsInBlock} from '@/components/shared';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {RootStackParamList} from '@/router';
import {useTranslation} from 'react-i18next';

interface BlocksProps
  extends NativeStackScreenProps<RootStackParamList, 'Block'> {}

export const Block = ({route, navigation}: BlocksProps) => {
  const {t} = useTranslation('block');
  const {installedApps} = useInstalledApps();
  const {control, onSubmit, handleSubmit, errors, isEditing} = useBlock({
    defaultBlock: route.params.block,
    onFinishSubmit: () =>
      navigation.navigate('Home', {
        screen: 'Blocks',
        params: {
          shouldRefresh: true,
        },
      }),
  });

  const [isOpen, toggleOpen] = useToggle(false);

  return (
    <Box className="flex h-full">
      <ScrollView className="flex-1 px-10 py-5">
        <Box className="gap-6">
          <Controller
            control={control}
            render={({field: {onChange, onBlur, value}}) => (
              <VStack>
                <Text>{t('name')}</Text>
                <Input variant="outline" size="md" isInvalid={!!errors.name}>
                  <InputField
                    placeholder="name"
                    onChangeText={onChange}
                    onBlur={onBlur}
                    value={value}
                  />
                </Input>
                {errors.name && (
                  <Text className="text-error-500 font-light">
                    {t(errors.name.message!)}
                  </Text>
                )}
              </VStack>
            )}
            name="name"
          />

          <Controller
            control={control}
            render={({field: {onChange, value}}) => (
              <VStack>
                <Button
                  className="w-32 rounded-2xl bg-custom-pink"
                  onPress={toggleOpen}>
                  <ButtonIcon as={PlusIcon} />
                  <ButtonText>{t('add_apps')}</ButtonText>
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
                <Box className="mt-2 p-3 border border-gray-200/20 rounded-xl">
                  {value?.length === 0 ? (
                    <Text>{t('no_apps_selected')}</Text>
                  ) : (
                    <SelectedAppsInBlock
                      packageNames={value}
                      installedApps={installedApps}
                    />
                  )}
                </Box>
                {errors.blockedApps && (
                  <Text className="text-error-500  font-light">
                    {t(errors.blockedApps.message!)}
                  </Text>
                )}
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
                  <CheckboxIcon as={CheckIcon} className="bg-custom-green " />
                </CheckboxIndicator>
                <CheckboxLabel>{t('block_apps')}</CheckboxLabel>
              </Checkbox>
            )}
            name="blockApps"
          />

          <Controller
            control={control}
            render={({field: {onChange, value}}) => (
              <VStack>
                <Checkbox
                  isInvalid={false}
                  isDisabled={false}
                  value={value?.toString()}
                  onChange={onChange}
                  isChecked={value}>
                  <CheckboxIndicator>
                    <CheckboxIcon as={CheckIcon} className="bg-custom-green" />
                  </CheckboxIndicator>
                  <CheckboxLabel>{t('block_Notifications')}</CheckboxLabel>
                </Checkbox>
                {errors.blockApps && (
                  <Text className="text-error-500 font-light mt-2">
                    {t(errors.blockApps.message!)}
                  </Text>
                )}
              </VStack>
            )}
            name="blockNotifications"
          />
        </Box>
      </ScrollView>
      <Box className="p-3 border-t-black/10 border-t shadow-lg">
        <Button className="rounded-2xl bg-custom-green" onPress={onSubmit}>
          <ButtonText>
            {t(isEditing ? 'update_block' : 'create_block')}
          </ButtonText>
        </Button>
      </Box>
    </Box>
  );
};
