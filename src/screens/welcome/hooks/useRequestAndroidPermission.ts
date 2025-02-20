import {useEffect, useState} from 'react';
import {Permission, PermissionsAndroid, PermissionStatus} from 'react-native';

export const useRequestAndroidPermission = ({
  permission,
  title,
  message,
}: {
  permission: Permission;
  title: string;
  message: string;
}): [boolean, () => void] => {
  const [isPermissionGranted, setIsPermissionGranted] = useState(false);

  useEffect(() => {
    (async () => {
      try {
        const isPermissionGranted = await PermissionsAndroid.check(permission);
        setIsPermissionGranted(isPermissionGranted);
      } catch (e) {
        console.log(e);
      }
    })();
  }, []);

  const requestPermission = async () => {
    try {
      const status = await PermissionsAndroid.request(permission, {
        title,
        message,
        buttonNeutral: 'Cancel',
        buttonPositive: 'Allow',
      });
      setIsPermissionGranted(status === PermissionsAndroid.RESULTS.GRANTED);
    } catch (e) {
      console.log(e);
    }
  };

  return [isPermissionGranted, requestPermission];
};
