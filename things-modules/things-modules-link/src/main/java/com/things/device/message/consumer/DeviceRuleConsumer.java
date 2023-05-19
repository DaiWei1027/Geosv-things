package com.things.device.message.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.things.common.constant.TopicConstants;
import com.things.influxdb.vo.DeviceData;
import com.things.mqtt.aop.MqttService;
import com.things.mqtt.aop.MqttTopic;
import com.things.rule.bean.RuleHandler;
import com.things.utils.ByteUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;

/**
 * 设备消息-规则处理
 *
 * @author DaiWei
 * @date 2023/05/08 14:45
 **/
@Slf4j
@MqttService
@AllArgsConstructor
public class DeviceRuleConsumer {

    private final RuleHandler ruleHandler;

    /**
     * 设备消息按规则处理
     *
     * @param message 设备消息
     */
    @MqttTopic(TopicConstants.DEVICE_RULE_TOPIC)
    public void onMessage(Message<?> message) {

        log.info("设备消息-规则处理消费者收到消息：[{}]", message.getPayload());

        String payloadStr = ByteUtil.bytes2String((byte[]) message.getPayload());

        DeviceData deviceData = JSONObject.parseObject(payloadStr, DeviceData.class);

        ruleHandler.handle(deviceData);
    }
}
