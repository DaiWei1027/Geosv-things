package com.things.device.domain.vo;

import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/05/19 10:30
 **/
@Data
public class OnlineCount {

    Integer onlineNumber;

    Integer offlineNumber;

    Integer total;
}
