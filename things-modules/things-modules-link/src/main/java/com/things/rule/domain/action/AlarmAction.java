package com.things.rule.domain.action;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DaiWei
 * @date 2023/05/10 14:46
 **/
@Data
public class AlarmAction {

    @TableId(type = IdType.AUTO)
    Integer id;

    @ApiModelProperty(value = "不传")
    Integer actionId;

    @ApiModelProperty(value = "告警标题")
    String title;

    @ApiModelProperty(value = "告警内容")
    String content;

    @ApiModelProperty(value = "告警级别")
    String level;

}
