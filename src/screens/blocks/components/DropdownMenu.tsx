import {FlatList, Text, useSx, View} from 'dripsy';
import {Check, EditIcon, EllipsisVertical, Trash} from 'lucide-react-native';
import {FC} from 'react';
import {useTranslation} from 'react-i18next';
import {TouchableOpacity} from 'react-native';
import {useToggle} from 'react-use';

interface DropdownMenuProps {
  onDelete: () => void;
  onEdit: () => void;
  onChangeStatus: () => void;
  isActive: boolean;
}

export const DropdownMenu: FC<DropdownMenuProps> = ({
  onDelete,
  onEdit,
  isActive,
  onChangeStatus,
}) => {
  const {t} = useTranslation('blocks');
  const [isOpen, toggle] = useToggle(false);
  const sx = useSx();

  const OPTIONS = [
    {
      icon: EditIcon,
      label: t('edit'),
      onPress: onEdit,
    },
    {
      icon: Trash,
      label: t('delete'),
      onPress: onDelete,
    },
    {
      icon: Check,
      label: t(isActive ? 'disable' : 'activate'),
      onPress: onChangeStatus,
    },
  ];

  const handleAction = (handler: () => void) => () => {
    toggle();
    handler();
  };

  return (
    <View
      sx={{
        position: 'absolute',
        top: 8,
        right: 8,
      }}>
      <TouchableOpacity
        activeOpacity={0.8}
        style={sx({borderRadius: 9999, backgroundColor: 'primary', p: 6})}
        onPress={toggle}>
        <EllipsisVertical size={18} />
      </TouchableOpacity>
      {isOpen && (
        <View
          sx={{
            position: 'absolute',
            zIndex: 1000,
            top: 40,
            right: 1,
            width: 150,
            backgroundColor: '#111',
            borderRadius: 5,
            elevation: 3,
            shadowColor: '#000',
            shadowOpacity: 0.1,
            shadowRadius: 5,
            shadowOffset: {width: 0, height: 2},
          }}>
          <FlatList
            data={OPTIONS}
            keyExtractor={(item, index) => index.toString()}
            renderItem={({item}) => (
              <TouchableOpacity
                style={sx({
                  display: 'flex',
                  flexDirection: 'row',
                  alignItems: 'center',
                  gap: 10,
                  p: 10,
                })}
                onPress={handleAction(item.onPress)}>
                <item.icon color="#fff" size={16} />
                <Text sx={{fontSize: 'md'}}>{item.label}</Text>
              </TouchableOpacity>
            )}
          />
        </View>
      )}
    </View>
  );
};
