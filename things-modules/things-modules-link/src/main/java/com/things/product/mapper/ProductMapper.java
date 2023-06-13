package com.things.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.things.product.domain.Product;
import com.things.product.domain.vo.ProductCount;

/**
 * @author DaiWei
 * @date 2023/04/03 14:36
 **/
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 统计启用禁用数量
     *
     * @return ProductCount
     */
    ProductCount countNumber();

}
