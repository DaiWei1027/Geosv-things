package com.things.influxdb.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.influxdb.annotation.TimeColumn;

import java.time.LocalDateTime;

@Data
@Measurement(name = "device_log")
public class DeviceData {

    /**
     * tag 可以理解为influxdb的索引
     */
    @Column(name = "device_id", tag = true)
    private String deviceId;

    @Column(name = "product_id", tag = true)
    private String productId;

    @Column(name = "data")
    private String data;

    @Column(name = "time")
    @TimeColumn
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime time;
}