export interface StatsObject {
  [packageName: string]: {
    appName: string;
    icon: string;
    appBlockCount: number;
    notificationBlockCount: number;
  };
}

export interface AppUsageStat {
  appName: string;
  icon: string;
  appBlockCount: number;
  notificationBlockCount: number;
}
