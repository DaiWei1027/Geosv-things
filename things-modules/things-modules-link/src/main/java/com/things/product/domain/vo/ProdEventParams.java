package com.things.product.domain.vo;

import com.things.product.domain.EventParam;
import com.things.product.domain.ProdEvent;
import lombok.Data;

import java.util.List;

/**
 * @author DaiWei
 * @date 2023/05/18 15:23
 **/
@Data
public class ProdEventParams {

    ProdEvent prodEvent;

    List<EventParam> eventParamList;
}
