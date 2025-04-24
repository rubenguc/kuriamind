import {InstalledApp} from '@/interfaces';
import {getSelectedApps} from '@/utils/block-utils';
import {Flex, Image} from 'dripsy';

interface SelectedAppsInBlockProps {
  packageNames: string[];
  installedApps: InstalledApp[];
}

export const SelectedAppsInBlock = ({
  installedApps,
  packageNames,
}: SelectedAppsInBlockProps) => {
  return (
    <Flex
      sx={{
        flexWrap: 'wrap',
        gap: 8,
      }}>
      {getSelectedApps(packageNames, installedApps).map(app => (
        <Image
          key={app.packageName}
          source={{
            uri: app.icon,
          }}
          sx={{
            width: 20,
            height: 20,
          }}
          alt={app.appName}
        />
      ))}
    </Flex>
  );
};
