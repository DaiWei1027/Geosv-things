package com.things.udp;

import io.swagger.annotations.Api;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Version 1.0
 * @Author meng.zhang6@hirain.com
 * @Created Date:2023/2/13 18:29
 * @Description <p>
 * @Modification <p>
 * Date Author Version Description
 * <p>
 * 2023/2/13 18:29 meng.zhang6@hirain.com 1.0 create file
 */

@Component
@Api(tags = "与嵌入式通信的配置文件")
@ConfigurationProperties(prefix = "udp")
public class UdpConfig {

    /**
     * 与嵌入式通信的udp监听端口
     */
    private static Integer listeningPort;

    /**
     * 与嵌入式通信的udp发送端口
     */
    private static Integer sendingPort;

    @Value("${udp.listeningPort}")
    public void setListeningPort(Integer listeningPort) {
        UdpConfig.listeningPort = listeningPort;
    }

    @Value("${udp.sendingPort}")
    public void setSendingPort(Integer sendingPort) {
        UdpConfig.sendingPort = sendingPort;
    }

    public static Integer getListeningPort() {
        return listeningPort;
    }

    public static Integer getSendingPort() {
        return sendingPort;
    }
}

