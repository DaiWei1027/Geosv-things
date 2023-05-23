package com.things.web.controller.link;

import com.things.common.core.domain.AjaxResult;
import com.things.common.core.redis.RedisCache;
import com.things.device.domain.Device;
import com.things.device.message.execute.CommandExecute;
import com.things.device.service.IDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author DaiWei
 * @date 2023/05/19 13:16
 **/
@Api(tags = "命令下发")
@RestController
@RequestMapping("/link/command")
@AllArgsConstructor
public class CommandController {

    private final IDeviceService deviceService;

    private final CommandExecute commandExecute;


    @ApiOperation("命令下发")
    @PostMapping("/send")
    public AjaxResult insert(String deviceId, String command) {

        Device device = deviceService.selectByDeviceId(deviceId);

        if (Objects.isNull(device)) {
            return AjaxResult.error("设备不存在");
        }

        //格式化指令 去除中文 英文的空格
        command = command.replace(" ", "").replace(" ", "");

        //发送指令
        commandExecute.execute(device, command);

        return AjaxResult.success("指令发送成功");
    }

}
