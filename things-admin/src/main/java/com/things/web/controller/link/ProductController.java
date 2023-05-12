package com.things.web.controller.link;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.things.common.constant.RedisConstants;
import com.things.common.core.controller.BaseController;
import com.things.common.core.domain.AjaxResult;
import com.things.common.core.redis.RedisCache;
import com.things.common.utils.StringUtils;
import com.things.product.domain.Product;
import com.things.product.service.IProductService;
import com.things.product.domain.vo.ProductVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author DaiWei
 * @date 2023/04/03 14:41
 **/
@Api(tags = "产品管理")
@RestController
@RequestMapping("/link/product")
public class ProductController extends BaseController {

    @Autowired
    private IProductService productService;
    @Autowired
    private RedisCache redisCache;

    @ApiOperation("新增")
    @PostMapping("/insert")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult insert(@RequestBody Product product) {
        product.setCreateBy(getUsername());
        product.setCreateTime(new Date());
        productService.save(product);
        redisCache.setCacheObject(RedisConstants.PRODUCT + product.getId(), product);
        return AjaxResult.success();
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult update(@RequestBody Product product) {
        product.setUpdateBy(getUsername());
        product.setUpdateTime(new Date());
        redisCache.setCacheObject(RedisConstants.PRODUCT + product.getId(), product);
        return toAjax(productService.updateById(product));
    }

    @ApiOperation("删除")
    @PostMapping("/delete/{id}")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult delete(@PathVariable Integer id) {
        redisCache.deleteObject(RedisConstants.PRODUCT + id);
        return toAjax(productService.removeById(id));
    }

    @ApiOperation("分页查询")
    @PostMapping("/page")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult page(@RequestBody ProductVo param) {

        Page<Product> page = new Page<>(param.getPageNum(), param.getPageSize());

        Page<Product> pageData = productService.page(page, new LambdaQueryWrapper<Product>()
                .like(StringUtils.isNotEmpty(param.getProductName()), Product::getProductName, param.getProductName())
        );

        return AjaxResult.success(pageData);
    }


}
