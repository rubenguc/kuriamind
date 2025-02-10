export interface Block {
  id: string;
  name: string;
  blockedApps: string[];
  blockApps: boolean;
  blockNotifications: boolean;
  isActive: boolean;
}

export type BlockToSave = Omit<Block, 'id'>;
