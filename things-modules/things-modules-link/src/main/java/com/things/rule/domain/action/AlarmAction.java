package com.things.rule.domain.action;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/05/10 14:46
 **/
@Data
public class AlarmAction {

    @TableId(type = IdType.AUTO)
    Integer id;

    Integer actionId;

    String title;

    String content;

    String level;

}
