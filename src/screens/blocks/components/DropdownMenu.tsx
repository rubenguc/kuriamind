import {FlatList, Text, useSx, View} from 'dripsy';
import {Check, EditIcon, EllipsisVertical, Trash} from 'lucide-react-native';
import {type FC, useRef, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {Dimensions, Modal, TouchableOpacity} from 'react-native';
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
  const buttonRef = useRef<View>(null);

  const [position, setPosition] = useState({x: 0, y: 0});

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

  const getPosition = () => {
    buttonRef.current?.measure((_fx, _fy, _width, _height, px, py) => {
      setPosition({x: px, y: py});
    });
  };

  const handleOpen = () => {
    getPosition();
    toggle();
  };

  return (
    <View
      sx={{
        position: 'absolute',
        top: 8,
        right: 8,
      }}>
      <TouchableOpacity
        ref={buttonRef}
        activeOpacity={0.8}
        style={sx({borderRadius: 9999, backgroundColor: 'primary', p: 6})}
        onPress={handleOpen}>
        <EllipsisVertical size={18} />
      </TouchableOpacity>
      <Modal visible={isOpen} transparent>
        <TouchableOpacity
          activeOpacity={1}
          style={{
            flex: 1,
            backgroundColor: 'transparent',
          }}
          onPress={toggle}>
          <View
            pointerEvents="auto"
            sx={{
              pointerEvents: 'auto',
              position: 'absolute',
              zIndex: 1000,
              top: position.y + 30, // Ajusta según el tamaño del botón
              right: Dimensions.get('window').width - position.x - 30, // Alinea al botón
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
                  onPress={e => {
                    e.stopPropagation();
                    handleAction(item.onPress)();
                  }}>
                  <item.icon color="#fff" size={16} />
                  <Text sx={{fontSize: 'md'}}>{item.label}</Text>
                </TouchableOpacity>
              )}
            />
          </View>
        </TouchableOpacity>
      </Modal>
    </View>
  );
};
