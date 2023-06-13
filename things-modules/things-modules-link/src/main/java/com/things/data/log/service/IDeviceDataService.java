package com.things.data.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.things.data.alarm.domain.Dashboard;
import com.things.data.alarm.domain.DataCount;
import com.things.device.domain.vo.DeviceVo;
import com.things.influxdb.vo.DeviceData;

import java.util.TreeMap;


/**
 * @author DaiWei
 * @date 2023/05/25 09:50
 **/
public interface IDeviceDataService {

    /**
     * 统计本日本月数量
     *
     * @return DataCount
     */
    DataCount count();

    /**
     * 分页查询设备历史数据
     *
     * @param deviceVo 查询参数
     * @return List<DeviceData>
     */
    Page<DeviceData> page(DeviceVo deviceVo);

    /**
     * 统计设备消息
     *
     * @param dashboard 查询参数
     * @return TreeMap<String,Long>
     */
    TreeMap<String,Long> multi(Dashboard dashboard);

}
