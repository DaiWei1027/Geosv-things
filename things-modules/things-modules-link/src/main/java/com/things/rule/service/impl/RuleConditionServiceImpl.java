package com.things.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.things.common.constant.RedisConstants;
import com.things.common.core.redis.RedisCache;
import com.things.rule.domain.Rule;
import com.things.rule.domain.RuleCondition;
import com.things.rule.mapper.RuleConditionMapper;
import com.things.rule.mapper.RuleMapper;
import com.things.rule.service.IRuleConditionService;
import com.things.rule.service.IRuleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DaiWei
 * @date 2023/05/09 10:56
 **/
@Service
@AllArgsConstructor
public class RuleConditionServiceImpl extends ServiceImpl<RuleConditionMapper, RuleCondition> implements IRuleConditionService {

    private final RuleConditionMapper ruleConditionMapper;


    @Override
    public void insertRuleConditions(int ruleId, List<RuleCondition> conditionList) {

        conditionList.forEach(ruleCondition -> {

            ruleCondition.setRuleId(ruleId);
            ruleConditionMapper.insert(ruleCondition);

        });

    }

    @Override
    public List<RuleCondition> getByRuleId(int ruleId) {
        return ruleConditionMapper.selectList(new LambdaQueryWrapper<RuleCondition>().eq(RuleCondition::getRuleId,ruleId));
    }
}
