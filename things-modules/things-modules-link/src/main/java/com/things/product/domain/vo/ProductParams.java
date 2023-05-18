package com.things.product.domain.vo;

import com.things.product.domain.Product;
import com.things.product.domain.EventParam;
import lombok.Data;

import java.util.List;

/**
 * 产品信息和产品参数信息
 *
 * @author DaiWei
 * @date 2023/05/09 14:05
 **/
@Data
public class ProductParams {

    /**
     * 产品信息
     */
    Product product;

    /**
     * 产品参数信息
     */
    List<EventParam> eventParamList;
}
