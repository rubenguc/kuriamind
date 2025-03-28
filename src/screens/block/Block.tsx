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
import {useTranslation} from 'react-i18next';
import {RootStackParamList} from '@/interfaces';
import {HStack} from '@/components/ui/hstack';
import {DateTimePickerAndroid} from '@react-native-community/datetimepicker';
import {Switch} from '@/components/ui/switch';

type BlocksProps = NativeStackScreenProps<RootStackParamList, 'Block'>;

export const Block = ({route, navigation}: BlocksProps) => {
  const {t} = useTranslation('block');
  const {installedApps} = useInstalledApps();
  const {
    control,
    onSubmit,
    errors,
    isEditing,
    formatTime,
    isAddTimerActive,
    getDefaultTime,
  } = useBlock({
    defaultBlock: route.params?.block || undefined,
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
                  <Text className="font-light text-error-500">
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
                <Box className="p-3 mt-2 border border-gray-200/20 rounded-xl">
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
                  <Text className="font-light text-error-500">
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
                  <Text className="mt-2 font-light text-error-500">
                    {t(errors.blockApps.message!)}
                  </Text>
                )}
              </VStack>
            )}
            name="blockNotifications"
          />

          <Controller
            control={control}
            render={({field: {onChange, value}}) => (
              <HStack space="md">
                <Switch
                  isChecked={value}
                  value={value}
                  onToggle={() => onChange(!value)}
                />
                <Text size="sm">{t('add_timer')}</Text>
              </HStack>
            )}
            name="addTimer"
          />

          {isAddTimerActive && (
            <>
              <Controller
                control={control}
                render={({field: {onChange, value}}) => (
                  <HStack className="items-center gap-5">
                    <Text className="w-[20%]">{t('start_time')}</Text>
                    <Button
                      variant="outline"
                      className="w-36"
                      onPress={() =>
                        DateTimePickerAndroid.open({
                          mode: 'time',
                          value: getDefaultTime(value),
                          onChange: (_, date) => {
                            onChange(formatTime(date!.toISOString()));
                          },
                        })
                      }>
                      <ButtonText className="text-white">{value}</ButtonText>
                    </Button>
                  </HStack>
                )}
                name="startTime"
              />

              <Controller
                control={control}
                render={({field: {onChange, value}}) => (
                  <>
                    <HStack className="items-center gap-5">
                      <Text className="w-[20%]">{t('end_time')}</Text>
                      <Button
                        variant="outline"
                        className="w-36"
                        onPress={() =>
                          DateTimePickerAndroid.open({
                            mode: 'time',
                            value: getDefaultTime(value),
                            onChange: (_, date) => {
                              onChange(formatTime(date!.toISOString()));
                            },
                          })
                        }>
                        <ButtonText className="text-white">{value}</ButtonText>
                      </Button>
                    </HStack>
                    {errors.endTime && (
                      <Text className="mt-2 font-light text-error-500">
                        {t(errors.endTime.message!)}
                      </Text>
                    )}
                  </>
                )}
                name="endTime"
              />
            </>
          )}
        </Box>
      </ScrollView>
      <Box className="p-3 border-t shadow-lg border-t-black/10">
        <Button className="rounded-2xl bg-custom-green" onPress={onSubmit}>
          <ButtonText>
            {t(isEditing ? 'update_block' : 'create_block')}
          </ButtonText>
        </Button>
      </Box>
    </Box>
  );
};
