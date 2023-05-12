package com.things.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.things.common.core.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 子设备
 *
 * @author DaiWei
 * @date 2023/05/12 13:43
 **/
@Data
public class SubDevice extends BaseDomain {

    @TableId(type = IdType.AUTO)
    int id;

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

    /**
     * 网关设备id
     */
    @ApiModelProperty("网关设备id")
    int deviceId;

    @ApiModelProperty("网关设备id")
    String location;

    @ApiModelProperty("启用状态")
    String status;

    @ApiModelProperty("连接状态")
    String connectionStatus;

}
