package com.things.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.things.common.core.domain.AjaxResult;
import com.things.product.domain.ProdEvent;
import com.things.product.domain.Product;

import javax.annotation.PostConstruct;

/**
 * @author DaiWei
 * @date 2023/04/03 14:36
 **/
public interface IProdEventService extends IService<ProdEvent> {

    /**
     * 新增
     *
     * @param prodEvent 产品事件信息
     * @return AjaxResult
     */
    AjaxResult insert(ProdEvent prodEvent);

    /**
     * 更新
     *
     * @param prodEvent 产品事件信息
     * @return AjaxResult
     */
    AjaxResult update(ProdEvent prodEvent);

    /**
     * 删除
     *
     * @param id 主键
     * @return AjaxResult
     */
    AjaxResult delete(Integer id);
}
