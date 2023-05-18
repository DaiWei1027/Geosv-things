package com.things.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.things.product.domain.ProdEvent;
import com.things.product.mapper.ProdEventMapper;
import com.things.product.mapper.ProductMapper;
import com.things.product.mapper.EventParamMapper;
import com.things.product.service.IProdEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author DaiWei
 * @date 2023/04/03 14:38
 **/
@Slf4j
@Service
@AllArgsConstructor
public class ProdEventServiceImpl extends ServiceImpl<ProdEventMapper, ProdEvent> implements IProdEventService {

    private final ProductMapper productMapper;

    private final EventParamMapper eventParamMapper;

}
