package com.things.web.controller.link;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.things.common.constant.DeviceConstants;
import com.things.common.core.controller.BaseController;
import com.things.common.core.domain.AjaxResult;
import com.things.common.utils.StringUtils;
import com.things.device.domain.Device;
import com.things.device.domain.vo.DeviceVo;
import com.things.device.domain.vo.OnlineCount;
import com.things.device.service.IDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author DaiWei
 * @date 2023/04/03 15:20
 **/
@Api(tags = "设备管理")
@RestController
@RequestMapping("/link/device")
public class DeviceController extends BaseController {

    @Autowired
    private IDeviceService deviceService;

    @ApiOperation("新增")
    @PostMapping("/insert")
    public AjaxResult insert(@RequestBody Device device) {
        device.setCreateBy(getUsername());
        return deviceService.insert(device);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public AjaxResult update(@RequestBody Device device) {

        Device deviceById = deviceService.getById(device.getId());

        if (deviceById.getDeviceId().equals(device.getDeviceId())) {
            return AjaxResult.error("设备编码不能修改");
        }

        if (deviceById.getConnectionType().equals(device.getConnectionType())) {
            return AjaxResult.error("接入方式不能修改");
        }

        device.setUpdateBy(getUsername());
        device.setUpdateTime(new Date());
        return AjaxResult.success(deviceService.updateById(device));
    }

    @ApiOperation("启用|停用")
    @GetMapping("/status")
    public AjaxResult status(Integer id, String status) {
        Device device = deviceService.getById(id);
        device.setStatus(status);
        device.setUpdateBy(getUsername());
        return AjaxResult.success(deviceService.updateById(device));
    }

    @ApiOperation("分页查询")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody DeviceVo deviceVo) {

        Page<Device> page = new Page<>(deviceVo.getPageNum(), deviceVo.getPageSize());

        Page<Device> pageData = deviceService.page(page, new LambdaQueryWrapper<Device>()
                .eq(StringUtils.isNotEmpty(deviceVo.getDeviceId()), Device::getDeviceId, deviceVo.getDeviceId())
                .like(StringUtils.isNotEmpty(deviceVo.getDeviceName()), Device::getDeviceName, deviceVo.getDeviceName())
                .eq(StringUtils.isNotEmpty(deviceVo.getConnectionStatus()), Device::getConnectionStatus, deviceVo.getConnectionStatus())
                .eq(null != deviceVo.getProductId(), Device::getProductId, deviceVo.getProductId())
                .eq(StringUtils.isNotEmpty(deviceVo.getDeviceType()), Device::getDeviceType, deviceVo.getDeviceType())
        );

        return AjaxResult.success(pageData);
    }

    @ApiOperation("在线离线")
    @PostMapping("/onlineCount")
    public AjaxResult onlineCount(){

        OnlineCount onlineCount = deviceService.onlineCount();

        return AjaxResult.success(onlineCount);
    }
}
