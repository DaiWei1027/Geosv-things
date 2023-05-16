package com.things.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.things.common.core.domain.AjaxResult;
import com.things.device.domain.Device;
import com.things.device.domain.SubDevice;
import com.things.device.mapper.DeviceMapper;
import com.things.device.mapper.SubDeviceMapper;
import com.things.device.service.IDeviceService;
import com.things.device.service.ISubDeviceService;
import com.things.utils.ConnectionUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author DaiWei
 * @date 2023/03/31 13:11
 **/
@Service
@AllArgsConstructor
public class SubDeviceServiceImpl extends ServiceImpl<SubDeviceMapper, SubDevice> implements ISubDeviceService {

    private final SubDeviceMapper subDeviceMapper;

    @Override
    public AjaxResult insert(SubDevice subDevice) {

        String subDeviceNo = subDevice.getSubDeviceNo();

        Integer count = subDeviceMapper.selectCount(new LambdaQueryWrapper<SubDevice>().eq(SubDevice::getSubDeviceNo, subDeviceNo));

        if (count > 0) {
            return AjaxResult.error("设备ID：[" + subDeviceNo + "] 已存在！");
        }

        subDeviceMapper.insert(subDevice);

        return AjaxResult.success();
    }


}
