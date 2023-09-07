package com.things.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.things.common.enums.ActionConstants;
import com.things.common.utils.bean.BeanUtils;
import com.things.rule.domain.Action;
import com.things.rule.domain.action.AlarmAction;
import com.things.rule.domain.action.MessageAction;
import com.things.rule.domain.vo.ActionVo;
import com.things.rule.mapper.ActionMapper;
import com.things.rule.mapper.AlarmActionMapper;
import com.things.rule.mapper.MessageActionMapper;
import com.things.rule.service.IActionService;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author DaiWei
 * @date 2023/05/09 10:56
 **/
@Service
@AllArgsConstructor
public class ActionServiceImpl extends ServiceImpl<ActionMapper, Action> implements IActionService {

    private final ActionMapper actionMapper;

    private final AlarmActionMapper alarmActionMapper;

    private final MessageActionMapper messageActionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertAction(int id, List<ActionVo> actionVos) {

        actionVos.forEach(actionVo -> {

            Action action = new Action();
            BeanUtils.copyProperties(actionVo, action);
            action.setRuleId(id);
            actionMapper.insert(action);


            switch (action.getType()) {
                case ActionConstants.ALARM:
                    alarmAction(action.getId(), actionVo.getAlarmAction());
                    break;
                case ActionConstants.PUSH:
                    push(action.getId(), actionVo.getMessageAction());
                    break;
                default:
                    break;
            }

        });

    }

    @Override
    public void removeByRuleId(int ruleId) {

        List<Action> actions = actionMapper.selectList(new LambdaQueryWrapper<Action>().eq(Action::getRuleId, ruleId));

        actions.forEach(action -> {

            switch (action.getType()) {
                case ActionConstants.ALARM:
                    alarmActionMapper.delete(new LambdaQueryWrapper<AlarmAction>().eq(AlarmAction::getActionId,action.getId()));
                    break;
                case ActionConstants.PUSH:
                    messageActionMapper.delete(new LambdaQueryWrapper<MessageAction>().eq(MessageAction::getActionId,action.getId()));
                    break;
                default:
                    break;
            }

            actionMapper.deleteById(action.getId());

        });

    }

    @Override
    public List<ActionVo> listByRuleId(int ruleId) {
        List<Action> actions = actionMapper.selectList(new LambdaQueryWrapper<Action>().eq(Action::getRuleId, ruleId));
        List<ActionVo> actionVos = Lists.newArrayList();
        actions.forEach(action -> {
            ActionVo actionVo = new ActionVo();
            BeanUtils.copyProperties(action,actionVo);
            switch (action.getType()) {
                case ActionConstants.ALARM:
                    actionVo.setAlarmAction(alarmActionMapper.selectOne(new LambdaQueryWrapper<AlarmAction>().eq(AlarmAction::getActionId,action.getId())));
                    break;
                case ActionConstants.PUSH:
                    actionVo.setMessageAction(messageActionMapper.selectOne(new LambdaQueryWrapper<MessageAction>().eq(MessageAction::getActionId,action.getId())));
                    break;
                default:
                    break;
            }
            actionVos.add(actionVo);
        });
        return actionVos;
    }

    public void alarmAction(int actionId, AlarmAction alarmAction) {

        alarmAction.setActionId(actionId);
        alarmActionMapper.insert(alarmAction);
    }

    public void push(int actionId, MessageAction messageAction) {

    }
}
