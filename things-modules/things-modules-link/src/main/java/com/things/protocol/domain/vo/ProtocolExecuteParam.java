package com.things.protocol.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/03/31 13:42
 **/
@Data
public class ProtocolExecuteParam {

    @ApiModelProperty("脚本")
    String protocolContent;

    @ApiModelProperty("参数")
    String params;

    String type;

}
