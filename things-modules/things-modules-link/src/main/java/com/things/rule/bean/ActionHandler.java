package com.things.rule.bean;

import com.things.common.enums.ActionConstants;
import com.things.influxdb.vo.DeviceData;
import com.things.rule.bean.action.AlarmActionExec;
import com.things.rule.bean.action.MessageActionExec;
import com.things.rule.domain.vo.ActionVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author DaiWei
 * @date 2023/05/10 14:53
 **/
@Slf4j
@Component
@AllArgsConstructor
public class ActionHandler {

    private final AlarmActionExec actionExec;

    private final MessageActionExec messageActionExec;

    /**
     * 执行动作
     *
     * @param actionVoList 执行动作
     */
    @Async("actionExecutor")
    public void action(List<ActionVo> actionVoList, DeviceData deviceData) {

        actionVoList.forEach( actionVo -> {

            String type = actionVo.getType();

            switch (type){

                case ActionConstants.ALARM:
                    actionExec.exe(actionVo,deviceData);
                break;
                case ActionConstants.MESSAGE:
                    messageActionExec.exe(actionVo,deviceData);
                    break;
                default:
                    break;
            }

        });

    }
}
