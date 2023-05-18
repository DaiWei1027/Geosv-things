package com.things.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.things.common.constant.RedisConstants;
import com.things.common.core.redis.RedisCache;
import com.things.product.domain.EventParam;
import com.things.product.domain.ProdEvent;
import com.things.product.domain.vo.ProdEventParams;
import com.things.product.mapper.ProdEventMapper;
import com.things.product.mapper.ProductMapper;
import com.things.product.mapper.EventParamMapper;
import com.things.product.service.IProdEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
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
        prodEvents.stream().map(ProdEvent::getProductId).distinct().forEach(productId->redisCache.deleteObject(productId.toString()));
        prodEvents.forEach(prodEvent -> {

            List<EventParam> eventParamList = eventParamMapper.selectList(new LambdaQueryWrapper<EventParam>().eq(EventParam::getEventId, prodEvent.getId()));

            ProdEventParams prodEventParams = new ProdEventParams();

            prodEventParams.setProdEvent(prodEvent);
            prodEventParams.setEventParamList(eventParamList);

            redisCache.rightPush(RedisConstants.PROD_EVENT + prodEvent.getProductId(), prodEventParams);

        });

    }
}
