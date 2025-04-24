import React, {ComponentProps} from 'react';
import {
  TouchableOpacity as RNTouchableOpacity,
  TouchableWithoutFeedback,
} from 'react-native';
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
  touchableProps?: ComponentProps<typeof TouchableWithoutFeedback>;
  isLoading?: boolean;
  isDisabled?: boolean;
} & ComponentProps<typeof View>;

export function Button(props: Props) {
  const {
    children,
    variant = 'primary',
    sx = {},
    labelSx = {},
    isText = true,
    onPress,
    labelProps,
    touchableProps,
    isLoading = false,
    isDisabled = false,
    ...viewProps
  } = props;

  const {theme} = useDripsyTheme();

  // Obtener estilos del tema según la variante
  const {
    label: labelStyle = {},
    disabled: disabledStyle,
    ...variantStyle
  } = theme.button[variant] ?? {
    label: {},
    disabled: {},
  };

  // Determinar si el botón debe estar deshabilitado
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
      disabled={isButtonDisabled} // Deshabilitar interacción
    >
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
