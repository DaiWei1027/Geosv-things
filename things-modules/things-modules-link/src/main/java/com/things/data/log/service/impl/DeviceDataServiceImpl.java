package com.things.data.log.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.things.common.utils.DateUtils;
import com.things.common.utils.StringUtils;
import com.things.data.alarm.domain.Dashboard;
import com.things.data.alarm.domain.DataCount;
import com.things.data.log.service.IDeviceDataService;
import com.things.device.domain.vo.DeviceVo;
import com.things.influxdb.vo.DeviceAlarm;
import com.things.influxdb.vo.DeviceData;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import plus.ojbk.influxdb.core.InfluxdbTemplate;
import plus.ojbk.influxdb.core.Op;
import plus.ojbk.influxdb.core.Query;
import plus.ojbk.influxdb.core.enums.Order;
import plus.ojbk.influxdb.core.model.QueryModel;
import plus.ojbk.influxdb.util.InfluxdbUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author DaiWei
 * @date 2023/05/25 09:51
 **/
@Service
@AllArgsConstructor
public class DeviceDataServiceImpl implements IDeviceDataService {

    private final InfluxdbTemplate influxdbTemplate;

    public static final String DEVICE = "device";
    public static final String ALARM = "alarm";

    public static final String DAY = "D";

    public static final String MONTH = "M";

    @Override
    public Page<DeviceData> page(DeviceVo deviceVo) {
        QueryModel queryModel = new QueryModel();

        String measurement = InfluxdbUtils.getMeasurement(DeviceData.class);

        queryModel.setMeasurement(measurement);

        buildQueryModel(queryModel, deviceVo);

        Page<DeviceData> deviceDataPage = new Page<>(deviceVo.getPageNum(), deviceVo.getPageSize());

        List<DeviceData> deviceData = influxdbTemplate.selectList(Query.build(queryModel), DeviceData.class);

        queryModel.setCurrent(0L);
        queryModel.setSize(0L);
        queryModel.setSelect(Query.count(InfluxdbUtils.getCountField(DeviceData.class)));
        queryModel.setWhere(Op.where(queryModel));

        long total = influxdbTemplate.count(Query.build(queryModel));

        deviceDataPage.setRecords(deviceData);
        int pages = PageUtil.totalPage((int) total, deviceVo.getPageSize());
        deviceDataPage.setPages(pages);
        deviceDataPage.setTotal(total);

        return deviceDataPage;
    }

    @Override
    public DataCount count() {

        QueryModel queryModel = new QueryModel();

        String measurement = InfluxdbUtils.getMeasurement(DeviceData.class);

        queryModel.setMeasurement(measurement);

        queryModel.setSelect(Query.count(InfluxdbUtils.getCountField(DeviceData.class)));

        buildTimeModel(queryModel, DateUtils.getNowDateStartTime(), DateUtils.getNowDateEndTime());

        DataCount dataCount = new DataCount();

        dataCount.setTodayCount(influxdbTemplate.count(Query.build(queryModel)));

        buildTimeModel(queryModel, DateUtils.firstDayOfMonth(), DateUtils.lastDayOfMonth());

        dataCount.setMonthCount(influxdbTemplate.count(Query.build(queryModel)));

        return dataCount;

    }

    @Override
    public TreeMap<String,Long> multi(Dashboard dashboard) {

        QueryModel queryModel = new QueryModel();

        String measurement;

        switch (dashboard.getType()) {
            case DEVICE:
                queryModel.setSelect(Query.count(InfluxdbUtils.getCountField(DeviceData.class)));
                measurement = InfluxdbUtils.getMeasurement(DeviceData.class);
                break;
            case ALARM:
                queryModel.setSelect(Query.count(InfluxdbUtils.getCountField(DeviceAlarm.class)));
                measurement = InfluxdbUtils.getMeasurement(DeviceAlarm.class);
                break;
            default:
                throw new RuntimeException("分组参数不正确");
        }

        queryModel.setMeasurement(measurement);

        LocalDate startTime = dashboard.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endTime = dashboard.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        List<LocalDate> datesBetween = Lists.newArrayList();

        if (DAY.equals(dashboard.getGroup())){
            datesBetween = DateUtils.getDatesBetween(startTime, endTime);
        }
        else if (MONTH.equals(dashboard.getGroup())){
            datesBetween = DateUtils.getMonthBetween(startTime, endTime);
        }

        TreeMap<String,Long> treeMap = Maps.newTreeMap();

        for (LocalDate localDate : datesBetween) {

            if (DAY.equals(dashboard.getGroup())){
                buildTimeModel(queryModel, DateUtils.getDateStartTime(localDate), DateUtils.getDateEndTime(localDate));
                treeMap.put(localDate.toString(),influxdbTemplate.count(Query.build(queryModel)));
            }
            else if (MONTH.equals(dashboard.getGroup())){
                buildTimeModel(queryModel, DateUtils.getFirstDayOfMonth(localDate), DateUtils.getLastDayOfMonth(localDate));
                treeMap.put(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM")),influxdbTemplate.count(Query.build(queryModel)));
            }
        }

        return treeMap;
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
        if (!Objects.isNull(deviceVo.getStartTime())) {
            queryModel.setStart(LocalDateTime.ofInstant(deviceVo.getStartTime().toInstant(), ZoneId.systemDefault()));
        }
        if (!Objects.isNull(deviceVo.getEndTime())) {
            queryModel.setEnd(LocalDateTime.ofInstant(deviceVo.getEndTime().toInstant(), ZoneId.systemDefault()));
        }

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
