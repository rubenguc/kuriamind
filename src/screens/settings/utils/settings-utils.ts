import {Settings} from '@/interfaces';

export const parseSettings = (settings: Record<string, any>): Settings => {
  return {
    blockMessage: settings.blockMessage || '',
  };
};
