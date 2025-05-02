import {useEffect} from 'react';
import {Platform, View} from 'react-native';
import {RequestPermissionOption} from './RequestPermissionOption';
import {usePermissions} from '../hooks';
import {useTranslation} from 'react-i18next';
import {Bell, PersonStanding, ShieldBan} from 'lucide-react-native';
import {RequestPermissionAcceptOption} from './RequestPermissionOptionWithAccept';
import {Flex, Text} from 'dripsy';

interface PermissionsProps {
  onAllPermissionsGranted: () => void;
}

export const Permissions = ({onAllPermissionsGranted}: PermissionsProps) => {
  const {t} = useTranslation('welcome');
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
  } = usePermissions();

  useEffect(() => {
    if (allPermissionsGranted) {
      onAllPermissionsGranted();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [allPermissionsGranted]);

  return (
    <>
      <Text
        sx={{
          fontSize: 'md',
          lineHeight: 20,
          pt: '7%',
        }}>
        {t('permissions_message')}
      </Text>

      <Flex sx={{flexDirection: 'column', gap: 30, mt: 30, pb: '10%'}}>
        {Number(Platform.Version) >= 33 && (
          <RequestPermissionOption
            isActive={isNotificationPermissionGranted}
            onRequestPermission={requestNotificationPermission}
            title={tPermissions('post_notifications.title')}
            description={tPermissions('post_notifications.description')}
            Icon={<Bell size={22} color="#fff" />}
          />
        )}
        <RequestPermissionOption
          isActive={isNotificationListenerServiceEnabled}
          onRequestPermission={requestNotificationListenerPermission}
          title={tPermissions('notification_listener.title')}
          description={tPermissions('notification_listener.description')}
          Icon={<Bell size={22} color="#fff" />}
        />

        <RequestPermissionAcceptOption
          isActive={isAccessibilityServiceEnabled}
          onRequestPermission={requestAccessibilityPermission}
          title={tPermissions('accesibility_services.title')}
          description={tPermissions('accesibility_services.description')}
          Icon={<PersonStanding size={22} color="#fff" />}
          acceptTitle={tPermissions('accesibility_services.accept_title')}
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

        <RequestPermissionOption
          isActive={isDisplayPopupEnabled}
          onRequestPermission={requestDisplayPopupEnabled}
          title={tPermissions('displayPopup.title')}
          description={tPermissions('displayPopup.description')}
          Icon={<ShieldBan size={22} color="#fff" />}
        />
      </Flex>
    </>
  );
};
