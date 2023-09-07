package com.things.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.things.common.constant.RedisConstants;
import com.things.common.core.domain.AjaxResult;
import com.things.common.core.redis.RedisCache;
import com.things.product.domain.EventParam;
import com.things.product.domain.ProdEvent;
import com.things.product.domain.vo.ProdEventParams;
import com.things.product.mapper.EventParamMapper;
import com.things.product.mapper.ProdEventMapper;
import com.things.product.service.IProdEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
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
public class ProdEventServiceImpl extends ServiceImpl<ProdEventMapper, ProdEvent> implements IProdEventService {

    private final ProdEventMapper prodEventMapper;

    private final EventParamMapper eventParamMapper;

    private final RedisCache redisCache;

    @PostConstruct
    private void init() {

        List<ProdEvent> prodEvents = prodEventMapper.selectList(new LambdaQueryWrapper<>());
        prodEvents.stream().map(ProdEvent::getProductId).distinct().forEach(productId->redisCache.deleteObject(RedisConstants.PROD_EVENT + productId.toString()));
        prodEvents.forEach(this::cache);

    }

    @Override
    public AjaxResult insert(ProdEvent prodEvent) {

        if (countByEventName(prodEvent.getEventName()) > 0){
            return AjaxResult.error("添加失败，事件:"+prodEvent.getEventName()+"已存在！");
        }
        prodEventMapper.insert(prodEvent);
        cache(prodEvent);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult update(ProdEvent prodEvent) {
        ProdEvent query = prodEventMapper.selectById(prodEvent);
        if (!query.getEventName().equals(prodEvent.getEventName()) && countByEventName(prodEvent.getEventName()) > 0){
            return AjaxResult.error("添加失败，修改后的事件名称："+prodEvent.getEventName()+"已存在");
        }
        prodEventMapper.updateById(prodEvent);
        cache(prodEvent);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult delete(Integer id) {
        ProdEvent prodEvent = prodEventMapper.selectById(id);
        redisCache.deleteObject(RedisConstants.PROD_EVENT + prodEvent.getProductId());
        prodEventMapper.deleteById(id);
        return AjaxResult.success();
    }

    @Override
    public List<ProdEventParams> eventTree(Integer productId) {

        List<ProdEvent> prodEvents = prodEventMapper.selectList(new LambdaQueryWrapper<ProdEvent>().eq(ProdEvent::getProductId, productId));

        List<ProdEventParams> prodEventParamsList = Lists.newArrayList();
        for (ProdEvent prodEvent : prodEvents) {

            ProdEventParams prodEventParams = new ProdEventParams();

            List<EventParam> eventParamList = eventParamMapper.selectList(new LambdaQueryWrapper<EventParam>().eq(EventParam::getEventId, prodEvent.getId()));

            prodEventParams.setProdEvent(prodEvent);

            prodEventParams.setEventParamList(eventParamList);

            prodEventParamsList.add(prodEventParams);
        }

        return prodEventParamsList;
    }

    public int countByEventName(String eventName){
        return prodEventMapper.selectCount(new LambdaQueryWrapper<ProdEvent>().eq(ProdEvent::getEventName, eventName));
    }

    public void cache(ProdEvent prodEvent){

        ProdEventParams prodEventParams = new ProdEventParams();

        List<EventParam> eventParamList = eventParamMapper.selectList(new LambdaQueryWrapper<EventParam>().eq(EventParam::getEventId, prodEvent.getId()));

        prodEventParams.setProdEvent(prodEvent);
        prodEventParams.setEventParamList(eventParamList);

        redisCache.rightPush(RedisConstants.PROD_EVENT + prodEvent.getProductId(), prodEventParams);

    }
}
