package com.things.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author DaiWei
 * @date 2023/04/13 09:44
 **/
@Data
@Component
@ConfigurationProperties(prefix = "netty")
public class NettyConfig {

    private Integer bossGroup;
    private Integer workGroup;

    private Integer queueSize;

    private Integer port;
}
