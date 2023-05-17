package com.things.tcp.config;


import com.things.mqtt.utils.SpringUtil;
import com.things.tcp.handler.NettyMessageHandler;
import com.things.tcp.handler.TCPDeviceOnlineStatus;
import com.things.utils.ByteUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * netty消息处理
 *
 * @author DaiWei
 * @version 1.0.1
 * @Date 2022/10/17
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    public static Map<String, ChannelHandlerContext> channelMap = new HashMap<>();

    /**
     * HB的16进制字符
     */
    public static final String HEART_BEAT = "4842";

    public static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(50,
            100,
            300,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50), r -> new Thread(r, "netty_task_pool_" + r.hashCode()),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object payload) {
        //获取客户端请求的地址，获取到ip+端口
        String url = ctx.channel().remoteAddress().toString();
        //区分客户端请求的那个端口
        String serverPort = ctx.channel().localAddress().toString();
        String requestPort = serverPort.substring(serverPort.length() - 4);
        log.info("[{}]向[{}]端口写入数据:[{}]", url, requestPort, payload);
        log.info("当前活跃线程数{}", threadPool.getActiveCount());

        if (payload.toString().startsWith(HEART_BEAT)) {
            //心跳包处理
            heartBeat(ctx, payload);

        } else {

            //如果通道有注册过心跳包，则设备有效，进行消息处理
            if (channelMap.containsValue(ctx)){

                threadPool.execute(() -> {
                    String deviceId = channelMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)).get(ctx);
                    NettyMessageHandler bean = SpringUtil.getBean(NettyMessageHandler.class);
                    bean.nettyMessage(deviceId,payload);
                });

            }else {

                ctx.disconnect();
                log.info("无效的channel,通道未进行心跳认证");

            }

        }

    }


    /**
     * channel异常处理
     *
     * @param ctx   channel
     * @param cause 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        if (channelMap.containsValue(ctx)){

            String deviceId = channelMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)).get(ctx);
            channelMap.remove(deviceId);
            SpringUtil.getBean(TCPDeviceOnlineStatus.class).offline(deviceId);
            log.info("[Netty_Handler] connection error,exception:{} , DeviceId:{}", cause.getMessage(),deviceId);

        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        if (channelMap.containsValue(ctx)){

            String deviceId = channelMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)).get(ctx);
            channelMap.remove(deviceId);
            SpringUtil.getBean(TCPDeviceOnlineStatus.class).offline(deviceId);
            log.info("[Netty_Handler] Channel Inactive ,remove DeviceId:{} success", deviceId);

        }
    }

    public void heartBeat(ChannelHandlerContext ctx, Object msg) {
        String heartBeat = ByteUtil.hexStringToString(msg.toString());
        String deviceId = heartBeat.substring(heartBeat.lastIndexOf("=") + 1);
        if (!channelMap.containsKey(deviceId)) {
            channelMap.put(deviceId,ctx);
        }
        SpringUtil.getBean(TCPDeviceOnlineStatus.class).online(deviceId);
        log.info("收到设备[{}]心跳", deviceId);
    }

    /**
     * 向所有通道发送消息
     *
     * @param message 消息
     */
    public static void channelWrite(String message) {
        if (!channelMap.isEmpty()) {
            for (String key : channelMap.keySet()) {
                ChannelHandlerContext channelHandlerContext = channelMap.get(key);
                channelHandlerContext.write(message);
                channelHandlerContext.flush();
            }
        } else {
            log.info("netty当前无设备连接");
        }
    }

    /**
     * 向指定通道发送消息
     *
     * @param channelHandlerContext 通道
     * @param message               消息
     */
    public static void channelWrite(ChannelHandlerContext channelHandlerContext, String message) {
        if (channelMap.containsValue(channelHandlerContext)) {
            channelHandlerContext.write(message);
            channelHandlerContext.flush();
        }
    }

    /**
     * 通过key获取通道发送消息
     *
     * @param key     地址
     * @param message 消息
     */
    public static void channelWrite(String key, String message) {
        if (channelMap.containsKey(key)) {
            ChannelHandlerContext channelHandlerContext = channelMap.get(key);
            channelHandlerContext.write(message);
            channelHandlerContext.flush();
        } else {
            log.info("netty未获取到连接{}", key);
        }
    }

    public static ChannelHandlerContext getChannelHandlerContext(String url) {
        return channelMap.get(url);
    }

}
