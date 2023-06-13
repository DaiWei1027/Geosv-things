package com.things.framework.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DaiWei
 */
@Data
public class Pagination {

    /**
     * 页码
     */
    @ApiModelProperty("页码")
    int pageNum;

    /**
     * 页数
     */
    @ApiModelProperty("页大小")
    int pageSize;
}
