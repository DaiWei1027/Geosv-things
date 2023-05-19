package com.things.web.controller.link;


import com.things.common.core.controller.BaseController;
import com.things.common.core.domain.AjaxResult;
import com.things.sse.RealTimeLogServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 实时日志
 *
 * @author DaiWei
 */
@Api(tags = "实时日志")
@RestController
@RequestMapping("/real_time/log")
public class RealTimeLogController extends BaseController {

    /**
     * 创建连接
     *
     * @param userId 客户端id
     * @return SseEmitter
     */
    @ApiOperation("创建连接")
    @GetMapping("/connect/{userId}")
    public SseEmitter connect(@PathVariable String userId) {

        return RealTimeLogServer.connect(userId);
    }

    /**
     * 关闭连接
     *
     * @param clientId 客户端id
     * @return R
     */
    @GetMapping("/close/{clientId}")
    public AjaxResult close(@PathVariable("clientId") String clientId) {
        RealTimeLogServer.removeUser(clientId);
        return AjaxResult.success("连接关闭");
    }
}
