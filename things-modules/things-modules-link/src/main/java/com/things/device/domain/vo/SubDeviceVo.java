package com.things.device.domain.vo;

import com.things.framework.param.Pagination;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/03/31 16:00
 **/
@Data
public class SubDeviceVo extends Pagination {


    /**
     * 边设备唯一标识
     */
    @ApiModelProperty("子设备唯一标识")
    String subDeviceId;

    @ApiModelProperty("设备唯一标识")
    String subDeviceNo;

    /**
     * 设备名称
     */
    @ApiModelProperty("子设备名称")
    String deviceName;

    @ApiModelProperty("产品id")
    Integer productId;

    @ApiModelProperty("状态")
    String status;

    @ApiModelProperty("连接状态")
    String connectionStatus;

}
