package com.things.mqtt.mqtt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * mqtt 参数
 * @author daiwei
 * @Date 2022/10/20
 */
@Data
@Component
public class MqttProperties {

    /**
     * 用户名
     */
    @Value("${mqtt.username}")
    private String username;

    /**
     * 密码
     */
    @Value("${mqtt.password}")
    private String password;

    /**
     * 连接地址
     */
    @Value("${mqtt.host-url}")
    private String hostUrl;

    /**
     * 进-客户Id
     */
    @Value("${mqtt.in-client-id}")
    private String inClientId;

    /**
     * 出-客户Id
     */
    @Value("${mqtt.out-client-id}")
    private String outClientId;

    /**
     * 客户Id
     */
    @Value("${mqtt.client-id}")
    private String clientId;

    /**
     * 默认连接话题
     */
    @Value("${mqtt.default-topic}")
    private String defaultTopic;

    /**
     * 超时时间
     */
    @Value("${mqtt.timeout}")
    private int timeout;

    /**
     * 保持连接数
     */
    @Value("${mqtt.keepalive}")
    private int keepalive;

    /**
     * 是否清除session
     */
    @Value("${mqtt.clearSession}")
    private boolean clearSession;

}
