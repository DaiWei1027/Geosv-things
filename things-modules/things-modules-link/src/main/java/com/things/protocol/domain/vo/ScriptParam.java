package com.things.protocol.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/08/31 16:52
 **/
@Data
public class ScriptParam {

    @ApiModelProperty(value = "主键",required = true)
    int id;

    @ApiModelProperty(value = "脚本内容",required = true)
    String protocolContent;
}
