import {Flex, SxProp, Text, useDripsyTheme} from 'dripsy';
import {FC} from 'react';

interface BadgeProps {
  Icon: any;
  text: string;
  textProps?: SxProp;
  variant?: 'success';
}

const BACKGROUND_COLOR = {
  success: '#2c4433',
  error: 'red',
};

const TEXT_COLOR = {
  success: 'accent',
  error: 'white',
};

export const Badge: FC<BadgeProps> = ({
  Icon,
  textProps,
  text,
  variant = 'success',
}) => {
  const {theme} = useDripsyTheme();

  const backgroundColor = BACKGROUND_COLOR[variant];
  const color = theme.colors[TEXT_COLOR[variant]];

  return (
    <Flex
      sx={{
        gap: 4,
        backgroundColor,
        px: 10,
        py: 2,
        borderRadius: 10,
        alignItems: 'center',
      }}>
      <Icon size={16} color={color} />
      <Text sx={{...(textProps || {}), color}}>{text}</Text>
    </Flex>
  );
};
