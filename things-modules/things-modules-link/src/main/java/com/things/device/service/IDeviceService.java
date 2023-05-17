package com.things.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.things.common.core.domain.AjaxResult;
import com.things.device.domain.Device;

/**
 * @author DaiWei
 * @date 2023/03/31 13:10
 **/
public interface IDeviceService extends IService<Device> {

    /**
     * 新增
     *
     * @param device 设备参数
     * @return AjaxResult
     */
    AjaxResult insert(Device device);


    /**
     * 通过设备编号查询
     *
     * @param deviceId 设备编号
     * @return Device
     */
    Device selectByDeviceId(String deviceId);

    /**
     * 设备下线
     *
     * @param deviceId 设备id
     */
    void offline(String deviceId);

    /**
     * 设备下线
     *
     * @param deviceId 设备id
     */
    void online(String deviceId);
}
