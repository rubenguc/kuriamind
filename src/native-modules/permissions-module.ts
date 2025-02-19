import {NativeModules} from 'react-native';

const {PermissionsModule} = NativeModules;

// NotificationListenerService
export const openNotificationListenerSettings = (): void => {
  PermissionsModule.openNotificationListenerSettings();
};

export const checkNotificationListenerServiceEnabled = (): Promise<boolean> => {
  return PermissionsModule.checkNotificationListenerServiceEnabled();
};

// AccessibilityService
export const openAccessibilitySettings = (): void => {
  PermissionsModule.openAccessibilitySettings();
};

export const checkAccessibilityServiceEnabled = (): Promise<boolean> => {
  return PermissionsModule.checkAccessibilityServiceEnabled();
};
