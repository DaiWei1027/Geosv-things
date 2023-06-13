package com.things.web.controller.link;

import com.things.data.alarm.service.IAlarmDataService;
import com.things.common.core.domain.AjaxResult;
import com.things.device.domain.vo.DeviceVo;
import com.things.rule.domain.action.vo.AlarmActionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 告警中心
 *
 * @author DaiWei
 * @date 2023/05/24 15:28
 **/
@Api(tags = "告警中心")
@RestController
@RequestMapping("/alarmData")
@AllArgsConstructor
public class AlarmDataController {

    private final IAlarmDataService alarmDataService;

    @ApiOperation("查询告警配置")
    @PostMapping("/pageAlarm")
    public AjaxResult pageAlarm(@RequestBody AlarmActionVo alarmActionVo){

        return AjaxResult.success(alarmDataService.pageAlarmAction(alarmActionVo));
    }

    @ApiOperation("设备告警记录")
    @PostMapping("/page")
    public AjaxResult pageAlarm(@RequestBody DeviceVo deviceVo){

        return AjaxResult.success(alarmDataService.pageAlarm(deviceVo));
    }

    @ApiOperation("统计当日当月告警数量")
    @GetMapping("/count")
    public AjaxResult count(){

        return AjaxResult.success(alarmDataService.alarmCount());
    }


}
