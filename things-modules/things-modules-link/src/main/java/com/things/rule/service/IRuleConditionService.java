package com.things.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.things.rule.domain.Rule;
import com.things.rule.domain.RuleCondition;

import java.util.List;

/**
 * @author DaiWei
 * @date 2023/05/09 10:56
 **/
public interface IRuleConditionService extends IService<RuleCondition> {

    /**
     * 批量插入规则条件
     *
     * @param ruleId 规则ID
     * @param conditionList 触发条件
     */
    void insertRuleConditions(int ruleId, List<RuleCondition> conditionList);
}
