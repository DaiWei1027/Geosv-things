package com.things.device.message.handler;

import com.alibaba.fastjson2.JSONObject;
import com.things.common.constant.RedisConstants;
import com.things.common.constant.TopicConstants;
import com.things.common.core.redis.RedisCache;
import com.things.common.dynamicCompilation.ClassInjector;
import com.things.common.dynamicCompilation.DynamicClassLoader;
import com.things.common.dynamicCompilation.DynamicLoaderEngine;
import com.things.common.dynamicCompilation.bytecode.InjectionSystem;
import com.things.device.domain.Device;
import com.things.device.service.IDeviceService;
import com.things.influxdb.service.IInfluxDbService;
import com.things.influxdb.vo.DeviceData;
import com.things.mqtt.mqtt.MqttGateway;
import com.things.product.domain.vo.ProductParams;
import com.things.protocol.domain.Protocol;
import com.things.protocol.utils.ProtocolManage;
import com.things.utils.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @author DaiWei
 * @date 2023/04/26 15:41
 **/
@Slf4j
@Component
public class DeviceMessageHandler {

    @Autowired
    private ProtocolManage protocolManage;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IInfluxDbService influxDbService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private MqttGateway mqttGateway;

    @Autowired
    @Qualifier("deviceExecutor")
    private ThreadPoolTaskExecutor deviceExecutor;

    @Async("deviceExecutor")
    public void handleMessage(String productId, String deviceId, Object payload) {

        final ProductParams productParams = redisCache.getCacheObject(RedisConstants.PRODUCT + productId);

        if (Objects.isNull(productParams)) {
            log.info("设备消息处理器：查询产品信息为空为空，产品ID[{}]，设备编号[{}]", productId, deviceId);
            return;
        }

        final Protocol protocol = redisCache.getCacheObject(RedisConstants.PROTOCOL + productParams.getProduct().getProtocolId());

        if (Objects.isNull(protocol)) {
            log.info("设备消息处理器：查询协议为空，产品ID[{}]，设备编号[{}]", productId, deviceId);
            return;
        }

        final Object data = parse(payload, protocol.getDataType());


        Device device = deviceService.selectByDeviceId(deviceId);

        if (!Objects.isNull(device)) {

            deviceExecutor.execute(() -> {

                //动态协议解析数据
                JSONObject deviceJson;
                try {

                    deviceJson = protocolManage.load(protocol.getId(),data);

                } catch (Exception e) {
                    log.error("设备消息处理器，协议解析消息错误：{}", e.getMessage());
                    return;
                }

                log.debug("协议解析数据成功：数据[{}]", deviceJson);

                DeviceData deviceData = new DeviceData();
                deviceData.setData(deviceJson.toString());
                deviceData.setDeviceId(deviceId);
                deviceData.setProductId(productId);

                //插入influxDB

                influxDbService.insertDeviceData(deviceData);

                mqttGateway.sendToMqtt(TopicConstants.DEVICE_RULE_TOPIC, JSONObject.toJSONString(deviceData));

            });
        }


    }

    public static Object parse(Object payload, String dataType) {

        switch (dataType) {
            case "BYTE":
                payload = ByteUtil.bytes2HexString((byte[]) payload);
                break;
            case "JSON":
                payload = ByteUtil.bytes2HexString((byte[]) payload);
                payload = ByteUtil.hexStringToString(payload.toString());
                payload = JSONObject.parseObject(payload.toString());
                break;
            default:
                break;

        }

        return payload;

    }
}
