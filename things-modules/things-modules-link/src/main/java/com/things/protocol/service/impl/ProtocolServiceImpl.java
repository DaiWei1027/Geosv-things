package com.things.protocol.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.things.common.constant.RedisConstants;
import com.things.common.core.redis.RedisCache;
import com.things.common.enums.DeviceStatus;
import com.things.common.groovy.GroovyPlugin;
import com.things.common.groovy.GroovyUtils;
import com.things.protocol.domain.Protocol;
import com.things.protocol.domain.vo.ProtocolExecuteParam;
import com.things.protocol.mapper.ProtocolMapper;
import com.things.protocol.service.IProtocolService;
import com.things.protocol.utils.ProtocolManage;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;

/**
 * @author DaiWei
 * @date 2023/03/31 13:11
 **/
@Service
@AllArgsConstructor
public class ProtocolServiceImpl extends ServiceImpl<ProtocolMapper, Protocol> implements IProtocolService {

    private final ProtocolMapper protocolMapper;

    private final ProtocolManage protocolManage;

    @Override
    public int insert(Protocol protocol) {
        int insert = protocolMapper.insert(protocol);
        protocolUpdate(protocol.getId(), protocol.getStatus(), protocol.getProtocolContent());
        return insert;
    }

    @Override
    public int update(Protocol protocol) {
        int updateById = protocolMapper.updateById(protocol);
        protocolUpdate(protocol.getId(), protocol.getStatus(), protocol.getProtocolContent());
        return updateById;
    }

    @Override
    public JSONObject execute(ProtocolExecuteParam param) {

        String protocolContent = StringEscapeUtils.unescapeHtml4(param.getProtocolContent());

        String body = param.getParams();

        try {
            GroovyPlugin groovyPlugin = GroovyUtils.instanceTaskGroovyScript(protocolContent);
            return groovyPlugin.load(body);
        } catch (Exception e) {
            throw new RuntimeException("脚本加载失败");
        }

    }

    @Override
    public int disabled(Integer id, String status) {
        Protocol protocol = protocolMapper.selectById(id);
        protocol.setStatus(status);
        int updateById = protocolMapper.updateById(protocol);
        protocolUpdate(protocol.getId(), protocol.getStatus(), protocol.getProtocolContent());
        return updateById;
    }

    /**
     * 更新产品协议缓存
     *
     * @param id              主键
     * @param status          状态
     * @param protocolContent 协议内容
     */
    public void protocolUpdate(int id, String status, String protocolContent) {

        if (DeviceStatus.ENABLE.getCode().equals(status)) {

            try {
                protocolManage.update(id, protocolContent);
            } catch (Exception e) {
                throw new RuntimeException("脚本内容不合规，协议内容更新失败");
            }

        }

        if (DeviceStatus.DISABLE.getCode().equals(status)) {

            protocolManage.delete(id);

        }

    }
}
