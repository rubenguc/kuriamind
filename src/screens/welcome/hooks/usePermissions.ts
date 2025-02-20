import {PermissionsAndroid} from 'react-native';
import {useRequestAndroidPermission} from './useRequestAndroidPermission';
import {useRequestNativePermissions} from './useRequestNativePermissions';
import {
  checkNotificationListenerServiceEnabled,
  openNotificationListenerSettings,
  checkAccessibilityServiceEnabled,
  openAccessibilitySettings,
} from '@/native-modules/permissions-module';

export const usePermissions = () => {
  const [isNotificationPermissionGranted, requestNotificationPermission] =
    useRequestAndroidPermission({
      permission: PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS,
      title: 'Notification Permission',
      message: 'We need your permission to send notifications.',
    });

  const [
    isNotificationListenerServiceEnabled,
    requestNotificationListenerPermission,
  ] = useRequestNativePermissions({
    checkPermission: checkNotificationListenerServiceEnabled,
    requestPermission: openNotificationListenerSettings,
  });

  const [isAccessibilityServiceEnabled, requestAccessibilityPermission] =
    useRequestNativePermissions({
      checkPermission: checkAccessibilityServiceEnabled,
      requestPermission: openAccessibilitySettings,
    });

  const allPermissionsGranted =
    isNotificationPermissionGranted &&
    isNotificationListenerServiceEnabled &&
    isAccessibilityServiceEnabled;

  return {
    isNotificationPermissionGranted,
    requestNotificationPermission,
    isNotificationListenerServiceEnabled,
    requestNotificationListenerPermission,
    isAccessibilityServiceEnabled,
    requestAccessibilityPermission,
    allPermissionsGranted,
  };
};
