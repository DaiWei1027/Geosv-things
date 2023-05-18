package com.things.product.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.things.common.core.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 产品事件
 *
 * @author DaiWei
 * @date 2023/05/18 14:46
 **/
@Data
public class ProdEvent extends BaseDomain {

    @TableId(type = IdType.AUTO)
    Integer id;

    @ApiModelProperty(value = "产品id" ,required = true)
    Integer productId;

    @ApiModelProperty(value = "事件名称" ,required = true)
    String eventName;

    @ApiModelProperty(value = "事件标识" ,required = true)
    String eventIdentify;


}
