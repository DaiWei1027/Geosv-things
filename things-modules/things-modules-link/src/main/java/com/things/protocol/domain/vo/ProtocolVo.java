package com.things.protocol.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/09/04 14:27
 **/
@Data
public class ProtocolVo {

    @TableId(type = IdType.AUTO)
    Integer id;

    @ApiModelProperty(value = "协议名称")
    String protocolName;

}
