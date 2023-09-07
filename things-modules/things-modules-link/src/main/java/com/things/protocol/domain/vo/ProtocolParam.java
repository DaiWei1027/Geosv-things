package com.things.protocol.domain.vo;

import com.things.framework.param.Pagination;
import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/03/31 13:31
 **/
@Data
public class ProtocolParam extends Pagination {

    String protocolName;

    String protocolMark;

    String protocolVersion;

    String type;

    String dataType;

    String status;

    String language;

}
