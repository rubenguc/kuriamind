import {NativeModules} from 'react-native';

const {AppMonitorModule} = NativeModules;

export const startMonitoring = () => {
  return AppMonitorModule.startMonitoring();
};

export const stopMonitoring = async () => {
  return AppMonitorModule.stopMonitoring();
};
