package com.things.mqtt.mqtt;


import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * MqttConfigV2
 *
 * @author daiwei
 * @date 2022/10/20
 */
@Slf4j
@Configuration
public class MqttFactory {

    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private MqttMessageHandle mqttMessageHandle;


    //Mqtt 客户端工厂 所有客户端从这里产生
    @Bean("mqttPahoClientFactory")
    public MqttPahoClientFactory mqttPahoClientFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(mqttProperties.getHostUrl().split(","));
        options.setUserName(mqttProperties.getUsername());
        options.setPassword(mqttProperties.getPassword().toCharArray());
        factory.setConnectionOptions(options);
        log.info("============mqtt factory 启动成功 ================");
        return factory;
    }

    // Mqtt 管道适配器
    @Bean("adapter")
    public MqttPahoMessageDrivenChannelAdapter adapter(MqttPahoClientFactory mqttPahoClientFactory){
        return new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getInClientId(),mqttPahoClientFactory,mqttProperties.getDefaultTopic().split(","));
    }

    // 消息生产者 (接收,处理来自mqtt的消息)
    @Bean("mqttInbound")
    public IntegrationFlow mqttInbound(MqttPahoMessageDrivenChannelAdapter adapter) {
        adapter.setCompletionTimeout(5000);
        adapter.setQos(1);
        // 设置转换器，接收bytes
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        converter.setPayloadAsBytes(true);
        adapter.setConverter(converter);

        return IntegrationFlows.from( adapter)
                .channel(new ExecutorChannel(mqttThreadPoolTaskExecutor()))
                .handle(mqttMessageHandle)
                .get();
    }

    // 出站处理器 (向 mqtt 发送消息)
    @Bean("mqttOutboundFlow")
    public IntegrationFlow mqttOutboundFlow(MqttPahoClientFactory mqttPahoClientFactory) {

        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(mqttProperties.getOutClientId(),mqttPahoClientFactory);
        handler.setAsync(true);
        handler.setConverter(new DefaultPahoMessageConverter());
        handler.setDefaultTopic(mqttProperties.getDefaultTopic());
        return IntegrationFlows.from( "mqttOutboundChannel").handle(handler).get();
    }

    /******************************************** 字节流管道  ********************************************/
//
//    // Mqtt 管道适配器
//    @Bean("adapter2")
//    public MqttPahoMessageDrivenChannelAdapter adapter2(MqttPahoClientFactory mqttPahoClientFactory){
//        return new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getInClientId()+ "-byte",mqttPahoClientFactory,mqttProperties.getByteTopic().split(","));
//    }
//
//    // 消息生产者 (接收,处理来自mqtt的消息)
//    @Bean("mqttInbound2")
//    public IntegrationFlow mqttInbound2(MqttPahoMessageDrivenChannelAdapter adapter2) {
//        adapter2.setCompletionTimeout(5000);
//        adapter2.setQos(1);
//
//        return IntegrationFlows.from( adapter2)
//                .channel(new ExecutorChannel(mqttThreadPoolTaskExecutor()))
//                .handle(mqttMessageHandle)
//                .get();
//    }
//
//
//    // 出站处理器 (向 mqtt 发送消息)
//    @Bean("mqttOutboundFlow2")
//    public IntegrationFlow mqttOutboundFlow2(MqttPahoClientFactory mqttPahoClientFactory) {
//
//        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(mqttProperties.getOutClientId() + "-byte",mqttPahoClientFactory);
//        handler.setAsync(true);
//        handler.setConverter(new DefaultPahoMessageConverter());
//        handler.setDefaultTopic(mqttProperties.getDefaultTopic());
//        return IntegrationFlows.from( "mqttOutboundChannel2").handle(handler).get();
//    }

    @Bean
    public ThreadPoolTaskExecutor mqttThreadPoolTaskExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 最大可创建的线程数
        int maxPoolSize = 200;
        executor.setMaxPoolSize(maxPoolSize);
        // 核心线程池大小
        int corePoolSize = 50;
        executor.setCorePoolSize(corePoolSize);
        // 队列最大长度
        int queueCapacity = 1000;
        executor.setQueueCapacity(queueCapacity);
        // 线程池维护线程所允许的空闲时间
        int keepAliveSeconds = 300;
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        return executor;
    }
}

