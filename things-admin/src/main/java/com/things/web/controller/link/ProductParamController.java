package com.things.web.controller.link;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.things.common.constant.RedisConstants;
import com.things.common.core.controller.BaseController;
import com.things.common.core.domain.AjaxResult;
import com.things.common.core.redis.RedisCache;
import com.things.product.domain.Product;
import com.things.product.domain.ProductParam;
import com.things.product.domain.vo.ProductParams;
import com.things.product.service.IProductParamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author DaiWei
 * @date 2023/05/09 09:28
 **/
@Api(tags = "产品参数管理")
@RestController
@RequestMapping("/link/product_param")
@AllArgsConstructor
public class ProductParamController extends BaseController {

    private final IProductParamService productParamService;

    private final RedisCache redisCache;

    @ApiOperation("新增")
    @PostMapping("/insert")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult insert(@RequestBody ProductParam productParam) {
        productParam.setCreateBy(getUsername());
        productParam.setCreateTime(new Date());
        productParamService.save(productParam);
        //更新产品缓存
        int productId = productParam.getProductId();

        List<ProductParam> productParamList = productParamService.list(new LambdaQueryWrapper<ProductParam>()
                .eq(ProductParam::getProductId, productId));

        ProductParams productParams = redisCache.getCacheObject(RedisConstants.PRODUCT + productId);

        productParams.setProductParamList(productParamList);

        redisCache.setCacheObject(RedisConstants.PRODUCT + productId, productParams);

        return AjaxResult.success();
    }

    @ApiOperation("删除")
    @PostMapping("/delete/{id}")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult delete(@PathVariable Integer id) {
        return toAjax(productParamService.removeById(id));
    }


    @ApiOperation("查询产品所有参数")
    @PostMapping("/listByProductId/{productId}")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult listByProductId(@PathVariable Integer productId) {

        List<ProductParam> productParams = productParamService.list(new LambdaQueryWrapper<ProductParam>().eq(ProductParam::getProductId, productId));

        return AjaxResult.success(productParams);
    }
}
