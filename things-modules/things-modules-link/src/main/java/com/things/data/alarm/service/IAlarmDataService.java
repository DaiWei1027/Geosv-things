package com.things.data.alarm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.things.data.alarm.domain.DataCount;
import com.things.device.domain.vo.DeviceVo;
import com.things.influxdb.vo.DeviceAlarm;
import com.things.rule.domain.action.AlarmAction;
import com.things.rule.domain.action.vo.AlarmActionVo;

/**
 * @author DaiWei
 * @date 2023/05/24 15:43
 **/
public interface IAlarmDataService {

    /**
     * 统计本日本月告警数量
     *
     * @return DataCount
     */
    DataCount alarmCount();

    /**
     * 分页查询告警记录
     *
     * @param deviceVo 查询参数
     * @return List<DeviceAlarm>
     */
    Page<DeviceAlarm> pageAlarm(DeviceVo deviceVo);

    /**
     * 查询告警配置
     *
     * @param alarmActionVo 查询参数
     * @return 告警配置
     */
    Page<AlarmAction> pageAlarmAction(AlarmActionVo alarmActionVo);
}
