import {JSX} from 'react';
import {TouchableOpacity} from 'react-native';
import {CircleCheckBig} from 'lucide-react-native';
import {Flex, Text, useDripsyTheme} from 'dripsy';

interface RequestPermissionOptionProps {
  title: string;
  description: string;
  isActive: boolean;
  onRequestPermission: () => void;
  Icon: JSX.Element;
}

export const RequestPermissionOption = ({
  isActive,
  onRequestPermission,
  title,
  Icon,
  description,
}: RequestPermissionOptionProps) => {
  const {theme} = useDripsyTheme();

  return (
    <TouchableOpacity disabled={isActive} onPress={onRequestPermission}>
      <Flex
        sx={{
          alignItems: 'center',
          px: '3%',
          py: '4%',
          backgroundColor: '#111',
          borderWidth: 2,
          borderColor: isActive ? 'accent' : 'grayDisabled',
          borderRadius: 12,
          gap: '2%',
        }}>
        {Icon}
        <Flex sx={{flex: 1, flexDirection: 'column', px: '2%'}}>
          <Flex sx={{gap: '2%', alignItems: 'center', mb: '3%'}}>
            <CircleCheckBig
              size={18}
              color={isActive ? '#9bec8f' : theme.colors.grayDisabled}
            />
            <Text sx={{fontSize: 'lg'}}>{title}</Text>
          </Flex>
          <Text sx={{lineHeight: 20}}>{description}</Text>
        </Flex>
      </Flex>
    </TouchableOpacity>
  );
};
