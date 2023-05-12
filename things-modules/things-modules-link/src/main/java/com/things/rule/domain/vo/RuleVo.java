package com.things.rule.domain.vo;

import com.things.rule.domain.Action;
import com.things.rule.domain.Rule;
import com.things.rule.domain.RuleCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author DaiWei
 * @date 2023/05/09 11:11
 **/
@Data
public class RuleVo extends Rule {

    /**
     * 触发条件
     */
    @ApiModelProperty(name = "触发条件数组")
    List<RuleCondition> ruleConditions;

    /**
     * 执行动作
     */
    List<ActionVo> actionVos;
}
