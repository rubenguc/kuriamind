import {NavigatorScreenParams} from '@react-navigation/native';

export interface Block {
  id: string;
  name: string;
  blockedApps: string[];
  blockApps: boolean;
  blockNotifications: boolean;
  isActive: boolean;
  startTime: string;
  endTime: string;
}

export type BlockToSave = Omit<Block, 'id' | 'isActive'>;

export interface InstalledApp {
  packageName: string;
  appName: string;
  icon: string;
}

export type BottomStackParamList = {
  Blocks:
    | {
        shouldRefresh?: boolean;
      }
    | undefined;
  Settings: undefined;
  BlockAction: undefined;
  Stats: undefined;
};

export type RootStackParamList = {
  Home: NavigatorScreenParams<BottomStackParamList>;
  Block: {block: Block} | undefined;
  Welcome: undefined;
  Settings: undefined;
};

export interface Settings {
  blockMessage: string;
}

type packageName = string;

export interface Stat {
  date: string;
  appStats: {
    [key: packageName]: {
      appBlockCount: number;
      notificationBlockCount: number;
    };
  };
}

export type StatsFilter = 'today' | 'last3days' | 'last7days';
