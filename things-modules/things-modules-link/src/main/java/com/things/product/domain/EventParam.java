package com.things.product.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.things.common.core.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 产品参数
 *
 * @author DaiWei
 * @date 2023/05/09 09:18
 **/
@Data
public class EventParam extends BaseDomain {

    @TableId(type = IdType.AUTO)
    Integer id;

    @ApiModelProperty(value = "产品id",required = true)
    Integer eventId;

    @ApiModelProperty(value = "参数名称",required = true)
    String paramName;

    @ApiModelProperty(value = "参数字段",required = true)
    String fieldName;
}
