package com.things.rule.bean.action;

import com.things.common.constant.RedisConstants;
import com.things.common.core.redis.RedisCache;
import com.things.common.utils.bean.BeanUtils;
import com.things.device.domain.Device;
import com.things.device.service.IDeviceService;
import com.things.influxdb.service.IInfluxDbService;
import com.things.influxdb.vo.DeviceAlarm;
import com.things.influxdb.vo.DeviceData;
import com.things.product.domain.Product;
import com.things.product.domain.vo.ProductParams;
import com.things.rule.domain.AlarmAction;
import com.things.rule.domain.vo.ActionVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author DaiWei
 * @date 2023/05/10 16:20
 **/
@Slf4j
@Component
@AllArgsConstructor
public class AlarmActionExec {

    private final IDeviceService deviceService;

    private final RedisCache redisCache;

    private final IInfluxDbService influxDbService;

    public void exe(ActionVo actionVo , DeviceData deviceData){

        Device device = deviceService.selectByDeviceId(deviceData.getDeviceId());

        ProductParams productParams = redisCache.getCacheObject(RedisConstants.PRODUCT + deviceData.getProductId());

        Product product = productParams.getProduct();

        DeviceAlarm deviceAlarm = new DeviceAlarm();

        BeanUtils.copyProperties(deviceData,deviceAlarm);

        deviceAlarm.setDeviceName(device.getDeviceName());
        deviceAlarm.setProductName(product.getProductName());
        deviceAlarm.setLocation(device.getLocation());
        deviceAlarm.setTime(LocalDateTime.now());

        //设置告警信息
        AlarmAction alarmAction = actionVo.getAlarmAction();
        deviceAlarm.setAlarmName(alarmAction.getTitle());
        deviceAlarm.setLevel(alarmAction.getLevel());

        influxDbService.insertDeviceAlarm(deviceAlarm);

    }
}
