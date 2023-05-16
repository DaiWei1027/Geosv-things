package com.things.web.controller.link;


import com.google.common.collect.Maps;
import com.things.common.core.domain.AjaxResult;
import com.things.common.utils.StringUtils;
import com.things.device.domain.vo.DeviceVo;
import com.things.influxdb.vo.DeviceAlarm;
import com.things.influxdb.vo.DeviceData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import plus.ojbk.influxdb.core.InfluxdbTemplate;
import plus.ojbk.influxdb.core.Op;
import plus.ojbk.influxdb.core.Query;
import plus.ojbk.influxdb.core.enums.Order;
import plus.ojbk.influxdb.core.model.QueryModel;
import plus.ojbk.influxdb.util.InfluxdbUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author DaiWei
 * @date 2023/04/18 16:38
 **/
@Api(tags = "设备数据")
@RestController
@RequestMapping("/deviceData")
@AllArgsConstructor
public class DeviceDataController {

    private final InfluxdbTemplate influxdbTemplate;

    @ApiOperation("设备历史数据")
    @PostMapping("/device_history")
    public AjaxResult pageDeviceHistory(@RequestBody DeviceVo deviceVo){

        QueryModel queryModel = new QueryModel();

        String measurement = InfluxdbUtils.getMeasurement(DeviceData.class);

        queryModel.setMeasurement(measurement);

        buildQueryModel(queryModel,deviceVo);

        List<DeviceData> deviceData = influxdbTemplate.selectList(Query.build(queryModel), DeviceData.class);

        return AjaxResult.success(deviceData);
    }

    @ApiOperation("设备历史数据")
    @PostMapping("/alarm")
    public AjaxResult pageAlarm(@RequestBody DeviceVo deviceVo){

        QueryModel queryModel = new QueryModel();

        String measurement = InfluxdbUtils.getMeasurement(DeviceAlarm.class);

        queryModel.setMeasurement(measurement);

        buildQueryModel(queryModel,deviceVo);

        List<DeviceAlarm> deviceData = influxdbTemplate.selectList(Query.build(queryModel), DeviceAlarm.class);

        return AjaxResult.success(deviceData);
    }

    public void buildQueryModel(QueryModel queryModel,DeviceVo deviceVo){

        queryModel.setUseTimeZone(true);
        Map<String, Object> whereMap = Maps.newHashMap();

        if (StringUtils.isNotEmpty(deviceVo.getDeviceId())) {
            whereMap.put("device_id", deviceVo.getDeviceId());
        }
        if (!Objects.isNull(deviceVo.getProductId())) {
            whereMap.put("product_id", deviceVo.getProductId().toString());
        }
        queryModel.setMap(whereMap);
        if (!Objects.isNull(deviceVo.getStartTime())){
            queryModel.setStart(LocalDateTime.ofInstant(deviceVo.getStartTime().toInstant(), ZoneId.systemDefault()));
        }
        if (!Objects.isNull(deviceVo.getEndTime())){
            queryModel.setEnd(LocalDateTime.ofInstant(deviceVo.getEndTime().toInstant(), ZoneId.systemDefault()));
        }

        queryModel.setCurrent((long)deviceVo.getPageNum());
        queryModel.setSize((long)deviceVo.getPageSize());

        queryModel.setOrder(Order.DESC);

        queryModel.setWhere(Op.where(queryModel));

    }
}
