package com.things.web.controller.link;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.things.common.core.controller.BaseController;
import com.things.common.core.domain.AjaxResult;
import com.things.common.utils.StringUtils;
import com.things.common.utils.bean.BeanUtils;
import com.things.rule.domain.Rule;
import com.things.rule.domain.RuleCondition;
import com.things.rule.domain.vo.RuleParam;
import com.things.rule.domain.vo.RuleVo;
import com.things.rule.service.IRuleConditionService;
import com.things.rule.service.IRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author DaiWei
 * @date 2023/05/09 11:01
 **/
@Api(tags = "规则管理")
@RestController
@RequestMapping("/link/rule")
@AllArgsConstructor
public class RuleController extends BaseController {

    private final IRuleService ruleService;

    private final IRuleConditionService ruleConditionService;


    @ApiOperation("分页查询")
    @PostMapping("/page")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult page(@RequestBody RuleParam param) {

        Page<Rule> page = new Page<>(param.getPageNum(), param.getPageSize());

        Page<Rule> pageData = ruleService.page(page, new LambdaQueryWrapper<Rule>()
                .like(StringUtils.isNotEmpty(param.getRuleName()), Rule::getRuleName, param.getRuleName())
                .like(StringUtils.isNotEmpty(param.getRuleNo()), Rule::getRuleNo, param.getRuleNo())
                .eq(null != param.getProductId(), Rule::getProductId, param.getProductId())
                .eq(StringUtils.isNotEmpty(param.getStatus()), Rule::getStatus, param.getStatus())
        );

        return AjaxResult.success(pageData);
    }

    @ApiOperation("主键查询")
    @PostMapping("/getById/{id}")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult getById(@PathVariable Integer id) {

        Rule rule = ruleService.getById(id);

        RuleVo ruleVo = new RuleVo();

        BeanUtils.copyProperties(rule,ruleVo);

        List<RuleCondition> ruleConditions = ruleConditionService.list(new LambdaQueryWrapper<RuleCondition>().eq(RuleCondition::getRuleId, id));

        ruleVo.setRuleConditions(ruleConditions);

        return AjaxResult.success(ruleVo);
    }

    @ApiOperation("新增")
    @PostMapping("/insert")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult insert(@RequestBody RuleVo ruleVo) {
        ruleVo.setCreateBy(getUsername());
        return ruleService.add(ruleVo);
    }

    @ApiOperation("编辑")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult update(@RequestBody RuleVo ruleVo) {
        ruleVo.setUpdateBy(getUsername());
        return ruleService.updateRule(ruleVo);
    }
}
