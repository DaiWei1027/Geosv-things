package com.things.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.things.common.core.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/03/31 16:00
 **/
@Data
public class Device extends BaseDomain {

    @TableId(type = IdType.AUTO)
    int id;

    @ApiModelProperty("设备唯一标识")
    String deviceId;

    @ApiModelProperty("设备名称")
    String deviceName;

    @ApiModelProperty("所在位置")
    String location;

    @ApiModelProperty("设备类型 直连设备 device | 网关设备 gateway")
    String deviceType;

    @ApiModelProperty("产品id")
    Integer productId;

    @ApiModelProperty("状态")
    String status;

    @ApiModelProperty("连接类型")
    String connectionType;

    @ApiModelProperty("连接状态")
    String connectionStatus;

}
