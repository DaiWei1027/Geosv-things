package com.things.device.message.execute;

import com.alibaba.fastjson2.JSONObject;
import com.things.common.constant.DeviceConstants;
import com.things.common.constant.RedisConstants;
import com.things.common.constant.TopicConstants;
import com.things.common.core.redis.RedisCache;
import com.things.device.domain.Device;
import com.things.mqtt.mqtt.MqttGateway;
import com.things.product.domain.Product;
import com.things.protocol.domain.Protocol;
import com.things.tcp.config.NettyServerHandler;
import com.things.tcp.handler.NettyMessageHandler;
import com.things.utils.ByteUtil;
import com.things.utils.ConnectionUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 命令执行器
 *
 * @author DaiWei
 * @date 2023/05/19 13:34
 **/
@Slf4j
@Component
@AllArgsConstructor
public class CommandExecute {

    private final MqttGateway mqttGateway;

    private final RedisCache redisCache;

    public void execute(Device device, String command) {

        //查询产品信息
        Product product = redisCache.getCacheObject(RedisConstants.PRODUCT + device.getProductId());
        //查询协议
        Protocol protocol = redisCache.getCacheObject(RedisConstants.PROTOCOL + product.getProtocolId());

        String dataType = protocol.getDataType();

        switch (device.getConnectionType()) {
            case DeviceConstants.TCP:
                tcp(device, command);
                break;
            case DeviceConstants.MQTT:
                mqtt(device, dataType, command);
                break;
            default:
                break;

        }

    }

    private void mqtt(Device device, String dataType, String command) {

        //组装设备订阅的topic
        String topic = TopicConstants.DEVICE_DOWN_TOPIC.replace("productId", device.getProductId().toString()).replace("deviceId", device.getDeviceId());

        switch (dataType) {
            case "BYTE":
                mqttGateway.sendToMqtt(topic, ByteUtil.hexString2Bytes((command)));
                break;
            case "JSON":
                mqttGateway.sendToMqtt(topic, command);
                break;
            default:
                break;

        }

    }

    private void tcp(Device device, String command) {

        ChannelHandlerContext ctx = NettyServerHandler.getChannelHandlerContext(device.getDeviceId());

        if (Objects.isNull(ctx)) {
            throw new RuntimeException("设备不存在，或已离线");
        }

        NettyServerHandler.channelWrite(ctx, command);

    }
}
