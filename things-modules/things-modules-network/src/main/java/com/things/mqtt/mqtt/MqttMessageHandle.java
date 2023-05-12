package com.things.mqtt.mqtt;



import com.things.mqtt.aop.MqttService;
import com.things.mqtt.aop.MqttTopic;
import com.things.mqtt.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * MessageHandleService
 *
 * @author daiwei
 * @date 2022/10/20
 */
@Component
public class MqttMessageHandle implements MessageHandler {

    public static final Logger log = LoggerFactory.getLogger(MqttMessageHandle.class);

    // 包含 @MqttService注解 的类(Component)
    public static Map<String, Object> mqttServices;


    /**
     * 所有mqtt到达的消息都会在这里处理
     * 要注意这个方法是在线程池里面运行的
     * @param message message
     */
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        getMqttTopicService(message);
    }

    public Map<String, Object> getMqttServices(){
        if(mqttServices==null){
            mqttServices = SpringUtil.getBeansByAnnotation(MqttService.class);
        }
        return mqttServices;
    }

    public void getMqttTopicService(Message<?> message){
        // 在这里 我们根据不同的 主题 分发不同的消息
        String receivedTopic = message.getHeaders().get("mqtt_receivedTopic",String.class);
        if(receivedTopic==null || "".equals(receivedTopic)){
            return;
        }
        for(Map.Entry<String, Object> entry : getMqttServices().entrySet()){
        	// 把所有带有 @MqttService 的类遍历
            Class<?> clazz = entry.getValue().getClass();
            // 获取他所有方法
            Method[] methods = clazz.getDeclaredMethods();
            for ( Method method: methods ){
                if (method.isAnnotationPresent(MqttTopic.class)){
                	// 如果这个方法有 这个注解
                    MqttTopic handleTopic = method.getAnnotation(MqttTopic.class);
                    if(isMatch(receivedTopic,handleTopic.value())){
                    	// 并且 这个 topic 匹配成功
                        try {
                            method.invoke(SpringUtil.getBean(clazz),message);
                            return;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            log.error("代理错误");
                        } catch (InvocationTargetException e) {
                            log.error("执行 {} 方法出现错误",handleTopic.value(),e);
                        }
                    }
                }
            }
        }
    }


    /**
     * mqtt 订阅的主题与我实际的主题是否匹配
     * @param topic 是实际的主题
     * @param pattern 是我订阅的主题 可以是通配符模式
     * @return 是否匹配
     */
    public static boolean isMatch(String topic, String pattern){

        if((topic==null) || (pattern==null) ){
            return false;
        }

        if(topic.equals(pattern)){
            // 完全相等是肯定匹配的
            return true;
        }

        if("#".equals(pattern)){
            // # 号代表所有主题  肯定匹配的
            return true;
        }
        String[] splitTopic = topic.split("/");
        String[] splitPattern = pattern.split("/");

        boolean match = true;

        // 如果包含 # 则只需要判断 # 前面的
        for (int i = 0; i < splitPattern.length; i++) {
            if(!"#".equals(splitPattern[i])){
                // 不是# 号 正常判断
                if(i>=splitTopic.length){
                    // 此时长度不相等 不匹配
                    match = false;
                    break;
                }
                if(!splitTopic[i].equals(splitPattern[i]) && !"+".equals(splitPattern[i])){
                    // 不相等 且不等于 +
                    match = false;
                    break;
                }
            }
            else {
                // 是# 号  肯定匹配的
                break;
            }
        }

        return match;
    }

}

