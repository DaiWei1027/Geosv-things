package com.things.web.controller.link;


import com.things.common.core.domain.AjaxResult;
import com.things.data.alarm.domain.Dashboard;
import com.things.data.log.service.IDeviceDataService;
import com.things.device.domain.vo.DeviceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 设备历史数据
 *
 * @author DaiWei
 * @date 2023/04/18 16:38
 **/
@Api(tags = "设备数据")
@RestController
@RequestMapping("/deviceData")
@AllArgsConstructor
public class DeviceDataController {

    private final IDeviceDataService deviceDataService;

    @ApiOperation("设备历史数据")
    @PostMapping("/device_history")
    public AjaxResult pageDeviceHistory(@RequestBody DeviceVo deviceVo){

        return AjaxResult.success(deviceDataService.page(deviceVo));
    }

    @ApiOperation("统计本日本月数量")
    @GetMapping("/count")
    public AjaxResult count(){

        return AjaxResult.success(deviceDataService.count());
    }

    @ApiOperation("设备消息统计")
    @PostMapping("/_multi")
    public AjaxResult multi(@RequestBody Dashboard dashboard){

        return AjaxResult.success(deviceDataService.multi(dashboard));
    }
}
