package com.things.mqtt.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * MqttGateway
 *
 * @author daiwei
 * @date 2022/10/20
 */

@Component
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {

    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String data);

    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, byte[] data);

    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) Integer Qos, String data);
}

