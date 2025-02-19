import {NativeModules} from 'react-native';

const {PermissionsModule} = NativeModules;

export const openNotificationSettings = () => {
  PermissionsModule.openNotificationSettings();
};

export const isNotificationServiceEnabled = async () => {
  try {
    const isEnabled = await PermissionsModule.isNotificationServiceEnabled();
    console.log('Servicio de accesibilidad habilitado:', isEnabled);
  } catch (error) {
    console.error('Error verificando el servicio:', error);
  }
};
