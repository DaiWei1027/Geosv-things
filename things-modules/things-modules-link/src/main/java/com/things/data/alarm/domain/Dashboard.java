package com.things.data.alarm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 仪表盘查询
 *
 * @author DaiWei
 * @date 2023/05/26 14:59
 **/
@Data
public class Dashboard {

    @ApiModelProperty(name = "消息类型",notes = "设备消息：device 告警记录：alarm")
    String type;

    @ApiModelProperty(name = "分组",notes = "按天：D,按月:M")
    String group;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    Date endTime;


}
