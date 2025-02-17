import {
  createContext,
  FC,
  PropsWithChildren,
  useContext,
  useEffect,
  useState,
} from 'react';
import {InstalledApp} from '@/interfaces';
import {getInstalledApps} from '@/native-modules/installed-apps';

const InstalledAppsContext = createContext<{
  installedApps: InstalledApp[];
  loadInstalledApps: () => void;
}>({
  installedApps: [],
  loadInstalledApps: () => {},
});

export const InstalledAppsProvider: FC<PropsWithChildren> = ({children}) => {
  const [installedApps, setInstalledApps] = useState<InstalledApp[]>([]);

  const loadInstalledApps = async () => {
    try {
      const installedApps = await getInstalledApps();
      setInstalledApps(installedApps);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    loadInstalledApps();
  }, []);

  return (
    <InstalledAppsContext.Provider
      value={{
        installedApps,
        loadInstalledApps,
      }}>
      {children}
    </InstalledAppsContext.Provider>
  );
};

export const useInstalledApps = () => useContext(InstalledAppsContext);
