import {makeTheme} from 'dripsy';

const baseButton = {
  borderRadius: 12,
  borderWidth: 1,
  borderColor: 'transparent',
  px: '5%',
  py: '2%',
  label: {
    textAlign: 'center',
    fontWeight: 'bold',
    fontSize: 16,
  },
};

const DEFAULT_FONT = 'HelveticaNeueCyr';

// body: ['HelveticaNeueCyrUltraLigth'],
// sans: ['HelveticaNeueCyrMedium'],
// bold: ['HelveticaNeueCyrBold'],
// light: ['HelveticaNeueCyrLight'],
// roman: ['HelveticaNeueCyrRoman'],
// thin: ['HelveticaNeueCyrThin'],
// ultraLigth: ['HelveticaNeueCyrUltraLigth'],

const theme = {
  customFonts: {
    [DEFAULT_FONT]: {
      bold: 'HelveticaNeueCyrMedium',
      default: 'HelveticaNeueCyrUltraLigth',
      normal: 'HelveticaNeueCyrUltraLigth',
      400: 'HelveticaNeueCyrThin',
      500: 'HelveticaNeueCyrMedium',
      600: 'HelveticaNeueCyrMedium',
      700: 'HelveticaNeueCyrMedium',
      800: 'HelveticaNeueCyrMedium',
      900: 'HelveticaNeueCyrRoman',
    },
  },
  fonts: {
    root: DEFAULT_FONT,
  },
  colors: {
    background: '#24222f',
    card: '#24222f',
    text: '#fff',
    primary: '#f08bc3',
    secondary: '#cba2d9',
    accent: '#9bec8f',
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
        color: 'black',
        ...baseButton.label,
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
    },
  },
  text: {
    body: {
      color: 'text',
    },
    h1: {
      color: 'text',
    },
    p: {
      color: 'text',
    },
  },
};

export default makeTheme(theme);
