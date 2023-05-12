package com.things.rule.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.things.rule.domain.Action;
import com.things.rule.domain.AlarmAction;
import com.things.rule.domain.MessageAction;
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

    AlarmAction alarmAction;

    MessageAction messageAction;
}
