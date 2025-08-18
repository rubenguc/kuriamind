import {RequestPermissionAcceptOption} from '@/screens/welcome/components';
import {RequestPermissionOption} from '@/screens/welcome/components/RequestPermissionOption';
import {usePermissions} from '@/screens/welcome/hooks';
import NativeLocalStorage from '@/specs/NativeLocalStorage';
import {Flex, ScrollView, Text, View} from 'dripsy';
import {Bell, PersonStanding, ShieldBan} from 'lucide-react-native';
import {useTranslation} from 'react-i18next';
import {Modal, Platform} from 'react-native';

export const CheckPermissionsModal = () => {
  const isInit = NativeLocalStorage.getItem('isFirstTime') !== 'null';
  const {t: tPermissions} = useTranslation('permissions');

  const {
    isNotificationPermissionGranted,
    requestNotificationPermission,
    //
    isNotificationListenerServiceEnabled,
    requestNotificationListenerPermission,
    //
    isAccessibilityServiceEnabled,
    requestAccessibilityPermission,
    //
    isDisplayPopupEnabled,
    requestDisplayPopupEnabled,
    //
    allPermissionsGranted,
    allPermissionsChecked,
  } = usePermissions({
    disabledCheck: !isInit,
  });

  const showModal = isInit && allPermissionsChecked && !allPermissionsGranted;

  return (
    <Modal transparent visible={showModal}>
      <View
        sx={{
          flex: 1,
          backgroundColor: 'rgb(0,0,0, 0.4)',
        }}>
        <View
          sx={{
            backgroundColor: 'background',
            flex: 1,
            mt: '15%',
            borderTopLeftRadius: 20,
            borderTopRightRadius: 20,
            px: '5%',
            pt: '5%',
          }}>
          <ScrollView>
            <Text
              sx={{
                fontSize: 'md',
                lineHeight: 20,
                mt: '7%',
              }}>
              {tPermissions('check_permissions_missing')}
            </Text>

            <Flex sx={{flexDirection: 'column', gap: 30, mt: 30, pb: '10%'}}>
              {Number(Platform.Version) >= 33 &&
                !isNotificationPermissionGranted && (
                  <RequestPermissionOption
                    isActive={isNotificationPermissionGranted}
                    onRequestPermission={requestNotificationPermission}
                    title={tPermissions('post_notifications.title')}
                    description={tPermissions('post_notifications.description')}
                    Icon={<Bell size={22} color="#fff" />}
                  />
                )}
              {!isNotificationListenerServiceEnabled && (
                <RequestPermissionOption
                  isActive={isNotificationListenerServiceEnabled}
                  onRequestPermission={requestNotificationListenerPermission}
                  title={tPermissions('notification_listener.title')}
                  description={tPermissions(
                    'notification_listener.description',
                  )}
                  Icon={<Bell size={22} color="#fff" />}
                />
              )}

              {!isAccessibilityServiceEnabled && (
                <RequestPermissionAcceptOption
                  isActive={isAccessibilityServiceEnabled}
                  onRequestPermission={requestAccessibilityPermission}
                  title={tPermissions('accesibility_services.title')}
                  description={tPermissions(
                    'accesibility_services.description',
                  )}
                  Icon={<PersonStanding size={22} color="#fff" />}
                  acceptTitle={tPermissions(
                    'accesibility_services.accept_title',
                  )}
                  acceptDescription={tPermissions(
                    'accesibility_services.accept_description',
                  )}
                  cancelButtonText={tPermissions(
                    'accesibility_services.cancel_button_text',
                  )}
                  acceptButtonText={tPermissions(
                    'accesibility_services.accept_button_text',
                  )}
                />
              )}

              {!isDisplayPopupEnabled && (
                <RequestPermissionOption
                  isActive={isDisplayPopupEnabled}
                  onRequestPermission={requestDisplayPopupEnabled}
                  title={tPermissions('displayPopup.title')}
                  description={tPermissions('displayPopup.description')}
                  Icon={<ShieldBan size={22} color="#fff" />}
                />
              )}
            </Flex>
          </ScrollView>
        </View>
      </View>
    </Modal>
  );
};
