package com.things.tcp.handler;

/**
 * @author DaiWei
 * @date 2023/05/17 13:55
 **/
public interface TCPDeviceOnlineStatus {
    /**
     * 设备上线
     *
     * @param deviceId 设备ID
     */
    void online(String deviceId);

    /**
     * 设备下线
     * @param deviceId 设备ID
     */
    void offline(String deviceId);
}
