import type {Settings} from '@/interfaces';
import {NativeModules} from 'react-native';

const {SettingsModule} = NativeModules;

export const getNativeSettings = async (): Promise<Settings> => {
  const data = await SettingsModule.getSettings();
  return JSON.parse(data);
};

export const setSetting = async (key: string, value: any) => {
  await SettingsModule.setSetting(key, String(value));
};
