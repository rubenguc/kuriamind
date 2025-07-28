import {makeTheme} from 'dripsy';

const baseButton = {
  borderRadius: 12,
  borderWidth: 1,
  borderColor: 'transparent',
  px: 2,
  py: 2,
  label: {
    textAlign: 'center',
    fontWeight: 'bold',
    fontSize: 16,
  },
};

const inputBase = {
  borderRadius: 8,
  borderWidth: 1,
  color: 'white',
  borderColor: 'gray',
};

const DEFAULT_FONT = 'opensans';

const DripsyTheme = makeTheme({
  customFonts: {
    [DEFAULT_FONT]: {
      bold: 'OpenSans-Bold',
      default: 'OpenSans-Regular',
      normal: 'OpenSans-Regular',
    },
  },
  fonts: {
    root: DEFAULT_FONT,
  },
  colors: {
    background: '#24222f',
    card: '#24222f',
    text: '#fff',
    primary: '#1D71B8',
    secondary: '#cba2d9',
    accent: '#8bd480',
    gray: '#e5e0de',
    grayDisabled: '#999',
  },
  spacing: {
    xs: 4,
    sm: 8,
    md: 16,
    lg: 24,
    xl: 32,
  },
  fontSizes: {
    xs: 12,
    sm: 14,
    md: 16,
    lg: 18,
    xl: 20,
    '2xl': 24,
    '3xl': 32,
    '4xl': 40,
    '5xl': 48,
    '6xl': 64,
    '7xl': 80,
    '8xl': 96,
    '9xl': 128,
  },
  button: {
    primary: {
      ...baseButton,
      bg: 'primary',
      label: {
        color: 'white',
        ...baseButton.label,
      },
      disabled: {
        opacity: 0.5,
      },
    },
    outlined: {
      ...baseButton,
      bg: 'transparent',
      borderWidth: 1,
      borderColor: 'primary',
      label: {
        color: 'primary',
        ...baseButton.label,
      },
      disabled: {
        opacity: 0.5,
      },
    },
  },
  text: {
    error: {
      color: 'red',
    },
    body: {
      color: 'text',
    },
    h1: {
      color: 'text',
    },
    p: {
      color: 'gray',
    },
  },
  forms: {
    input: {
      ...inputBase,
      cursorColor: 'primary',
    },
    inputError: {
      ...inputBase,
      borderColor: 'red',
    },
  },
});

type MyTheme = typeof DripsyTheme;
declare module 'dripsy' {
  // eslint-disable-next-line @typescript-eslint/no-empty-interface
  interface DripsyCustomTheme extends MyTheme {}
}

export default DripsyTheme;
