package com.things.rule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.things.common.core.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 规则
 *
 * @author DaiWei
 * @date 2023/05/09 10:12
 **/
@Data
public class Rule extends BaseDomain {

    @TableId(type = IdType.AUTO)
    int id;

    @NotBlank(message="规则名称不能为空")
    @ApiModelProperty(name = "规则名称",required = true)
    String ruleName;

    @NotBlank(message="规则编号不能为空")
    @ApiModelProperty(name = "规则编号",required = true)
    String ruleNo;

    @ApiModelProperty(name = "状态 启用0 停用 1",required = true)
    String status;

    @ApiModelProperty(name = "描述")
    String description;

    @ApiModelProperty(name = "触发条件",notes = "全部：ALL 任意一个：ANY")
    String triggering;

    @NotBlank(message="产品ID不能为空")
    @ApiModelProperty(name = "产品ID",required = true)
    int productId;
}
