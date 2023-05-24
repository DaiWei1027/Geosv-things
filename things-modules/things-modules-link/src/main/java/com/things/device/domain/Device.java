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
    Integer id;

    @ApiModelProperty(value = "设备唯一标识",required = true)
    String deviceId;

    @ApiModelProperty(value = "设备名称",required = true)
    String deviceName;

    @ApiModelProperty("所在位置")
    String location;

    @ApiModelProperty(value = "设备类型",notes = "直连设备 device | 网关设备 gateway",required = true)
    String deviceType;

    @ApiModelProperty(value = "协议类型",notes = "如MQTT、MODBUS、JSON",required = true)
    String ruleType;

    @ApiModelProperty(value = "产品id",required = true)
    Integer productId;

    @ApiModelProperty(value = "状态",notes = "启用：0 禁用：1",required = true)
    String status;

    @ApiModelProperty(value = "连接类型",notes = "MQTT,TCP",required = true)
    String connectionType;

    @ApiModelProperty("连接状态")
    String connectionStatus;

}
