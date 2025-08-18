import {PermissionsAndroid} from 'react-native';
import {useRequestAndroidPermission} from './useRequestAndroidPermission';
import {useRequestNativePermissions} from './useRequestNativePermissions';
import {
  checkNotificationListenerServiceEnabled,
  openNotificationListenerSettings,
  checkAccessibilityServiceEnabled,
  openAccessibilitySettings,
  checkDisplayPopupPermission,
  openDisplayPopupPermissionSettings,
} from '@/native-modules/permissions-module';

interface UsePermissionsProps {
  disabledCheck: boolean;
}

export const usePermissions = (
  {disabledCheck}: UsePermissionsProps = {disabledCheck: false},
) => {
  const [
    isNotificationPermissionGranted,
    requestNotificationPermission,
    isNotificationPermissionChecked,
  ] = useRequestAndroidPermission({
    permission: PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS,
    title: 'Notification Permission',
    message: 'We need your permission to send notifications.',
    disabledCheck,
  });

  const [
    isNotificationListenerServiceEnabled,
    requestNotificationListenerPermission,
    isNotificationListenerServiceChecked,
  ] = useRequestNativePermissions({
    checkPermission: checkNotificationListenerServiceEnabled,
    requestPermission: openNotificationListenerSettings,
    disabledCheck,
  });

  const [
    isAccessibilityServiceEnabled,
    requestAccessibilityPermission,
    isAccessibilityServiceChecked,
  ] = useRequestNativePermissions({
    checkPermission: checkAccessibilityServiceEnabled,
    requestPermission: openAccessibilitySettings,
    disabledCheck,
  });

  const [
    isDisplayPopupEnabled,
    requestDisplayPopupEnabled,
    isDisplayPopupChecked,
  ] = useRequestNativePermissions({
    checkPermission: checkDisplayPopupPermission,
    requestPermission: openDisplayPopupPermissionSettings,
    disabledCheck,
  });

  const allPermissionsGranted =
    isNotificationPermissionGranted &&
    isNotificationListenerServiceEnabled &&
    isAccessibilityServiceEnabled &&
    isDisplayPopupEnabled;

  const allPermissionsChecked =
    isNotificationPermissionChecked &&
    isNotificationListenerServiceChecked &&
    isAccessibilityServiceChecked &&
    isDisplayPopupChecked;

  return {
    isNotificationPermissionGranted,
    requestNotificationPermission,
    isNotificationListenerServiceEnabled,
    requestNotificationListenerPermission,
    isAccessibilityServiceEnabled,
    requestAccessibilityPermission,
    allPermissionsGranted,
    isDisplayPopupEnabled,
    requestDisplayPopupEnabled,
    allPermissionsChecked,
  };
};
