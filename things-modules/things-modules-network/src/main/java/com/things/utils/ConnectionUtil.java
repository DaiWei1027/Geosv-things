package com.things.utils;

import com.things.mqtt.utils.MqttUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author DaiWei
 * @date 2023/04/03 16:07
 **/
@Component
@AllArgsConstructor
public class ConnectionUtil {

    public static final String TCP = "TCP";

    public static final String MQTT = "MQTT";

    private final MqttUtils mqttUtils;

    public ConnectionData connectionData(String connectionType , String deviceId, String productId){

        ConnectionData connectionData = new ConnectionData();
        connectionData.setMode(connectionType);

        switch (connectionType){
            case TCP:
                connectionData.setData(new TcpData());
                break;
            case MQTT:
                connectionData.setData(mqttUtils.getDeviceMqttData(deviceId,productId));
                break;
            default:
                break;
        }

        //TODO 添加一个存储


        return connectionData;
    }

    @Data
    public static class ConnectionData{

        String mode;

        Object data;

    }

    @Data
    public static class TcpData{

        String url = "localhost:9001";
    }
}
