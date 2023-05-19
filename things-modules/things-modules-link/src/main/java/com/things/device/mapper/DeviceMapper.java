package com.things.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.things.device.domain.Device;
import com.things.device.domain.vo.OnlineCount;
import com.things.protocol.domain.Protocol;
import org.apache.ibatis.annotations.Param;

/**
 * @author DaiWei
 * @date 2023/03/31 11:58
 **/
public interface DeviceMapper extends BaseMapper<Device> {
    void offline(@Param("deviceId") String deviceId);

    void online(@Param("deviceId") String deviceId);

    OnlineCount onlineCount();

}
