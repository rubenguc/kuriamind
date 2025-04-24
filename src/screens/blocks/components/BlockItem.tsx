import {SelectedAppsInBlock} from '@/components/shared';
import {Badge} from '@/components/ui';
import {Block, InstalledApp} from '@/interfaces';
import {Flex, Text, View} from 'dripsy';
import {Check, Timer} from 'lucide-react-native';
import {useTranslation} from 'react-i18next';
import {DropdownMenu} from './DropdownMenu';

interface BlockItemProps {
  block: Block;
  allApps: InstalledApp[];
  onEdit: () => void;
  onDelete: () => void;
  onChangeStatus: () => void;
}

export const BlockItem = ({
  allApps,
  block,
  onEdit,
  onDelete,
  onChangeStatus,
}: BlockItemProps) => {
  const {t} = useTranslation('blocks');

  const isActive = block.isActive;
  const isAppsBlocked = block.blockApps;
  const isNotificationsBlocked = block.blockNotifications;
  const hasTimers = block.startTime !== '' && block.endTime !== '';

  return (
    <View
      sx={{
        display: 'flex',
        borderWidth: 1.5,
        borderColor: isActive ? 'accent' : 'gray',
        borderRadius: 8,
        backgroundColor: '#222',
        p: 10,
      }}>
      <Text
        sx={{
          fontSize: 'xl',
          mb: 6,
        }}>
        {block.name}
      </Text>
      <Flex
        sx={{
          gap: 8,
          mb: 10,
        }}>
        {isAppsBlocked && <Badge Icon={Check} text={t('apps')} />}
        {isNotificationsBlocked && (
          <Badge Icon={Check} text={t('notifications')} />
        )}
      </Flex>
      {hasTimers && (
        <Flex sx={{alignItems: 'center', gap: 4, mb: 6}}>
          <Timer color="white" size={18} />
          <Text>
            {block.startTime} - {block.endTime}
          </Text>
        </Flex>
      )}

      <SelectedAppsInBlock
        packageNames={block.blockedApps}
        installedApps={allApps}
      />

      <DropdownMenu
        isActive={isActive}
        onDelete={onDelete}
        onEdit={onEdit}
        onChangeStatus={onChangeStatus}
      />
    </View>
  );
};
