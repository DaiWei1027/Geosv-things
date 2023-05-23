package com.things.influxdb.service.impl;

import com.google.common.collect.Maps;
import com.things.influxdb.service.IInfluxDbService;
import com.things.influxdb.vo.DeviceAlarm;
import com.things.influxdb.vo.DeviceData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import plus.ojbk.influxdb.core.InfluxdbTemplate;
import plus.ojbk.influxdb.util.InfluxdbUtils;

import java.util.Map;

/**
 * @author DaiWei
 * @date 2023/04/26 16:28
 **/
@Service
@AllArgsConstructor
public class InfluxDbServiceImpl implements IInfluxDbService {

    private final InfluxdbTemplate influxdbTemplate;


    @Override
    public void insertDeviceData(DeviceData deviceData) {

        String measurement = InfluxdbUtils.getMeasurement(DeviceData.class);

        Map<String, String> tags = Maps.newHashMap();
        Map<String, Object> fields = Maps.newHashMap();

        tags.put("device_id", deviceData.getDeviceId());
        tags.put("device_name", deviceData.getDeviceName());
        fields.put("product_id", deviceData.getProductId());
        fields.put("data", deviceData.getData());

        long time = System.currentTimeMillis() / 1000;

        influxdbTemplate.insert(tags, fields, time, measurement);

    }

    @Override
    public void insertDeviceAlarm(DeviceAlarm deviceAlarm){

        influxdbTemplate.insert(deviceAlarm);

    }
}
