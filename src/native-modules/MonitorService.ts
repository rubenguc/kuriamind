import {NativeModules} from 'react-native';

const {MonitorModule} = NativeModules;

export const enableMonitoring = async () => {
  try {
    await MonitorModule.enableMonitoring();
  } catch (error) {
    console.error('Error al habilitar monitoreo:', error);
  }
};

export const disableMonitoring = async () => {
  try {
    await MonitorModule.disableMonitoring();
  } catch (error) {
    console.error('Error al deshabilitar monitoreo:', error);
  }
};

export const isMonitoringEnabled = async () => {
  try {
    return await MonitorModule.isMonitoringEnabled();
  } catch (error) {
    console.error('Error al obtener estado del monitoreo:', error);
    return false;
  }
};
