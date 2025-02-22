module.exports = {
  root: true,
  extends: '@react-native',
  overrides: [
    {
      files: ['*.tsx'],
      rules: {
        'react/react-in-jsx-scope': 'off',
        'react-native/no-inline-styles': 0,
        'react-hooks/exhaustive-deps': 'off',
      },
    },
  ],
};
