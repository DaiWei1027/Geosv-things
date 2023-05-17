package com.things.device.message.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.things.common.utils.StringUtils;
import com.things.device.service.IDeviceService;
import com.things.mqtt.aop.MqttService;
import com.things.mqtt.aop.MqttTopic;
import com.things.tcp.handler.TCPDeviceOnlineStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

/**
 * emqx上下线监听
 *
 * @author Daiwei
 * @version 1.0.0
 * @date 2022/10/22
 */
@Slf4j
@MqttService
public class DeviceOnlineStatusListener implements TCPDeviceOnlineStatus {

    /**
     * 监听设备上下线的主题
     */
    public static final String CONNECTED_STATUS_TOPIC = "$SYS/brokers/+/clients/#";

    /**
     * 上线标识
     */
    public static final String CONNECTED = "connected";

    /**
     * 下线标识
     */
    public static final String DISCONNECTED = "disconnected";

    @Autowired
    private IDeviceService deviceService;


    /**
     * 处理上下线通知的逻辑
     * 客户端在线状态用数据库来记录
     *
     * @param message 消息体
     */
    @MqttTopic(CONNECTED_STATUS_TOPIC)
    public void connectedMessage(Message<?> message) {
        String payload = message.getPayload().toString();
        try {
            //获取主题名称
            Object topicObject = message.getHeaders().get("mqtt_receivedTopic");
            //解析消息体
            JSONObject payloadJson = JSON.parseObject(payload);
            //获取设备id
            String deviceId = payloadJson.get("clientid").toString();
            if (null != topicObject && StringUtils.isNotEmpty(deviceId)) {
                String topic = topicObject.toString();
                if (topic.endsWith(DISCONNECTED)) {
                    deviceService.offline(deviceId);
                    log.info("设备[{}]下线", deviceId);
                } else if (topic.endsWith(CONNECTED)) {
                    deviceService.online(deviceId);
                    log.info("设备[{}]上线", deviceId);
                } else {
                    log.info("设备[{}]消息中不包含上下线标识,topic[{}]", deviceId, topic);
                }
            }
        } catch (JSONException e) {
            log.error("上下线监听器解析message错误,{}，message:{}", e.getMessage(), message);
        }
    }

    @Override
    public void online(String deviceId) {
        deviceService.online(deviceId);
    }

    @Override
    public void offline(String deviceId) {
        deviceService.offline(deviceId);
    }
}
