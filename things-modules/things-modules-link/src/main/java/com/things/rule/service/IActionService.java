package com.things.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.things.common.core.domain.AjaxResult;
import com.things.rule.domain.Action;
import com.things.rule.domain.Rule;
import com.things.rule.domain.vo.ActionVo;
import com.things.rule.domain.vo.RuleVo;

import java.util.List;

/**
 * @author DaiWei
 * @date 2023/05/09 10:56
 **/
public interface IActionService extends IService<Action> {

    /**
     * 批量添加执行动作
     *
     * @param id        规则ID
     * @param actionVos 执行动作参数
     */
    void insertAction(int id, List<ActionVo> actionVos);

    /**
     * 根据规则id删除
     *
     * @param ruleId 规则id
     */
    void removeByRuleId(int ruleId);

    /**
     * 规则id查询执行动作
     * @param ruleId 规则id
     * @return List<ActionVo>
     */
    List<ActionVo> listByRuleId(int ruleId);
}
