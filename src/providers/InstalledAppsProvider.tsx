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
import {useTranslation} from 'react-i18next';
import {useCustomToast} from '@/hooks';

const InstalledAppsContext = createContext<{
  installedApps: InstalledApp[];
  loadInstalledApps: () => void;
}>({
  installedApps: [],
  loadInstalledApps: () => {},
});

export const InstalledAppsProvider: FC<PropsWithChildren> = ({children}) => {
  const {t} = useTranslation('shared');
  const {showErrorToast} = useCustomToast();
  const [installedApps, setInstalledApps] = useState<InstalledApp[]>([]);

  const loadInstalledApps = async () => {
    try {
      const installedApps = await getInstalledApps();
      setInstalledApps(installedApps);
    } catch (error) {
      showErrorToast({description: t('error_loading_apps')});
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
