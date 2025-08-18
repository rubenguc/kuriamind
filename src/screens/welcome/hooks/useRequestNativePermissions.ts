import {useCallback, useState} from 'react';
import {AppState} from 'react-native';
import {useFocusEffect} from '@react-navigation/native';

interface useRequestNativePermissionsProps {
  checkPermission: () => Promise<boolean>;
  requestPermission: () => void;
  disabledCheck?: boolean;
}

export const useRequestNativePermissions = ({
  checkPermission,
  requestPermission,
  disabledCheck = false,
}: useRequestNativePermissionsProps): [boolean, () => void, boolean] => {
  const [isPermissionGranted, setIsPermissionGranted] = useState(false);
  const [isPermissionChecked, setIsPermissionChecked] = useState(false);

  const check = async () => {
    try {
      if (!disabledCheck) {
        const isGranted = await checkPermission();
        setIsPermissionGranted(isGranted);
      }
    } catch (error) {
      console.error(error);
    }
    setIsPermissionChecked(true);
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

  return [isPermissionGranted, requestPermission, isPermissionChecked];
};
