import { useEffect } from 'react';
import { Platform, View } from 'react-native';
import { RequestPermissionOption } from './RequestPermissionOption';
import { usePermissions } from '../hooks';
import { useTranslation } from 'react-i18next';
import { Text } from '@/components/ui/text';
import { VStack } from '@/components/ui/vstack';
import { Bell, PersonStanding, ShieldBan } from 'lucide-react-native';

interface PermissionsProps {
  onAllPermissionsGranted: () => void;
}

export const Permissions = ({ onAllPermissionsGranted }: PermissionsProps) => {
  const { t } = useTranslation('welcome');
  const { t: tPermissions } = useTranslation('permissions');

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
  }, [allPermissionsGranted]);

  return (
    <View>
      <Text className="mb-10 text-lg text-white">
        {t('permissions_message')}
      </Text>

      <VStack className="gap-5">
        {Number(Platform.Version) >= 33 && (
          <RequestPermissionOption
            isActive={isNotificationPermissionGranted}
            onRequestPermission={requestNotificationPermission}
            title={tPermissions('post_notifications.title')}
            description={tPermissions('post_notifications.description')}
            Icon={<Bell size={18} color="#fff" />}
          />
        )}
        <RequestPermissionOption
          isActive={isNotificationListenerServiceEnabled}
          onRequestPermission={requestNotificationListenerPermission}
          title={tPermissions('notification_listener.title')}
          description={tPermissions('notification_listener.description')}
          Icon={<Bell size={18} color="#fff" />}
        />

        <RequestPermissionOption
          isActive={isAccessibilityServiceEnabled}
          onRequestPermission={requestAccessibilityPermission}
          title={tPermissions('accesibility_services.title')}
          description={tPermissions('accesibility_services.description')}
          Icon={<PersonStanding size={18} color="#fff" />}
        />

        <RequestPermissionOption
          isActive={isDisplayPopupEnabled}
          onRequestPermission={requestDisplayPopupEnabled}
          title={tPermissions('displayPopup.title')}
          description={tPermissions('displayPopup.description')}
          Icon={<ShieldBan size={18} color="#fff" />}
        />

      </VStack>
    </View>
  );
};
