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
    Integer id;

    /**
     * 边设备唯一标识
     */
    @ApiModelProperty(value = "子设备唯一标识",required = true)
    String deviceId;

    @ApiModelProperty(value = "设备唯一标识",required = true)
    String deviceNo;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "子设备名称",required = true)
    String deviceName;

    /**
     * 网关设备id
     */
    @ApiModelProperty(value = "网关设备id",required = true)
    int gatewayId;

    @ApiModelProperty(value = "产品id",required = true)
    int productId;

    @ApiModelProperty(value = "位置",required = true)
    String location;

    @ApiModelProperty(value = "启用状态",required = true)
    String status;

    @ApiModelProperty("连接状态")
    String connectionStatus;

}
