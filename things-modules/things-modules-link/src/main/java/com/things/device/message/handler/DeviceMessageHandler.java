package com.things.device.message.handler;

import com.alibaba.fastjson2.JSONObject;
import com.things.common.constant.DeviceConstants;
import com.things.common.constant.RedisConstants;
import com.things.common.constant.TopicConstants;
import com.things.common.core.redis.RedisCache;
import com.things.common.utils.StringUtils;
import com.things.device.domain.Device;
import com.things.device.domain.SubDevice;
import com.things.device.service.IDeviceService;
import com.things.device.service.ISubDeviceService;
import com.things.influxdb.service.IInfluxDbService;
import com.things.influxdb.vo.DeviceData;
import com.things.mqtt.mqtt.MqttGateway;
import com.things.product.domain.Product;
import com.things.product.domain.vo.ProductParams;
import com.things.protocol.domain.Protocol;
import com.things.protocol.utils.ProtocolManage;
import com.things.tcp.handler.NettyMessageHandler;
import com.things.utils.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author DaiWei
 * @date 2023/04/26 15:41
 **/
@Slf4j
@Component
public class DeviceMessageHandler implements NettyMessageHandler {

    @Autowired
    private ProtocolManage protocolManage;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IInfluxDbService influxDbService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private ISubDeviceService subDeviceService;

    @Autowired
    private MqttGateway mqttGateway;

    @Autowired
    @Qualifier("deviceExecutor")
    private ThreadPoolTaskExecutor deviceExecutor;

    public void mqttMessage(String productId, String deviceId, Object payload) {

        final Product product = redisCache.getCacheObject(RedisConstants.PRODUCT + productId);

        if (Objects.isNull(product)) {
            log.info("设备消息处理器：查询产品信息为空为空，产品ID[{}]，设备编号[{}]", productId, deviceId);
            return;
        }

        final Protocol protocol = redisCache.getCacheObject(RedisConstants.PROTOCOL + product.getProtocolId());

        if (Objects.isNull(protocol)) {
            log.info("设备消息处理器：查询协议为空，产品ID[{}]，设备编号[{}]", productId, deviceId);
            return;
        }

        final Object data = parse(payload, protocol.getDataType());

        Device device = deviceService.selectByDeviceId(deviceId);

        if (!Objects.isNull(device) && DeviceConstants.ENABLE.equals(device.getStatus())) {

            deviceExecutor.execute(() -> {

                //动态协议解析数据
                List<JSONObject> deviceJson;
                try {

                    deviceJson = protocolManage.load(protocol.getId(), data);

                } catch (Exception e) {
                    log.error("设备消息处理器，协议解析消息错误：{}", e.getMessage());
                    return;
                }

                log.debug("协议解析数据成功：数据[{}]", deviceJson);

                //判断设备是直连设备还是网关设备
                String deviceType = device.getDeviceType();
                List<DeviceData> deviceDataList = Lists.newArrayList();

                switch (deviceType) {

                    case DeviceConstants.DEVICE:
                        deviceDataList = device(device, deviceJson);
                        break;
                    case DeviceConstants.GATEWAY:
                        deviceDataList = gateway(device, deviceJson);
                        break;
                    default:
                        break;
                }

                if (!CollectionUtils.isEmpty(deviceDataList)) {

                    deviceDataList.forEach(deviceData -> {

                        mqttGateway.sendToMqtt(TopicConstants.DEVICE_RULE_TOPIC, JSONObject.toJSONString(deviceData));

                    });

                }

            });

        } else {

            log.info("无效的设备ID：{}，或设备已停用", deviceId);

        }

    }

    /**
     * TCP设备消息处理
     *
     * @param deviceId 设备id
     * @param payload  数据
     */
    @Override
    public void nettyMessage(String deviceId, Object payload) {

        Device device = deviceService.selectByDeviceId(deviceId);

        mqttMessage(device.getProductId().toString(), deviceId, ByteUtil.hexString2Bytes(payload.toString()));
    }

    /**
     * 普通设备数据处理
     *
     * @param device 设备信息
     * @param data   数据
     * @return 设备数据
     */
    private List<DeviceData> device(Device device, List<JSONObject> data) {

        List<DeviceData> deviceDataList = Lists.newArrayList();

        data.forEach(item -> {

            DeviceData deviceData = new DeviceData();
            deviceData.setData(item.toString());
            deviceData.setDeviceId(device.getDeviceId());
            deviceData.setProductId(device.getProductId().toString());
            deviceData.setDeviceName(device.getDeviceName());
            deviceDataList.add(deviceData);
            //插入influxDB
            influxDbService.insertDeviceData(deviceData);

        });

        return deviceDataList;
    }

    /**
     * 网关设备数据处理
     *
     * @param device 设备信息
     * @param data   数据
     * @return 设备数据
     */
    private List<DeviceData> gateway(Device device, List<JSONObject> data) {

        List<DeviceData> deviceDataList = Lists.newArrayList();

        data.forEach(item -> {

            DeviceData deviceData = new DeviceData();

            //边设备唯一标识，协议必须解析出边设备标识，与边设备管理中的标识对应，如Modbus的地址
            String deviceNo = item.get(DeviceConstants.DEVICE_NO).toString();

            if (StringUtils.isNotEmpty(deviceNo)) {
                //边设备唯一标识和网关id查询边设备信息
                SubDevice subDevice = subDeviceService.getSubDevice(deviceNo, device.getId());
                if (!Objects.isNull(subDevice) && DeviceConstants.ENABLE.equals(subDevice.getStatus())) {

                    deviceData.setDeviceId(subDevice.getDeviceNo());
                    deviceData.setDeviceName(subDevice.getDeviceName());
                    deviceData.setProductId(String.valueOf(subDevice.getProductId()));
                    deviceData.setData(item.toString());

                    deviceDataList.add(deviceData);

                    //插入influxDB
                    influxDbService.insertDeviceData(deviceData);

                } else {
                    log.info("设备消息处理器：未查询到边设备信息，边设备标识[{}]，网关id[{}],或设备已停用", deviceNo, device.getId());
                }
            }

        });

        return deviceDataList;
    }

    /**
     * 数据类型转换
     *
     * @param payload  数据
     * @param dataType 数据类型
     * @return Object
     */
    private static Object parse(Object payload, String dataType) {

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
