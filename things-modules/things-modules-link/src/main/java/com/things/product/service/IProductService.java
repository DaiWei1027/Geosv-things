package com.things.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.things.product.domain.Product;
import com.things.product.domain.vo.ProductCount;

/**
 * @author DaiWei
 * @date 2023/04/03 14:36
 **/
public interface IProductService extends IService<Product> {

    /**
     * 统计启用禁用数量
     *
     * @return ProductCount
     */
    ProductCount countNumber();

}
