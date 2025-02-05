import { FlatList, Image, Text, View } from 'react-native';
import React, { useEffect, useState } from 'react';
import { useNavigation } from '@react-navigation/native';
import { getInstalledApps } from '../../native-modules/MyAppBridge';

const Welcome = () => {

  const [apps, setApps] = useState([])

  useEffect(() => {
    (async () => {
      const result = await getInstalledApps();
      setApps(result)
    })();
  }, []);

  return (
    <View>
      <FlatList
        data={apps}
        keyExtractor={(item) => item.packageName}
        renderItem={({ item }) => (
          <View style={{ flexDirection: 'row', alignItems: 'center', padding: 10 }}>
            <Image
              source={{ uri: item.icon }}
              style={{ width: 40, height: 40, marginRight: 10 }}
            />
            <Text>{item.appName}</Text>
          </View>
        )}
      />
    </View>
  );
};

export default Welcome;
