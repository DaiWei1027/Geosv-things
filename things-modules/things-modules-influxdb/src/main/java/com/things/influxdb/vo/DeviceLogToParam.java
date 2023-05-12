package com.things.influxdb.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.influxdb.annotation.TimeColumn;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Measurement(name = "device_log")
public class DeviceLogToParam {

    /**
     * tag 可以理解为influxdb的索引
     */
    @Column(name = "device_id", tag = true)
    private String deviceId;
    @Column(name = "pressure")
    private String pressure;

    @Column(name = "temperature")
    private String temperature;

    @Column(name = "time")
    @TimeColumn
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;

    private Long pageNum;
    private Long pageSize;
}