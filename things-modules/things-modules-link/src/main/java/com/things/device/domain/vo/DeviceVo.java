package com.things.device.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.things.common.core.domain.BaseDomain;
import com.things.framework.param.Pagination;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author DaiWei
 * @date 2023/03/31 16:00
 **/
@Data
public class DeviceVo extends Pagination {


    @ApiModelProperty("设备唯一标识")
    String deviceId;

    @ApiModelProperty("设备名称")
    String deviceName;

    @ApiModelProperty("设备类型 直连设备 device | 网关设备 gateway")
    String deviceType;

    @ApiModelProperty("产品id")
    Integer productId;

    @ApiModelProperty("状态")
    String status;

    @ApiModelProperty("连接状态")
    String connectionStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    Date endTime;

}
