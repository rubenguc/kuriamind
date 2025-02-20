import {useEffect} from 'react';
import {View} from 'react-native';
import {RequestPermissionOption} from './RequestPermissionOption';
import {usePermissions} from '../hooks';
import {useTranslation} from 'react-i18next';
import {Text} from '@/components/ui/text';
import {VStack} from '@/components/ui/vstack';
import {Bell, PersonStanding} from 'lucide-react-native';

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
    allPermissionsGranted,
  } = usePermissions();

  useEffect(() => {
    if (allPermissionsGranted) {
      onAllPermissionsGranted();
    }
  }, [allPermissionsGranted]);

  return (
    <View>
      <Text className="mb-5">{t('permissions_message')}</Text>

      <VStack className="gap-5">
        <RequestPermissionOption
          isActive={isNotificationPermissionGranted}
          onRequestPermission={requestNotificationPermission}
          title={tPermissions('post_notifications.title')}
          description={tPermissions('post_notifications.description')}
          Icon={<Bell size={18} />}
        />
        <RequestPermissionOption
          isActive={isNotificationListenerServiceEnabled}
          onRequestPermission={requestNotificationListenerPermission}
          title={tPermissions('notification_listener.title')}
          description={tPermissions('notification_listener.description')}
          Icon={<Bell size={18} />}
        />

        <RequestPermissionOption
          isActive={isAccessibilityServiceEnabled}
          onRequestPermission={requestAccessibilityPermission}
          title={tPermissions('accesibility_services.title')}
          description={tPermissions('accesibility_services.description')}
          Icon={<PersonStanding size={18} />}
        />
      </VStack>
    </View>
  );
};
