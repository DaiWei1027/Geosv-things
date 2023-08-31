package com.things.web.controller.media;

import com.things.common.config.MediaServerConfig;
import com.things.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 流媒体中心
 *
 * @author DaiWei
 * @date 2023/08/29 15:30
 **/
@Api(tags = "流媒体中心")
@RestController
@RequestMapping("/media")
@AllArgsConstructor
public class MediaServerController {

    private final MediaServerConfig mediaServer;

    @GetMapping(value = "/list")
    @ApiOperation("推流列表查询")
    @Parameter(name = "page", description = "当前页")
    @Parameter(name = "count", description = "每页查询数量")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    public AjaxResult list(@RequestParam(required = false) Integer page,
                           @RequestParam(required = false) Integer count) {

        return AjaxResult.success(mediaServer.listProxy(page, count));
    }

    @GetMapping(value = "/getPlayUrl")
    @ApiOperation("获取推流播放地址")
    @Parameter(name = "app", description = "应用名", required = true)
    @Parameter(name = "stream", description = "流id", required = true)
    public AjaxResult getPlayUrl(@RequestParam String app, @RequestParam String stream) {

        return AjaxResult.success(mediaServer.getPlayUrl(app, stream));
    }

    @GetMapping(value = "/devices")
    @ApiOperation("国标设备列表")
    @Parameter(name = "page", description = "当前页")
    @Parameter(name = "count", description = "每页查询数量")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    public AjaxResult devices(@RequestParam(required = false) Integer page,
                              @RequestParam(required = false) Integer count) {

        return AjaxResult.success(mediaServer.devices(page, count));
    }

    @GetMapping("/devices/{deviceId}/channels")
    @ApiOperation("分页查询通道")
    @Parameter(name = "deviceId", description = "设备国标编号", required = true)
    @Parameter(name = "page", description = "当前页", required = true)
    @Parameter(name = "count", description = "每页查询数量", required = true)
    @Parameter(name = "query", description = "查询内容")
    @Parameter(name = "online", description = "是否在线")
    @Parameter(name = "channelType", description = "设备/子目录-> false/true")
    @Parameter(name = "catalogUnderDevice", description = "是否直属与设备的目录")
    public AjaxResult channels(@PathVariable String deviceId,
                               int page, int count,
                               @RequestParam(required = false) String query,
                               @RequestParam(required = false) Boolean online,
                               @RequestParam(required = false) Boolean channelType,
                               @RequestParam(required = false) Boolean catalogUnderDevice) {

        return AjaxResult.success(mediaServer.channels(page, count, deviceId, query, online, channelType, catalogUnderDevice));
    }

    @ApiOperation("查询国标设备")
    @Parameter(name = "deviceId", description = "设备国标编号", required = true)
    @GetMapping("/devices/{deviceId}")
    public AjaxResult devices(@PathVariable String deviceId) {

        return AjaxResult.success(mediaServer.queryVideoDevice(deviceId));
    }

    @ApiOperation("开始点播")
    @Parameter(name = "deviceId", description = "设备国标编号", required = true)
    @Parameter(name = "channelId", description = "通道国标编号", required = true)
    @GetMapping("/start/{deviceId}/{channelId}")
    public AjaxResult play(@PathVariable String deviceId,
                           @PathVariable String channelId) {
        return AjaxResult.success(mediaServer.play(deviceId, channelId));
    }
}
