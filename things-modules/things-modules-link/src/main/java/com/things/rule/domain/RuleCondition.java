package com.things.rule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 规则条件
 *
 * @author DaiWei
 * @date 2023/05/09 10:34
 **/
@Data
public class RuleCondition {

    @TableId(type = IdType.AUTO)
    Integer id;

    @ApiModelProperty
    Integer ruleId;

    /**
     * 设备数据中某个参数的名称
     */
    @ApiModelProperty(name = "参数名称")
    String param;

    @ApiModelProperty(name = "关系运算符")
    String operator;

    @ApiModelProperty(name = "触发参数",notes = "如选择设置范围则上传数组[param1,param2]")
    String value;
}
