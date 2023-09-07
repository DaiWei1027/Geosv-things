package com.things.rule.domain.vo;

import com.things.rule.domain.Action;
import com.things.rule.domain.action.AlarmAction;
import com.things.rule.domain.action.MessageAction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 规则执行动作
 *
 * @author DaiWei
 * @date 2023/05/10 11:56
 **/
@Data
public class ActionVo extends Action {

    @ApiModelProperty("告警")
    AlarmAction alarmAction;

    @ApiModelProperty("消息")
    MessageAction messageAction;
}
