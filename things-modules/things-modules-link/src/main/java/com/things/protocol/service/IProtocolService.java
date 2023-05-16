package com.things.protocol.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.things.protocol.domain.Protocol;
import com.things.protocol.domain.vo.ProtocolExecuteParam;

import java.util.List;

/**
 * @author DaiWei
 * @date 2023/03/31 13:10
 **/
public interface IProtocolService extends IService<Protocol> {

    int insert(Protocol protocol);

    int update(Protocol protocol);

    int disabled(Integer id, String status);

    List<JSONObject> execute(ProtocolExecuteParam param);




}
