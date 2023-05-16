package com.things.web.controller.link;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.things.common.core.controller.BaseController;
import com.things.common.core.domain.AjaxResult;
import com.things.common.utils.StringUtils;
import com.things.device.domain.SubDevice;
import com.things.device.domain.vo.SubDeviceVo;
import com.things.device.service.ISubDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaiWei
 * @date 2023/04/03 15:20
 **/
@Api(tags = "设备管理")
@RestController
@RequestMapping("/link/SubDevice")
public class SubDeviceController extends BaseController {

    @Autowired
    private ISubDeviceService subDeviceService;

    @ApiOperation("新增")
    @RequestMapping("/insert")
    public AjaxResult insert(@RequestBody SubDevice subDevice){
        subDevice.setCreateBy(getUsername());
        return subDeviceService.insert(subDevice);
    }

    @ApiOperation("更新")
    @RequestMapping("/update")
    public AjaxResult update(@RequestBody SubDevice subDevice){

        SubDevice subDeviceById = subDeviceService.getById(subDevice.getId());

        if (subDeviceById.getDeviceNo().equals(subDevice.getDeviceNo())){
            return AjaxResult.error("设备编码不能修改");
        }
        

        subDevice.setUpdateBy(getUsername());
        return AjaxResult.success(subDeviceService.updateById(subDevice));
    }

    @ApiOperation("启用|停用")
    @RequestMapping("/status")
    public AjaxResult status(@RequestBody SubDevice subDevice){

        SubDevice subDeviceById = subDeviceService.getById(subDevice.getId());
        
        subDevice.setUpdateBy(getUsername());
        return AjaxResult.success(subDeviceService.updateById(subDevice));
    }

    @ApiOperation("分页查询")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody SubDeviceVo subDeviceVo){

        Page<SubDevice> page = new Page<>(subDeviceVo.getPageNum(), subDeviceVo.getPageSize());

        Page<SubDevice> pageData = subDeviceService.page(page, new LambdaQueryWrapper<SubDevice>()
                .eq(StringUtils.isNotEmpty(subDeviceVo.getDeviceNo()), SubDevice::getDeviceNo, subDeviceVo.getDeviceNo())
                .like(StringUtils.isNotEmpty(subDeviceVo.getDeviceName()), SubDevice::getDeviceName, subDeviceVo.getDeviceName())
                .eq(StringUtils.isNotEmpty(subDeviceVo.getConnectionStatus()), SubDevice::getConnectionStatus, subDeviceVo.getConnectionStatus())
                .eq(StringUtils.isNotEmpty(subDeviceVo.getStatus()), SubDevice::getStatus, subDeviceVo.getStatus())
        );

        return AjaxResult.success(pageData);
    }
}
