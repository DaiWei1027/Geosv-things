package com.things.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.things.product.domain.EventParam;
import com.things.product.mapper.EventParamMapper;
import com.things.product.service.IEventParamService;
import org.springframework.stereotype.Service;

/**
 * @author DaiWei
 * @date 2023/04/03 14:38
 **/
@Service
public class EventParamServiceImpl extends ServiceImpl<EventParamMapper, EventParam> implements IEventParamService {
}
