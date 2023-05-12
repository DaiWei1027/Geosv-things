package com.things.device.message.consumer;

import com.things.device.message.handler.DeviceMessageHandler;
import com.things.mqtt.aop.MqttService;
import com.things.mqtt.aop.MqttTopic;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;

/**
 * @author DaiWei
 * @date 2023/04/26 15:11
 **/

@MqttService
@AllArgsConstructor
public class DeviceMessageConsumer {

    public static final Logger log = LoggerFactory.getLogger(DeviceMessageConsumer.class);

    public static final String MQTT_RECEIVED_TOPIC = "mqtt_receivedTopic";

    private final DeviceMessageHandler deviceMessageHandler;

    /**
     * mqtt设备消息接收
     * 主题示例：/v1/device/upload/{protocolId}/{deviceId}
     *
     * @param message
     */
    @MqttTopic("/v1/device/upload/message/#")
    public void onMessage(Message<?> message) {

        log.info("设备消息消费者收到消息：[{}]", message.getPayload());

        String receivedTopic = message.getHeaders().get(MQTT_RECEIVED_TOPIC, String.class);

        String deviceId = receivedTopic.substring(receivedTopic.lastIndexOf("/") + 1);

        String productId = receivedTopic.substring(receivedTopic.lastIndexOf("/", receivedTopic.lastIndexOf("/") - 1) + 1, receivedTopic.lastIndexOf("/"));

        final Object payload = message.getPayload();

        deviceMessageHandler.handleMessage(productId, deviceId, payload);
    }
}
