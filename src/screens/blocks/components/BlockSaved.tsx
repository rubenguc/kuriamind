import {SelectedAppsInBlock} from '@/components/shared';
import {Button, ButtonIcon, ButtonText} from '@/components/ui/button';
import {Menu, MenuItem, MenuItemLabel} from '@/components/ui/menu';
import {Text} from '@/components/ui/text';
import {VStack} from '@/components/ui/vstack';
import {Block, InstalledApp} from '@/interfaces';
import {EditIcon, EllipsisVertical, Trash} from 'lucide-react-native';

interface BlockSavedProps {
  block: Block;
  allApps: InstalledApp[];
  onEdit: () => void;
  onDelete: () => void;
}

export const BlockSaved = ({
  allApps,
  block,
  onEdit,
  onDelete,
}: BlockSavedProps) => {
  return (
    <VStack className="p-4 bg-gray-600/40 rounded-2xl">
      <Text>{block.name}</Text>
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
            <Button
              size="xs"
              variant="outline"
              {...triggerProps}
              className="absolute top-2 right-2 border-0 rounded-full bg-red-100">
              <ButtonIcon as={EllipsisVertical} size="sm" />
            </Button>
          );
        }}>
        <MenuItem
          key="edit"
          className="gap-3"
          textValue="Edit"
          onPress={onEdit}>
          <EditIcon size={15} />
          <MenuItemLabel size="sm">Edit</MenuItemLabel>
        </MenuItem>
        <MenuItem
          key="delete"
          className="gap-3"
          onPress={onDelete}
          textValue="Delete">
          <Trash size={15} />
          <MenuItemLabel size="sm">Delete</MenuItemLabel>
        </MenuItem>
      </Menu>
    </VStack>
  );
};
