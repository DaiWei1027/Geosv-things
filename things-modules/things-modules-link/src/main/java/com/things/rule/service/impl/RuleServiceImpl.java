package com.things.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.things.common.constant.RedisConstants;
import com.things.common.core.domain.AjaxResult;
import com.things.common.core.redis.RedisCache;
import com.things.common.utils.bean.BeanUtils;
import com.things.rule.domain.Rule;
import com.things.rule.domain.RuleCondition;
import com.things.rule.domain.vo.ActionVo;
import com.things.rule.domain.vo.RuleVo;
import com.things.rule.mapper.RuleMapper;
import com.things.rule.service.IActionService;
import com.things.rule.service.IRuleConditionService;
import com.things.rule.service.IRuleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DaiWei
 * @date 2023/05/09 10:56
 **/
@Slf4j
@Service
@AllArgsConstructor
public class RuleServiceImpl extends ServiceImpl<RuleMapper, Rule> implements IRuleService {

    private final RuleMapper ruleMapper;

    private final IRuleConditionService ruleConditionService;

    private final IActionService actionService;

    private final RedisCache redisCache;

    @PostConstruct
    public void init(){

        List<Rule> rules = ruleMapper.selectList(new LambdaQueryWrapper<>());

        List<Integer> collect = rules.stream().map(Rule::getProductId).distinct().collect(Collectors.toList());

        collect.forEach(productId -> redisCache.deleteObject(RedisConstants.RULE + productId));

        rules.forEach(rule -> {

            List<RuleCondition> ruleConditionList = ruleConditionService.list(new LambdaQueryWrapper<RuleCondition>().eq(RuleCondition::getRuleId, rule.getId()));
            RuleVo ruleVo = new RuleVo();
            BeanUtils.copyProperties(rule, ruleVo);
            ruleVo.setRuleConditions(ruleConditionList);

            //查询执行动作
            List<ActionVo> actionVos = actionService.listByRuleId(rule.getId());
            ruleVo.setActionVos(actionVos);

            redisCache.rightPush(RedisConstants.RULE + ruleVo.getProductId(), ruleVo);

        });

        log.info("规则业务层：更新规则成功：[{}]条",rules.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(RuleVo ruleVo) {

        Integer count = ruleMapper.selectCount(new LambdaQueryWrapper<Rule>().eq(Rule::getRuleNo, ruleVo.getRuleNo()));

        if (count > 0) {

            return AjaxResult.error("规则编号:[" + ruleVo.getRuleNo() + "]已存在，请重试");

        }

        Rule rule = new Rule();
        BeanUtils.copyProperties(ruleVo, rule);
        rule.setCreateTime(new Date());
        ruleMapper.insert(rule);

        List<RuleCondition> ruleConditions = ruleVo.getRuleConditions();

        ruleConditionService.insertRuleConditions(rule.getId(), ruleConditions);

        List<ActionVo> actionVos = ruleVo.getActionVos();

        actionService.insertAction(rule.getId(),actionVos);

        ruleVo.setId(rule.getId());
        redisCache.rightPush(RedisConstants.RULE + ruleVo.getProductId(), ruleVo);

        return AjaxResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateRule(RuleVo ruleVo) {

        Rule rule = new Rule();
        BeanUtils.copyProperties(ruleVo, rule);
        rule.setUpdateTime(new Date());
        ruleMapper.updateById(rule);

        ruleConditionService.remove(new LambdaQueryWrapper<RuleCondition>().eq(RuleCondition::getRuleId, rule.getId()));

        List<RuleCondition> ruleConditions = ruleVo.getRuleConditions();

        ruleConditionService.insertRuleConditions(rule.getId(), ruleConditions);

        actionService.removeByRuleId(rule.getId());

        actionService.insertAction(rule.getId(),ruleVo.getActionVos());

        //查询规则缓存
        List<RuleVo> cacheList = redisCache.getCacheList(RedisConstants.RULE + ruleVo.getProductId());
        //筛选之前的缓存
        List<RuleVo> collect = cacheList.stream().filter(item -> item.getId() == rule.getId()).collect(Collectors.toList());

        cacheList.removeAll(collect);

        cacheList.add(ruleVo);

        redisCache.setCacheList(RedisConstants.RULE + ruleVo.getProductId(),cacheList);

        return AjaxResult.success();
    }
}
