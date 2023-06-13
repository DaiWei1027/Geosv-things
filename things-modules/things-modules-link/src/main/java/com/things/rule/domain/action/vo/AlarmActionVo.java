package com.things.rule.domain.action.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.things.framework.param.Pagination;
import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/05/10 14:46
 **/
@Data
public class AlarmActionVo extends Pagination {

    String title;

    String level;

}
