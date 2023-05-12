package com.things.rule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.things.common.core.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 规则执行动作
 *
 * @author DaiWei
 * @date 2023/05/10 11:56
 **/
@Data
public class Action {

    @TableId(type = IdType.AUTO)
    int id;

    int ruleId;

    @ApiModelProperty(value = "类型",notes = "告警、推送、转发、短信")
    String type;


}
