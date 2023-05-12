package com.things.mqtt.domain;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CmdParam {

    @NotBlank(message="设备地址不能为空")
    String addr;

    @NotBlank(message="命令不能为空")
    String cmd;

    String mac;

    String msg;
}
