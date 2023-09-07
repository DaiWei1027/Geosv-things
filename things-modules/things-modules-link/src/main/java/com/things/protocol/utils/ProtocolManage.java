package com.things.protocol.utils;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.things.common.constant.RedisConstants;
import com.things.common.core.redis.RedisCache;
import com.things.common.groovy.GroovyPlugin;
import com.things.common.groovy.GroovyUtils;
import com.things.protocol.domain.Protocol;
import com.things.protocol.mapper.ProtocolMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author DaiWei
 * @date 2023/04/26 16:13
 **/
@Slf4j
@Component
@AllArgsConstructor
public class ProtocolManage {

    private final RedisCache redisCache;

    private final ProtocolMapper protocolMapper;

    private final ConcurrentHashMap<Integer, GroovyPlugin> groovyPluginMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {

        List<Protocol> protocols = protocolMapper.selectList(new QueryWrapper<>());

        AtomicInteger updateNumber = new AtomicInteger();

        protocols.forEach(protocol -> {

            redisCache.setCacheObject(RedisConstants.PROTOCOL + protocol.getId(), protocol);

            //加载动态脚本实例化
            try {
                GroovyPlugin objClass = GroovyUtils.instanceTaskGroovyScript(protocol.getProtocolContent());
                groovyPluginMap.put(protocol.getId(), objClass);
                updateNumber.getAndIncrement();
            } catch (Exception e) {
                log.error("协议[{}]加载动态脚本失败:{}", protocol.getProtocolName(), e.getMessage());
            }

        });

        log.info("协议管理器：初始化协议缓存成功：[{}]", updateNumber);
    }

    /**
     * 解析数据为JSON
     *
     * @param protocolId 协议id
     * @param payload    数据
     * @return JSONObject
     */
    public List<JSONObject> load(int protocolId, Object payload) {

        GroovyPlugin groovyPlugin = groovyPluginMap.get(protocolId);

        if (!Objects.isNull(groovyPlugin)) {
            //缓存不存在，则查询数据库
            Protocol protocol = protocolMapper.selectById(protocolId);
            if (!Objects.isNull(protocol)) {

                try {

                    groovyPlugin = GroovyUtils.instanceTaskGroovyScript(protocol.getProtocolContent());
                    groovyPluginMap.put(protocol.getId(), groovyPlugin);

                } catch (Exception e) {
                    log.error("协议管理器，加载协议[{}]动态脚本失败：{}", protocol.getProtocolName(), e.getMessage());
                }

            }

        }

        return groovyPlugin.load(payload.toString());

    }

    public void update(int protocolId, String protocolContent) throws IllegalAccessException, InstantiationException {

        redisCache.setCacheObject(RedisConstants.PROTOCOL + protocolId, protocolContent);

        GroovyPlugin groovyPlugin = GroovyUtils.instanceTaskGroovyScript(protocolContent);

        groovyPluginMap.put(protocolId, groovyPlugin);

    }

    public void delete(int protocolId) {

        redisCache.deleteObject(RedisConstants.PROTOCOL + protocolId);

        groovyPluginMap.remove(protocolId);

    }



}
