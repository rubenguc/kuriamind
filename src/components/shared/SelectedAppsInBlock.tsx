import {InstalledApp} from '@/interfaces';
import {getSelectedApps} from '@/utils/block-utils';
import {HStack} from '../ui/hstack';
import {Image} from '../ui/image';

interface SelectedAppsInBlockProps {
  packageNames: string[];
  installedApps: InstalledApp[];
}

export const SelectedAppsInBlock = ({
  installedApps,
  packageNames,
}: SelectedAppsInBlockProps) => {
  return (
    <HStack className="flex-wrap gap-2">
      {getSelectedApps(packageNames, installedApps).map(app => (
        <Image
          key={app.packageName}
          source={{
            uri: app.icon,
          }}
          className="h-8 w-8"
          alt={app.appName}
        />
      ))}
    </HStack>
  );
};
