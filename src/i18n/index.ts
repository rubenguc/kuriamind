import i18next from 'i18next';
import {initReactI18next} from 'react-i18next';
import RNLanguageDetector from '@os-team/i18next-react-native-language-detector';
import en from './locales/en.json';
import es from './locales/es.json';

i18next
  .use(RNLanguageDetector)
  .use(initReactI18next)
  .init({
    fallbackLng: 'en',
    supportedLngs: ['en', 'es'],
    ns: [],
    defaultNS: undefined,
    resources: {
      en,
      es,
    },
    interpolation: {
      escapeValue: false,
    },
  });

export default i18next;
