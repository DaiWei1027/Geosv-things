package com.things.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.things.common.core.domain.AjaxResult;
import com.things.rule.domain.Rule;
import com.things.rule.domain.vo.RuleVo;

/**
 * @author DaiWei
 * @date 2023/05/09 10:56
 **/
public interface IRuleService extends IService<Rule> {

    /**
     * 新增规则
     *
     * @param ruleVo 规则参数
     * @return AjaxResult
     */
    AjaxResult add(RuleVo ruleVo);

    /**
     * 编辑
     *
     * @param ruleVo 规则参数
     * @return AjaxResult
     */
    AjaxResult updateRule(RuleVo ruleVo);

    /**
     * 启用|停用
     *
     * @param id     主键
     * @param status 状态
     * @return
     */
    AjaxResult status(Integer id, String status);

    /**
     * id查询
     *
     * @param id 主键
     * @return
     */
    RuleVo selectById(Integer id);
}
