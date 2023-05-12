package com.things.influxdb.utils;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Configuration
public class InfluxDbOkHttpClientBuilderProvider implements org.springframework.boot.autoconfigure.influx.InfluxDbOkHttpClientBuilderProvider {

    @Override
    public OkHttpClient.Builder get() {
    	// 设置超时时间为100秒
        return new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS);
    }
}
