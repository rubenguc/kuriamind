import {Controller} from 'react-hook-form';
import {useBlock} from './hooks';
import {PlusIcon} from 'lucide-react-native';
import {useToggle} from 'react-use';
import {useInstalledApps} from '@/providers';
import type {NativeStackScreenProps} from '@react-navigation/native-stack';
import {useTranslation} from 'react-i18next';
import type {RootStackParamList} from '@/interfaces';
import {DateTimePickerAndroid} from '@react-native-community/datetimepicker';
import {ScrollView, Text, TextInput, useDripsyTheme, View} from 'dripsy';
import {Button} from '@/components/ui';
import {Flex} from 'dripsy';
import {SelectedAppsInBlock} from '@/components/shared';
import {AdvancedCheckbox} from 'react-native-advanced-checkbox';
import {Switch} from 'react-native';
import {AppsToSelect} from './components/AppsToSelect';

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

  const {theme} = useDripsyTheme();

  return (
    <View sx={{flex: 1}}>
      <ScrollView
        sx={{
          flex: 1,
          px: 10,
          py: 5,
        }}>
        <View sx={{gap: 20}}>
          <Controller
            control={control}
            render={({field: {onChange, onBlur, value}}) => (
              <View sx={{display: 'flex'}}>
                <Text sx={{mb: 2}}>{t('name')}</Text>
                <TextInput
                  variant={errors.name ? 'inputError' : 'input'}
                  value={value}
                  onBlur={onBlur}
                  onChangeText={onChange}
                />
                {errors.name?.message && (
                  <Text variant="error">{t(errors.name.message)}</Text>
                )}
              </View>
            )}
            name="name"
          />

          <Controller
            control={control}
            render={({field: {onChange, value}}) => (
              <View sx={{display: 'flex'}}>
                <Button
                  sx={{
                    width: '30%',
                    mb: 5,
                  }}
                  onPress={toggleOpen}>
                  <Flex sx={{alignItems: 'center', gap: 10}}>
                    <PlusIcon color="black" />
                    <Text
                      sx={{
                        fontSize: 'sm',
                        color: 'black',
                      }}>
                      {t('add')}
                    </Text>
                  </Flex>
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
                <View
                  sx={{
                    p: 8,
                    mt: 4,
                    borderWidth: 1.5,
                    borderColor: 'gray',
                    borderRadius: 8,
                  }}>
                  {value?.length === 0 ? (
                    <Text sx={{color: 'gray'}}>{t('no_apps_selected')}</Text>
                  ) : (
                    <SelectedAppsInBlock
                      packageNames={value}
                      installedApps={installedApps}
                    />
                  )}
                </View>
                {errors.blockedApps?.message && (
                  <Text variant="error">{t(errors.blockedApps.message)}</Text>
                )}
              </View>
            )}
            name="blockedApps"
          />

          <View sx={{gap: 4}}>
            <Controller
              control={control}
              render={({field: {onChange, value}}) => (
                <AdvancedCheckbox
                  size={18}
                  value={value}
                  onValueChange={onChange}
                  label={t('block_apps')}
                  labelStyle={{
                    color: 'white',
                  }}
                  checkedColor={theme.colors.accent}
                />
              )}
              name="blockApps"
            />

            <Controller
              control={control}
              render={({field: {onChange, value}}) => (
                <View sx={{flex: 1}}>
                  <AdvancedCheckbox
                    size={18}
                    value={value}
                    onValueChange={onChange}
                    label={t('block_Notifications')}
                    labelStyle={{
                      color: 'white',
                    }}
                    checkedColor={theme.colors.accent}
                  />
                  {errors.blockApps?.message && (
                    <Text variant="error">{t(errors.blockApps.message)}</Text>
                  )}
                </View>
              )}
              name="blockNotifications"
            />

            <Controller
              control={control}
              render={({field: {onChange, value}}) => (
                <Flex
                  sx={{
                    mt: 3,
                    alignItems: 'center',
                  }}>
                  <Switch
                    trackColor={{
                      true: theme.colors.accent,
                      false: 'gray',
                    }}
                    thumbColor={!value ? theme.colors.accent : 'gray'}
                    value={!value}
                    onValueChange={() => onChange(!value)}
                  />
                  <Text
                    sx={{
                      fontSize: 'md',
                    }}>
                    {t('always_active')}
                  </Text>
                </Flex>
              )}
              name="addTimer"
            />
          </View>

          {isAddTimerActive && (
            <>
              <Controller
                control={control}
                render={({field: {onChange, value}}) => (
                  <Flex sx={{alignItems: 'center'}}>
                    <Text
                      sx={{
                        width: 80,
                      }}>
                      {t('start_time')}
                    </Text>
                    <Button
                      variant="outlined"
                      sx={{
                        width: 100,
                      }}
                      onPress={() =>
                        DateTimePickerAndroid.open({
                          mode: 'time',
                          value: getDefaultTime(value),
                          onChange: (_, date) => {
                            date && onChange(formatTime(date.toISOString()));
                          },
                        })
                      }>
                      <Text>{value}</Text>
                    </Button>
                  </Flex>
                )}
                name="startTime"
              />

              <Controller
                control={control}
                render={({field: {onChange, value}}) => (
                  <>
                    <Flex sx={{alignItems: 'center'}}>
                      <Text
                        sx={{
                          width: 80,
                        }}>
                        {t('end_time')}
                      </Text>
                      <Button
                        variant="outlined"
                        sx={{
                          width: 100,
                        }}
                        onPress={() =>
                          DateTimePickerAndroid.open({
                            mode: 'time',
                            value: getDefaultTime(value),
                            onChange: (_, date) => {
                              date && onChange(formatTime(date.toISOString()));
                            },
                          })
                        }>
                        <Text>{value}</Text>
                      </Button>
                    </Flex>
                    {errors.endTime?.message && (
                      <Text variant="error">{t(errors.endTime.message)}</Text>
                    )}
                  </>
                )}
                name="endTime"
              />
            </>
          )}
        </View>
      </ScrollView>
      <View
        sx={{
          p: 10,
        }}>
        <Button onPress={onSubmit} sx={{py: 10}}>
          {t(isEditing ? 'update_block' : 'create_block')}
        </Button>
      </View>
    </View>
  );
};
