package com.things.product.domain.vo;

import lombok.Data;

/**
 * 产品数量统计
 *
 * @author DaiWei
 * @date 2023/05/25 17:00
 **/
@Data
public class ProductCount {

    Long totalNum;

    Long enableNum;

    Long disableNum;
}
