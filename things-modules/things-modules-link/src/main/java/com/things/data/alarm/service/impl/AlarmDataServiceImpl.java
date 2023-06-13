package com.things.data.alarm.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.things.data.alarm.domain.DataCount;
import com.things.data.alarm.service.IAlarmDataService;
import com.things.common.utils.DateUtils;
import com.things.common.utils.StringUtils;
import com.things.device.domain.vo.DeviceVo;
import com.things.influxdb.vo.DeviceAlarm;
import com.things.influxdb.vo.DeviceData;
import com.things.rule.domain.action.AlarmAction;
import com.things.rule.domain.action.vo.AlarmActionVo;
import com.things.rule.mapper.AlarmActionMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import plus.ojbk.influxdb.core.InfluxdbTemplate;
import plus.ojbk.influxdb.core.Op;
import plus.ojbk.influxdb.core.Query;
import plus.ojbk.influxdb.core.enums.Order;
import plus.ojbk.influxdb.core.model.QueryModel;
import plus.ojbk.influxdb.util.InfluxdbUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author DaiWei
 * @date 2023/05/24 15:43
 **/
@Service
@AllArgsConstructor
public class AlarmDataServiceImpl implements IAlarmDataService {

    private final InfluxdbTemplate influxdbTemplate;

    private final AlarmActionMapper alarmActionMapper;

    @Override
    public Page<AlarmAction> pageAlarmAction(AlarmActionVo alarmActionVo) {
        Page<AlarmAction> page = new Page<>(alarmActionVo.getPageNum(), alarmActionVo.getPageSize());
        return alarmActionMapper.selectPage(page, new LambdaQueryWrapper<AlarmAction>()
                .eq(StringUtils.isNotEmpty(alarmActionVo.getTitle()), AlarmAction::getTitle, alarmActionVo.getTitle())
                .eq(StringUtils.isNotEmpty(alarmActionVo.getLevel()), AlarmAction::getLevel, alarmActionVo.getLevel())
        );
    }

    @Override
    public Page<DeviceAlarm> pageAlarm(DeviceVo deviceVo) {
        QueryModel queryModel = new QueryModel();

        String measurement = InfluxdbUtils.getMeasurement(DeviceAlarm.class);

        queryModel.setMeasurement(measurement);

        buildQueryModel(queryModel, deviceVo);

        Page<DeviceAlarm> deviceDataPage = new Page<>(deviceVo.getPageNum(), deviceVo.getPageSize());

        List<DeviceAlarm> deviceAlarms = influxdbTemplate.selectList(Query.build(queryModel), DeviceAlarm.class);

        queryModel.setCurrent(0L);
        queryModel.setSize(0L);
        queryModel.setSelect(Query.count(InfluxdbUtils.getCountField(DeviceData.class)));
        queryModel.setWhere(Op.where(queryModel));

        long total = influxdbTemplate.count(Query.build(queryModel));

        deviceDataPage.setRecords(deviceAlarms);
        int pages = PageUtil.totalPage((int) total, deviceVo.getPageSize());
        deviceDataPage.setPages(pages);
        deviceDataPage.setTotal(total);

        return deviceDataPage;
    }

    @Override
    public DataCount alarmCount() {
        QueryModel queryModel = new QueryModel();

        String measurement = InfluxdbUtils.getMeasurement(DeviceAlarm.class);

        queryModel.setMeasurement(measurement);

        queryModel.setSelect(Query.count(InfluxdbUtils.getCountField(DeviceAlarm.class)));

        buildTimeModel(queryModel, DateUtils.getNowDateStartTime(), DateUtils.getNowDateEndTime());

        DataCount dataCount = new DataCount();

        dataCount.setTodayCount(influxdbTemplate.count(Query.build(queryModel)));

        buildTimeModel(queryModel, DateUtils.firstDayOfMonth(), DateUtils.lastDayOfMonth());

        dataCount.setMonthCount(influxdbTemplate.count(Query.build(queryModel)));

        return dataCount;
    }

    public void buildQueryModel(QueryModel queryModel, DeviceVo deviceVo) {

        queryModel.setUseTimeZone(true);
        Map<String, Object> whereMap = Maps.newHashMap();

        if (StringUtils.isNotEmpty(deviceVo.getDeviceId())) {
            whereMap.put("device_id", deviceVo.getDeviceId());
        }
        if (!Objects.isNull(deviceVo.getProductId())) {
            whereMap.put("product_id", deviceVo.getProductId().toString());
        }
        queryModel.setMap(whereMap);

        buildTimeModel(queryModel, deviceVo.getStartTime(), deviceVo.getEndTime());

        queryModel.setCurrent((long) deviceVo.getPageNum());
        queryModel.setSize((long) deviceVo.getPageSize());

        queryModel.setOrder(Order.DESC);

        queryModel.setWhere(Op.where(queryModel));

    }

    public void buildTimeModel(QueryModel queryModel, Date startTime, Date endTime) {
        queryModel.setUseTimeZone(true);
        if (!Objects.isNull(startTime)) {
            queryModel.setStart(LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()));
        }
        if (!Objects.isNull(endTime)) {
            queryModel.setEnd(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()));
        }

        queryModel.setWhere(Op.where(queryModel));
    }
}
