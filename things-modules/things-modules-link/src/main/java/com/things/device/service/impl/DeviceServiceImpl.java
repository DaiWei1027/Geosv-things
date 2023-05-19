package com.things.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.things.common.constant.DeviceConstants;
import com.things.common.constant.RedisConstants;
import com.things.common.core.domain.AjaxResult;
import com.things.common.core.redis.RedisCache;
import com.things.common.enums.DeviceStatus;
import com.things.device.domain.Device;
import com.things.device.domain.vo.OnlineCount;
import com.things.device.mapper.DeviceMapper;
import com.things.device.service.IDeviceService;
import com.things.product.domain.Product;
import com.things.product.mapper.ProductMapper;
import com.things.protocol.domain.Protocol;
import com.things.protocol.mapper.ProtocolMapper;
import com.things.utils.ConnectionUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @author DaiWei
 * @date 2023/03/31 13:11
 **/
@Service
@AllArgsConstructor
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    private final DeviceMapper deviceMapper;

    private final ConnectionUtil connectionUtil;

    @Override
    public AjaxResult insert(Device device) {

        String deviceId = device.getDeviceId();

        Integer count = deviceMapper.selectCount(new LambdaQueryWrapper<Device>().eq(Device::getDeviceId, deviceId));

        if (count > 0) {
            return AjaxResult.error("设备ID：[" + deviceId + "] 已存在！");
        }
        device.setCreateTime(new Date());
        device.setConnectionStatus(DeviceConstants.OFFLINE);
        deviceMapper.insert(device);

        ConnectionUtil.ConnectionData connectionData = connectionUtil.connectionData(device.getConnectionType(), deviceId, device.getProductId().toString());

        return AjaxResult.success(connectionData);
    }

    @Override
    public Device selectByDeviceId(String deviceId) {

        return deviceMapper.selectOne(new LambdaQueryWrapper<Device>().eq(Device::getDeviceId, deviceId));

    }


    @Override
    public void offline(String deviceId) {
        deviceMapper.offline(deviceId);
    }

    @Override
    public void online(String deviceId) {
        deviceMapper.online(deviceId);
    }

    @Override
    public OnlineCount onlineCount() {
        return deviceMapper.onlineCount();
    }
}
