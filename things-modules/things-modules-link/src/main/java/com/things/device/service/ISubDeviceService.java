package com.things.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.things.common.core.domain.AjaxResult;
import com.things.device.domain.Device;
import com.things.device.domain.SubDevice;

/**
 * @author DaiWei
 * @date 2023/03/31 13:10
 **/
public interface ISubDeviceService extends IService<SubDevice> {

    /**
     * 新增
     *
     * @param device 设备参数
     * @return AjaxResult
     */
    AjaxResult insert(SubDevice device);

    /**
     * 边设备唯一编号和网关设备id查询
     *
     * @param deviceNo  边设备唯一编号
     * @param gatewayId 网关id
     * @return SubDevice
     */
    SubDevice getSubDevice(String deviceNo, int gatewayId);
}
