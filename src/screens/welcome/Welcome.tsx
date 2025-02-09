import { FlatList, Image, Text, View } from 'react-native';
import React, { useEffect, useState } from 'react';
import { useNavigation } from '@react-navigation/native';
import {
  getInstalledApps,
  getBlockedApps,
  saveBlockedApps,
  checkAccessibilityServiceStatus
} from '../../native-modules/MyAppBridge';
import { startMonitoring, stopMonitoring } from "../../native-modules/AppMonitor"
import { disableMonitoring, enableMonitoring, isMonitoringEnabled } from "../../native-modules/MonitorService"
import { Button } from '../../components/ui/button';
import {
  Checkbox,
  CheckboxIcon,
  CheckboxIndicator,
} from '../../components/ui/checkbox';
import { CheckIcon } from '../../components/ui/icon';

const Welcome = () => {
  const [apps, setApps] = useState([]);
  const [blockedApps, setBlockedApps] = useState([]);

  useEffect(() => {
    (async () => {
      const result = await getInstalledApps();
      setApps(result);

      const blockedApps = await getBlockedApps();
      console.log('blockedApps:', blockedApps);
      setBlockedApps(blockedApps);
    })();
  }, []);

  const setBlockedApp = (isAdded: boolean, id: string) => {
    if (!isAdded) {
      setBlockedApps(state => [...state, id]);
    } else {
      setBlockedApps(state => state.filter(_id => _id !== id));
    }
  };

  const saveConfig = () => {
    console.log('jelow')
    saveBlockedApps(blockedApps);
    console.log("saved")
  };

  return (
    <View className="relative">
      <Button className="absolute z-50 bottom-5 left-5" onPress={checkAccessibilityServiceStatus}>
        <Text>stop</Text>
      </Button>

      <Button className="absolute z-50 bottom-5 right-1/3" onPress={disableMonitoring}>
        <Text>stop</Text>
      </Button>

      <Button className="absolute z-50 bottom-5 right-1/2" onPress={enableMonitoring}>
        <Text>start</Text>
      </Button>

      <Button className="absolute z-50 bottom-5 right-5" onPress={saveConfig}>
        <Text>save</Text>
      </Button>

      <FlatList
        data={apps}
        keyExtractor={item => item.packageName}
        renderItem={({ item }) => (
          <View
            style={{ flexDirection: 'row', alignItems: 'center', padding: 10 }}>
            <Image
              source={{ uri: item.icon }}
              style={{ width: 40, height: 40, marginRight: 10 }}
            />
            <Text>{item.appName}</Text>

            <Checkbox
              size="md"
              isInvalid={false}
              isDisabled={false}
              value={item.packageName}
              isChecked={blockedApps.includes(item.packageName)}
              onChange={value => setBlockedApp(!value, item.packageName)}>
              <CheckboxIndicator>
                <CheckboxIcon as={CheckIcon} />
              </CheckboxIndicator>
            </Checkbox>
          </View>
        )}
      />
    </View>
  );
};

export default Welcome;
