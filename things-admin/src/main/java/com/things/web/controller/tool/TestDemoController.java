package com.things.web.controller.tool;

import com.things.common.dynamicCompilation.bytecode.ByteUtils;
import com.things.mqtt.mqtt.MqttGateway;
import com.things.utils.ByteUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaiWei
 * @date 2023/05/11 09:27
 **/
@Api("用户信息管理")
@RestController
@RequestMapping("/test/test")
public class TestDemoController {

    @Autowired
    @Qualifier("deviceExecutor")
    private ThreadPoolTaskExecutor deviceExecutor;
    @Autowired
    private MqttGateway mqttGateway;

    @ApiOperation("MQTT并发测试")
    @RequestMapping("/mqtt")
    public void mqttTest(){

        deviceExecutor.execute(() -> {

            for (int i = 0; i < 1000; i++) {

                mqttGateway.sendToMqtt("/v1/device/upload/message/5/3", ByteUtil.hexString2Bytes("010308001A001E001E001EA615"));

            }

        });

    }
}
