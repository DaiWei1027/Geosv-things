package com.things.device.bean;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.things.common.constant.SseEmitterConstants;
import com.things.device.domain.Device;
import com.things.influxdb.vo.DeviceData;
import com.things.product.domain.ProdEvent;
import com.things.sse.RealTimeLogServer;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author DaiWei
 * @date 2023/05/19 11:32
 **/
@Component
public class RealTimeLogHandler {

    private void push(String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(SseEmitterConstants.EVENT_TYPE,SseEmitterConstants.LOG);
        jsonObject.put("data",message);
        RealTimeLogServer.batchSendMessage(jsonObject.toString());
    }

    /**
     * 推送设备上报原始数据
     *
     * @param device 设备信息
     * @param data 数据
     */
    public void send(Device device, Object data) {

        if (RealTimeLogServer.getUserCount() > 0){

            String log = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");

            log += " - 上报数据：" + data.toString();

            log += ", 设备ID：" + device.getDeviceId();

            log += ", 设备名称：" + device.getDeviceName();

            push(log);
        }

    }

    public void fail(String message){

        if (RealTimeLogServer.getUserCount() > 0){

            String log = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");

            log += " - 解析失败：" + message;

            push(log);
        }

    }


    public void sendLog(ProdEvent prodEvent, DeviceData deviceData) {

        if (RealTimeLogServer.getUserCount() > 0) {

            String log = DateUtil.format(deviceData.getTime(), "yyyy-MM-dd HH:mm:ss");

            log += " - 解析成功：设备ID：" + deviceData.getDeviceId();

            log += "，设备名称：" + deviceData.getDeviceName();

            log += "，事件名称：" + prodEvent.getEventName();

            log += "，数据：" + deviceData.getData();

            push(log);
        }

    }
}
