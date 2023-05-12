package com.things.product.domain.vo;

import com.things.framework.param.Pagination;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/03/31 13:31
 **/
@Data
public class ProductVo extends Pagination {

    @ApiModelProperty(name = "产品名称")
    String productName;

}
