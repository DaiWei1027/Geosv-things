package com.things.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 消息推送
 *
 * @author DaiWei
 * @date 2022/11/29
 */
@Slf4j
public class RealTimeLogServer {
    /**
     * 当前连接数
     */
    private static AtomicInteger count = new AtomicInteger(0);

    /**
     * 使用map对象，便于根据userId来获取对应的SseEmitter，或者放redis里面
     */
    private static Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    /**
     * 创建用户连接并返回 SseEmitter
     *
     * @param clientId 客户端id
     * @return SseEmitter
     */
    public static SseEmitter connect(String clientId) {
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        SseEmitter sseEmitter = new SseEmitter(0L);
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(clientId));
        sseEmitter.onError(errorCallBack(clientId));
        sseEmitter.onTimeout(timeoutCallBack(clientId));
        sseEmitterMap.put(clientId, sseEmitter);
        // 数量+1
        count.getAndIncrement();
        log.info("实时日志:SseEmitter创建新的sse连接，当前客户端id：{}", clientId);
        sendMessage(clientId, "通道连接成功，等待数据中........");
        return sseEmitter;
    }

    /**
     * 给指定用户发送信息
     *
     * @param clientId 客户端id
     * @param message  消息
     */
    public static void sendMessage(String clientId, String message) {
        if (sseEmitterMap.containsKey(clientId)) {
            try {
                // sseEmitterMap.get(userId).send(message, MediaType.APPLICATION_JSON);
                sseEmitterMap.get(clientId).send(message);
            } catch (IOException e) {
                log.error("实时日志:SseEmitter用户[{}]推送异常:{}", clientId, e.getMessage());
                removeUser(clientId);
            }
        }
    }

    /**
     * 群发消息
     *
     * @param wsInfo 消息
     * @param ids    客户端id集合
     */
    public static void batchSendMessage(String wsInfo, List<String> ids) {
        ids.forEach(userId -> sendMessage(wsInfo, userId));
    }

    /**
     * 群发所有人
     *
     * @param wsInfo 消息
     */
    public static void batchSendMessage(String wsInfo) {

        sseEmitterMap.forEach((k, v) -> {
            try {
                v.send(wsInfo, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                log.error("实时日志:SseEmitter用户[{}]推送异常:{}", k, e.getMessage());
                removeUser(k);
            }
        });
    }


    /**
     * 移除用户连接
     *
     * @param clientId 客户端id
     */
    public static void removeUser(String clientId) {
        sseEmitterMap.remove(clientId);
        // 数量-1
        count.getAndDecrement();
        log.info("实时日志:SseEmitter移除用户：{}", clientId);
    }

    /**
     * 获取当前连接信息
     *
     * @return 连接信息
     */
    public static List<String> getIds() {
        return new ArrayList<>(sseEmitterMap.keySet());
    }

    /**
     * 获取当前连接数量
     *
     * @return 连接数量
     */
    public static int getUserCount() {
        return count.intValue();
    }

    /**
     * 回调
     *
     * @param clientId 客户端id
     * @return Runnable
     */
    private static Runnable completionCallBack(String clientId) {
        return () -> {
            log.info("实时日志:SseEmitter结束连接：{}", clientId);
            removeUser(clientId);
        };
    }

    private static Runnable timeoutCallBack(String userId) {
        return () -> {
            log.info("实时日志:SseEmitter连接超时：{}", userId);
            removeUser(userId);
        };
    }

    private static Consumer<Throwable> errorCallBack(String userId) {
        return throwable -> {
            log.info("实时日志:SseEmitter连接异常：{}", userId);
            removeUser(userId);
        };
    }
}
