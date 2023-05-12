package com.things.rule.domain.vo;

import com.things.framework.param.Pagination;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/05/09 11:54
 **/
@Data
public class RuleParam extends Pagination {

    String ruleName;

    String ruleNo;

    String status;

    Integer productId;

}
