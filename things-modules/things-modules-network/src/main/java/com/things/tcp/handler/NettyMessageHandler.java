package com.things.tcp.handler;

import com.things.mqtt.utils.SpringUtil;

/**
 * @author DaiWei
 * @date 2023/05/17 10:09
 **/
public interface NettyMessageHandler {

    /**
     * netty消息处理
     *
     * @param deviceId 设备id
     * @param payload  数据
     */
    public void nettyMessage(String deviceId, Object payload);

    /**
     * 通过IP地址查询设备
     *
     * @param ipAddr IP地址
     * @return 布尔
     */
    public boolean countDevice(String ipAddr);
}
