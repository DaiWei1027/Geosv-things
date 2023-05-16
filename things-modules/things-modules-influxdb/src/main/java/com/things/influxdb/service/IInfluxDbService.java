package com.things.influxdb.service;

import com.things.influxdb.vo.DeviceAlarm;
import com.things.influxdb.vo.DeviceData;

/**
 * @author DaiWei
 * @date 2023/04/26 16:27
 **/
public interface IInfluxDbService {

    /**
     * 新增设备数据记录
     *
     * @param deviceData 设备数据
     */
    void insertDeviceData(DeviceData deviceData);

    /**
     * 新增设备数据记录
     *
     * @param deviceAlarm 设备数据
     */
    void insertDeviceAlarm(DeviceAlarm deviceAlarm);
}
