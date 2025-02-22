import {NavigatorScreenParams} from '@react-navigation/native';

export interface Block {
  id: string;
  name: string;
  blockedApps: string[];
  blockApps: boolean;
  blockNotifications: boolean;
  isActive: boolean;
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
};

export type RootStackParamList = {
  Home: NavigatorScreenParams<BottomStackParamList>;
  Block: {block: Block} | undefined;
  Welcome: undefined;
  Settings: undefined;
};
