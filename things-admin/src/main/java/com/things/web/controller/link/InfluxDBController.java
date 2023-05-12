package com.things.web.controller.link;


import com.google.common.collect.Maps;
import com.things.common.core.domain.AjaxResult;
import com.things.common.utils.StringUtils;
import com.things.influxdb.vo.DeviceData;
import com.things.influxdb.vo.DeviceLogToParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.influxdb.impl.InfluxDBImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

/**
 * @author DaiWei
 * @date 2023/04/18 16:38
 **/
@Api(tags = "influx")
@RestController
@RequestMapping("/openApi/influxDB")
@AllArgsConstructor
public class InfluxDBController {

    private final InfluxdbTemplate influxdbTemplate;

    private final InfluxDBImpl influxDBImpl;

    @ApiOperation("新增")
    @GetMapping("/add")
    public AjaxResult queryFire(String deviceId) {

//        DeviceLogTo deviceLogTo = new DeviceLogTo();
//        deviceLogTo.setDeviceId("1234");
//        deviceLogTo.setTime(LocalDateTime.now());
//        deviceLogTo.setPressure("181");
//        deviceLogTo.setTemperature("226");
//        influxdbTemplate.insert(deviceLogTo);

        String measurement = InfluxdbUtils.getMeasurement(DeviceData.class);

        Map<String, String> tags = Maps.newHashMap();
        Map<String, Object> fields = Maps.newHashMap();

        tags.put("device_id", deviceId);
        fields.put("pressure", "220");
        fields.put("temperature", "16");

        long time = System.currentTimeMillis() / 1000;

        influxdbTemplate.insert(tags, fields, time, measurement + "_" + deviceId);

        return AjaxResult.success();
    }

    @ApiOperation("查询")
    @GetMapping("/list")
    public AjaxResult list(@RequestBody DeviceLogToParam param) {

        QueryModel queryModel = new QueryModel();

        String measurement = InfluxdbUtils.getMeasurement(DeviceData.class);

        queryModel.setMeasurement(measurement);

        queryModel.setUseTimeZone(true);
        Map<String, Object> whereMap = Maps.newHashMap();

        if (StringUtils.isNotEmpty(param.getDeviceId())) {
            whereMap.put("device_id", param.getDeviceId());
        }
        queryModel.setMap(whereMap);
        queryModel.setStart(LocalDateTime.ofInstant(param.getStartTime().toInstant(), ZoneId.systemDefault()));
        queryModel.setEnd(LocalDateTime.ofInstant(param.getEndTime().toInstant(), ZoneId.systemDefault()));
        queryModel.setCurrent(param.getPageNum());
        queryModel.setSize(param.getPageSize());

        queryModel.setOrder(Order.DESC);

        queryModel.setWhere(Op.where(queryModel));

        List<DeviceData> deviceData = influxdbTemplate.selectList(Query.build(queryModel), DeviceData.class);

        return AjaxResult.success(deviceData);
    }

}
