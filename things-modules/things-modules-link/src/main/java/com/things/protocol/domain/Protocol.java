package com.things.protocol.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/03/31 11:03
 **/
@Data
public class Protocol {

    @TableId(type = IdType.AUTO)
    int id;

    @ApiModelProperty(name = "协议名称")
    String protocolName;

    @ApiModelProperty(name = "协议标识")
    String protocolMark;

    @ApiModelProperty(name = "协议版本")
    String protocolVersion;

    @ApiModelProperty(name = "协议类型，Modbus 等")
    String type;

    @ApiModelProperty(name = "数据类型",notes = "BYTE,JSON")
    String dataType;

    @ApiModelProperty(value = "启动状态 0启用 1禁用")
    String status;

    @ApiModelProperty(name = "语言，JAVA")
    String language;

    @ApiModelProperty(name = "协议内容")
    String protocolContent;

    @ApiModelProperty(name = "备注")
    String remark;
}
