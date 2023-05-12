package com.things.mqtt.domain;

import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/04/03 15:58
 **/
@Data
public class MqttParam {

    String url;

    String userName;

    String password;

    String publish;

    String subscription;
}
