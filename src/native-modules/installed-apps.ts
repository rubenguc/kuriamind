import {InstalledApp} from '@/interfaces';
import {NativeModules} from 'react-native';

const {InstalledAppsModule} = NativeModules;

export const getInstalledApps = async (): Promise<InstalledApp[]> => {
  return await InstalledAppsModule.getAll();
};
