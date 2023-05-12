package com.things.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.things.product.domain.Product;
import com.things.product.domain.ProductParam;
import com.things.product.mapper.ProductMapper;
import com.things.product.mapper.ProductParamMapper;
import com.things.product.service.IProductParamService;
import com.things.product.service.IProductService;
import org.springframework.stereotype.Service;

/**
 * @author DaiWei
 * @date 2023/04/03 14:38
 **/
@Service
public class ProductParamServiceImpl extends ServiceImpl<ProductParamMapper, ProductParam> implements IProductParamService {
}
