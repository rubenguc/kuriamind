import type {Stat, StatsFilter} from '@/interfaces';
import {NativeModules} from 'react-native';

const {StatsModule} = NativeModules;

export const fetchStats = async (filter: StatsFilter): Promise<Stat[]> => {
  const jsonData = await StatsModule.queryStats(filter);
  return JSON.parse(jsonData);
};
