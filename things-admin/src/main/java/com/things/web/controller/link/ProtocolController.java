package com.things.web.controller.link;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.things.common.constant.DeviceConstants;
import com.things.common.core.controller.BaseController;
import com.things.common.core.domain.AjaxResult;
import com.things.common.groovy.GroovyUtils;
import com.things.common.utils.StringUtils;
import com.things.protocol.domain.Protocol;
import com.things.protocol.domain.vo.ProtocolExecuteParam;
import com.things.protocol.domain.vo.ProtocolParam;
import com.things.protocol.domain.vo.ProtocolVo;
import com.things.protocol.domain.vo.ScriptParam;
import com.things.protocol.service.IProtocolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 协议管理
 *
 * @author DaiWei
 * @date 2023/03/31 13:15
 **/
@Api(tags = "协议管理")
@RestController
@RequestMapping("/link/protocol")
public class ProtocolController extends BaseController {

    @Autowired
    private IProtocolService protocolService;

    @ApiOperation("新增")
    @PostMapping("/insert")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult insert(@RequestBody Protocol protocol) {
//        checkScript(protocol.getProtocolContent());
        return toAjax(protocolService.insert(protocol));
    }

    @ApiOperation("编辑")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult update(@RequestBody Protocol protocol) {
        checkScript(protocol.getProtocolContent());
        return toAjax(protocolService.update(protocol));
    }

    @ApiOperation("分页查询")
    @PostMapping("/page")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult page(@RequestBody ProtocolParam param) {

        Page<Protocol> page = new Page<>(param.getPageNum(), param.getPageSize());

        Page<Protocol> pageData = protocolService.page(page, new LambdaQueryWrapper<Protocol>()
                .like(StringUtils.isNotEmpty(param.getProtocolName()), Protocol::getProtocolName, param.getProtocolName())
                .like(StringUtils.isNotEmpty(param.getProtocolMark()), Protocol::getProtocolMark, param.getProtocolMark())
                .like(StringUtils.isNotEmpty(param.getProtocolVersion()), Protocol::getProtocolVersion, param.getProtocolVersion())
                .eq(StringUtils.isNotEmpty(param.getStatus()), Protocol::getStatus, param.getStatus())
                .eq(StringUtils.isNotEmpty(param.getLanguage()), Protocol::getLanguage, param.getLanguage())
                .eq(StringUtils.isNotEmpty(param.getType()), Protocol::getType, param.getType())
                .eq(StringUtils.isNotEmpty(param.getDataType()), Protocol::getDataType, param.getDataType())
        );

        return AjaxResult.success(pageData);
    }

    @ApiOperation("查询全部启用")
    @PostMapping("/all")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult list(){

        List<Protocol> list = protocolService.list(new LambdaQueryWrapper<Protocol>().eq(Protocol::getStatus, DeviceConstants.ENABLE));
        List<ProtocolVo> vos = Lists.newArrayList();
        for (Protocol protocol : list) {
            ProtocolVo protocolVo = new ProtocolVo();
            BeanUtils.copyProperties(protocol,protocolVo);
            vos.add(protocolVo);
        }

        return AjaxResult.success(vos);
    }

    @ApiOperation("id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult getById(@PathVariable int id){

        return AjaxResult.success(protocolService.getById(id));

    }

    @ApiOperation("启用|禁用")
    @GetMapping("/status")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult status(int id, String status) {

        return protocolService.disabled(id, status);
    }

    @ApiOperation("保存脚本")
    @PostMapping("/saveScript")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult saveScript(@RequestBody ScriptParam param) {

        checkScript(param.getProtocolContent());

        Protocol protocol = protocolService.getById(param.getId());
        protocol.setProtocolContent(param.getProtocolContent());
        return AjaxResult.success(protocolService.save(protocol));
    }

    @ApiOperation("执行脚本")
    @PostMapping("/execute")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult execute(@RequestBody ProtocolExecuteParam param) {
        return AjaxResult.success(protocolService.execute(param));
    }

    /**
     * 验证Java脚本是否合规
     *
     * @param script 脚本内容
     */
    public void checkScript(String script) {

        try {
            //动态加载错误则表示脚本不合规
            GroovyUtils.instanceTaskGroovyScript(script);

        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("脚本内容不合规");
        }

    }
}
