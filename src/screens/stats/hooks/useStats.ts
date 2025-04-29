import {Stat, StatsFilter} from '@/interfaces';
import {useEffect, useMemo, useState} from 'react';
import {fetchStats} from '@/native-modules/stats-module';
import {useInstalledApps} from '@/providers';
import {AppUsageStat, StatsObject} from '../interface';
import {useTranslation} from 'react-i18next';
import {useToggle} from 'react-use';
import {useCustomToast} from '@/hooks';

export const useStats = () => {
  const {t} = useTranslation('stats');
  const {installedApps} = useInstalledApps();
  const {showErrorToast} = useCustomToast();

  const [statFilter, setStatFilter] = useState<StatsFilter>('today');
  const [stats, setStats] = useState<Stat[]>([]);
  const [isLoading, toggleLoading] = useToggle(false);

  const getStats = async () => {
    toggleLoading(true);
    try {
      const statsData = await fetchStats(statFilter);
      setStats(statsData);
    } catch (err) {
      showErrorToast({
        description: t('error_fetching_stats'),
      });
    }
    toggleLoading(false);
  };

  useEffect(() => {
    getStats();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [statFilter]);

  const appUsageStats: AppUsageStat[] = useMemo(() => {
    const data: StatsObject = {};

    stats.forEach(dayStat => {
      const {appStats} = dayStat;

      Object.keys(appStats).forEach(packageName => {
        const {appBlockCount, notificationBlockCount} = appStats[packageName];

        if (data[packageName]) {
          data[packageName].appBlockCount += appBlockCount;
          data[packageName].notificationBlockCount += notificationBlockCount;
        } else {
          const app = installedApps.find(
            iApp => iApp.packageName === packageName,
          );

          if (app) {
            data[packageName] = {
              appName: app.appName,
              icon: app.icon,
              appBlockCount,
              notificationBlockCount,
            };
          }
        }
      });
    });

    return Object.values(data);
  }, [installedApps, stats]);

  const OPTIONS = [
    {
      label: t('today'),
      value: 'today',
    },
    {
      label: t('last3days'),
      value: 'last3days',
    },
    {
      label: t('last7days'),
      value: 'last7days',
    },
  ];

  const thereIsNoData = !isLoading && appUsageStats.length === 0;

  return {
    setStatFilter,
    appUsageStats,
    OPTIONS,
    statFilter,
    thereIsNoData,
  };
};
