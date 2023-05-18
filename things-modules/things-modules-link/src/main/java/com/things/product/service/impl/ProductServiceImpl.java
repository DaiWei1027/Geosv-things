package com.things.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.things.common.constant.RedisConstants;
import com.things.common.core.redis.RedisCache;
import com.things.product.domain.Product;
import com.things.product.domain.EventParam;
import com.things.product.domain.vo.ProductParams;
import com.things.product.mapper.ProductMapper;
import com.things.product.mapper.EventParamMapper;
import com.things.product.service.IProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author DaiWei
 * @date 2023/04/03 14:38
 **/
@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    private final ProductMapper productMapper;

    private final RedisCache redisCache;

    @PostConstruct
    public void init() {

        List<Product> products = productMapper.selectList(new LambdaQueryWrapper<>());

        products.forEach(product -> {

            redisCache.setCacheObject(RedisConstants.PRODUCT + product.getId(), product);
        });

        log.info("产品管理：更新产品信息成功：[{}]条", products.size());
    }

}
