import {NativeModules} from 'react-native';

const {MyAppModule} = NativeModules;

export const getInstalledApps = async (): Promise<
  {packageName: string; appName: string}[]
> => {
  return await MyAppModule.getInstalledApps();
};

export const getBlockedApps = async (): Promise<string[]> => {
  return await MyAppModule.getBlockedApps();
};

export const saveBlockedApps = async (apps: string[]): Promise<void> => {
  await MyAppModule.saveBlockedApps(apps);
};

export const checkAccessibilityServiceStatus = async (): Promise<void> => {
  const result = await MyAppModule.checkAccessibilityServiceStatus();
  console.log(result);
};
