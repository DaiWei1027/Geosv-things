package com.things.rule.bean.action;

import com.things.influxdb.vo.DeviceData;
import com.things.rule.domain.vo.ActionVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author DaiWei
 * @date 2023/05/10 16:20
 **/
@Slf4j
@Component
@AllArgsConstructor
public class MessageActionExec {

    public void exe(ActionVo actionVo , DeviceData deviceData){

    }
}
