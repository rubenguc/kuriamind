import {useCallback, useState} from 'react';
import {AppState} from 'react-native';
import {useFocusEffect} from '@react-navigation/native';

interface useRequestNativePermissionsProps {
  checkPermission: () => Promise<boolean>;
  requestPermission: () => void;
}

export const useRequestNativePermissions = ({
  checkPermission,
  requestPermission,
}: useRequestNativePermissionsProps): [boolean, () => void] => {
  const [isPermissionGranted, setIsPermissionGranted] = useState(false);

  const check = async () => {
    try {
      const isGranted = await checkPermission();
      setIsPermissionGranted(isGranted);
    } catch (error) {
      console.error(error);
    }
  };

  useFocusEffect(
    useCallback(() => {
      check();

      const subscription = AppState.addEventListener('change', nextAppState => {
        if (nextAppState === 'active') {
          check();
        }
      });

      return () => subscription.remove();
      // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []),
  );

  return [isPermissionGranted, requestPermission];
};
