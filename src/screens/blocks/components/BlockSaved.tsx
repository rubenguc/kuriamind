import {SelectedAppsInBlock} from '@/components/shared';
import {Badge, BadgeIcon, BadgeText} from '@/components/ui/badge';
import {Box} from '@/components/ui/box';
import {HStack} from '@/components/ui/hstack';
import {Menu, MenuItem, MenuItemLabel} from '@/components/ui/menu';
import {Text} from '@/components/ui/text';
import {VStack} from '@/components/ui/vstack';
import {Block, InstalledApp} from '@/interfaces';
import {Check, EditIcon, EllipsisVertical, Trash} from 'lucide-react-native';
import {useTranslation} from 'react-i18next';
import {TouchableOpacity} from 'react-native';

interface BlockSavedProps {
  block: Block;
  allApps: InstalledApp[];
  onEdit: () => void;
  onDelete: () => void;
  onChangeStatus: () => void;
}

export const BlockSaved = ({
  allApps,
  block,
  onEdit,
  onDelete,
  onChangeStatus,
}: BlockSavedProps) => {
  const {t} = useTranslation('blocks');

  const isActive = block.isActive;
  const isAppsBlocked = block.blockApps;
  const isNotificationsBlocked = block.blockNotifications;

  return (
    <VStack className="p-4 bg-gray-950 border border-custom-pink/50 rounded-2xl">
      <Text className="font-bold text-xl mb-3">{block.name}</Text>
      <HStack className="gap-4 mb-5">
        <Badge
          className="w-20 rounded-xl "
          size="md"
          variant="solid"
          action={isActive ? 'success' : 'muted'}>
          <BadgeIcon as={Check} className="mr-2" />
          <BadgeText className={isActive ? 'text-custom-green' : ''}>
            {t('active')}
          </BadgeText>
        </Badge>
        <Badge
          className="w-20 rounded-xl"
          size="md"
          variant="solid"
          action={isAppsBlocked ? 'success' : 'muted'}>
          <BadgeIcon as={Check} className="mr-2" />
          <BadgeText className={isAppsBlocked ? 'text-custom-green' : ''}>
            {t('apps')}
          </BadgeText>
        </Badge>
        <Badge
          className="min-w-20 rounded-xl"
          size="md"
          variant="solid"
          action={isNotificationsBlocked ? 'success' : 'muted'}>
          <BadgeIcon as={Check} className="mr-2" />
          <BadgeText
            className={isNotificationsBlocked ? 'text-custom-green' : ''}>
            {t('notifications')}
          </BadgeText>
        </Badge>
      </HStack>
      <SelectedAppsInBlock
        packageNames={block.blockedApps}
        installedApps={allApps}
      />
      <Menu
        className="w-28 right-7 overflow-hidden"
        placement="bottom"
        offset={-23}
        trigger={({...triggerProps}) => {
          return (
            <TouchableOpacity
              activeOpacity={0.95}
              x
              {...triggerProps}
              style={{
                position: 'absolute',
                top: 8,
                right: 8,
              }}>
              <Box className="p-2 border-0 rounded-full bg-custom-pink">
                <EllipsisVertical size={15} />
              </Box>
            </TouchableOpacity>
          );
        }}>
        <MenuItem
          key="edit"
          className="gap-3"
          textValue="Edit"
          onPress={onEdit}>
          <EditIcon size={15} color="white" />
          <MenuItemLabel size="sm">{t('edit')}</MenuItemLabel>
        </MenuItem>
        <MenuItem
          key="delete"
          className="gap-3"
          onPress={onDelete}
          textValue="Delete">
          <Trash size={15} color="white" />
          <MenuItemLabel size="sm">{t('delete')}</MenuItemLabel>
        </MenuItem>
        <MenuItem
          key="changeStatus"
          className="gap-3"
          onPress={onChangeStatus}
          textValue="changeStatus">
          <Check size={15} color="white" />
          <MenuItemLabel size="sm">
            {t(isActive ? 'disabled' : 'active')}
          </MenuItemLabel>
        </MenuItem>
      </Menu>
    </VStack>
  );
};
