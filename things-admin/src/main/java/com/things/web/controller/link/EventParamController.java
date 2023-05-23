package com.things.web.controller.link;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.things.common.constant.RedisConstants;
import com.things.common.core.controller.BaseController;
import com.things.common.core.domain.AjaxResult;
import com.things.common.core.redis.RedisCache;
import com.things.product.domain.EventParam;
import com.things.product.domain.ProdEvent;
import com.things.product.domain.vo.ProdEventParams;
import com.things.product.domain.vo.ProductParams;
import com.things.product.service.IEventParamService;
import com.things.product.service.IProdEventService;
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
public class EventParamController extends BaseController {

    private final IProdEventService prodEventService;

    private final IEventParamService productParamService;

    private final RedisCache redisCache;

    @ApiOperation("新增")
    @PostMapping("/insert")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult insert(@RequestBody EventParam eventParam) {
        eventParam.setCreateBy(getUsername());
        eventParam.setCreateTime(new Date());
        productParamService.save(eventParam);
        //更新事件缓存
        updateEvent(eventParam.getEventId());
        return AjaxResult.success();
    }

    @ApiOperation("删除")
    @PostMapping("/delete/{id}")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult delete(@PathVariable Integer id) {
        EventParam eventParam = productParamService.getById(id);
        updateEvent(eventParam.getEventId());
        return toAjax(productParamService.removeById(id));
    }


    @ApiOperation("查询事件所有参数")
    @PostMapping("/listByProductId/{eventId}")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult listByEventId(@PathVariable Integer eventId) {

        List<EventParam> eventParams = productParamService.list(new LambdaQueryWrapper<EventParam>().eq(EventParam::getEventId, eventId));

        return AjaxResult.success(eventParams);
    }

    private void updateEvent(int eventId){

        List<EventParam> eventParamList = productParamService.list(new LambdaQueryWrapper<EventParam>()
                .eq(EventParam::getEventId, eventId));

        ProdEvent prodEvent = prodEventService.getById(eventId);

        ProdEventParams prodEventParams = new ProdEventParams();

        prodEventParams.setProdEvent(prodEvent);
        prodEventParams.setEventParamList(eventParamList);

        redisCache.rightPush(RedisConstants.PROD_EVENT + prodEvent.getProductId(), prodEventParams);

    }
}
