package com.things.product.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.things.common.core.domain.BaseDomain;
import com.things.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/04/03 14:28
 **/
@Data
public class Product extends BaseDomain {

    @TableId(type = IdType.AUTO)
    Integer id;

    @ApiModelProperty(name = "产品名称",required = true)
    String productName;

    @ApiModelProperty(name = "厂家")
    String manufacturer;

//    @ApiModelProperty(name = "数据类型",required = true)
//    String dataType;

    @ApiModelProperty(name = "连接类型",required = true)
    String connectType;

    @ApiModelProperty(name = "启动状态 0启用 1禁用",required = true)
    String status;

    @ApiModelProperty(name = "协议id",required = true)
    Integer protocolId;

}
