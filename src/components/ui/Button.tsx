import React, {ComponentProps} from 'react';
import {TouchableOpacity as RNTouchableOpacity} from 'react-native';
import {
  ActivityIndicator,
  Text,
  View,
  useDripsyTheme,
  createThemedComponent,
} from 'dripsy';
import DripsyTheme from '@/theme';

const TouchableOpacity = createThemedComponent(RNTouchableOpacity);

type Props = {
  children: React.ReactNode | string;
  variant?: keyof typeof DripsyTheme.button;
  sx?: ComponentProps<typeof View>['sx'];
  labelSx?: ComponentProps<typeof View>['sx'];
  isText?: boolean;
  onPress?: () => void;
  labelProps?: ComponentProps<typeof Text>;
  isLoading?: boolean;
  isDisabled?: boolean;
} & ComponentProps<typeof TouchableOpacity>;

export function Button(props: Props) {
  const {
    children,
    variant = 'primary',
    sx = {},
    labelSx = {},
    isText = true,
    onPress,
    labelProps,
    isLoading = false,
    isDisabled = false,
    ...touchableProps
  } = props;

  const {theme} = useDripsyTheme();

  const {
    label: labelStyle = {},
    disabled: disabledStyle,
    ...variantStyle
  } = theme.button[variant] ?? {
    label: {},
    disabled: {},
  };

  const isButtonDisabled = isDisabled || isLoading;

  return (
    <TouchableOpacity
      activeOpacity={0.8}
      {...touchableProps}
      sx={{
        ...variantStyle,
        ...(isButtonDisabled && disabledStyle),
        ...sx,
      }}
      onPress={isButtonDisabled ? undefined : onPress}
      disabled={isButtonDisabled}>
      {isLoading ? (
        <ActivityIndicator
          color={theme.button[variant]?.label?.color || 'white'}
          size="small"
        />
      ) : isText ? (
        <Text {...labelProps} sx={{...labelStyle, ...labelSx}}>
          {children}
        </Text>
      ) : (
        children
      )}
    </TouchableOpacity>
  );
}
