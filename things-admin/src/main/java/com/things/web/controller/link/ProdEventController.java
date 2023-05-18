package com.things.web.controller.link;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.things.common.constant.RedisConstants;
import com.things.common.core.controller.BaseController;
import com.things.common.core.domain.AjaxResult;
import com.things.product.domain.ProdEvent;
import com.things.product.domain.Product;
import com.things.product.domain.vo.ProductParams;
import com.things.product.service.IProdEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author DaiWei
 * @date 2023/05/18 14:57
 **/
@Api(tags = "产品事件管理")
@RestController
@RequestMapping("/link/product")
@AllArgsConstructor
public class ProdEventController extends BaseController {

    private final IProdEventService prodEventService;

    @ApiOperation("新增")
    @PostMapping("/insert")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult insert(@RequestBody ProdEvent product) {
        product.setCreateBy(getUsername());
        product.setCreateTime(new Date());
        prodEventService.save(product);
        return AjaxResult.success();
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult update(@RequestBody ProdEvent product) {
        product.setUpdateBy(getUsername());
        product.setUpdateTime(new Date());
        prodEventService.updateById(product);
        return AjaxResult.success();
    }

    @ApiOperation("查询产品的所有事件")
    @PostMapping("/selectByProductId/{productId}")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult selectByProductId(@PathVariable Integer productId) {
        return AjaxResult.success(prodEventService.list(new LambdaQueryWrapper<ProdEvent>().eq(ProdEvent::getProductId,productId)));
    }
}
