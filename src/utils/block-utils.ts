import {InstalledApp} from '@/interfaces';

export const getSelectedApps = (
  selectedApps: string[] = [],
  allApps: InstalledApp[] = [],
) => {
  const filteredApps = allApps.filter(app =>
    selectedApps.includes(app.packageName),
  );

  return filteredApps;
};
