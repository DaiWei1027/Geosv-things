package com.things.mqtt.utils;

import com.things.common.constant.TopicConstants;
import com.things.mqtt.domain.MqttParam;
import com.things.mqtt.mqtt.MqttProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author DaiWei
 * @date 2023/04/03 15:57
 **/
@Component
public class MqttUtils {

    @Autowired
    private MqttProperties mqttProperties;

    public MqttParam getDeviceMqttData(String deviceId,String productId){

        MqttParam mqttParam = new MqttParam();
        mqttParam.setUrl(mqttProperties.getHostUrl().split(",")[0]);
        mqttParam.setUserName(mqttProperties.getUsername());
        mqttParam.setPassword(mqttProperties.getPassword());

        mqttParam.setPublish(TopicConstants.DEVICE_UPLOAD_TOPIC.replace("productId",productId).replace("deviceId",deviceId));
        mqttParam.setSubscription(TopicConstants.DEVICE_DOWN_TOPIC.replace("productId",productId).replace("deviceId",deviceId));

        return mqttParam;
    }
}
