package com.things.device.bean;

import cn.hutool.core.date.DateUtil;
import com.things.influxdb.vo.DeviceData;
import com.things.product.domain.ProdEvent;
import com.things.sse.RealTimeLogServer;
import org.springframework.stereotype.Component;

/**
 * @author DaiWei
 * @date 2023/05/19 11:32
 **/
@Component
public class RealTimeLogHandler {


    public void sendLog(ProdEvent prodEvent, DeviceData deviceData) {

        if (RealTimeLogServer.getUserCount() > 0) {

            String log = DateUtil.format(deviceData.getTime(),"yyyy-MM-dd HH:mm:ss");

            log += " - 设备ID：" + deviceData.getDeviceId();

            log += "，设备名称：" + deviceData.getDeviceName();

            log += "，事件名称：" + prodEvent.getEventName();

            log += "，数据：" + deviceData.getData();

            RealTimeLogServer.batchSendMessage(log);
        }

    }
}
