package com.things.common.config;

import com.alibaba.fastjson2.JSONObject;
import com.things.common.utils.http.HttpUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author DaiWei
 * @date 2023/08/29 15:32
 **/
@Data
@Component
@ConfigurationProperties(prefix = "media")
public class MediaServerConfig {

    String serverIp;

    String mediaServerId;

    public static final String AND = "&";

    /**
     * 查询拉流代理
     *
     * @param page  页码
     * @param count 页数
     * @return
     */
    public JSONObject listProxy(int page, int count) {

        String api = "/api/proxy/list";

        String param0 = "page=".concat(String.valueOf(page));

        String param1 = "count=".concat(String.valueOf(count));

        String data = HttpUtils.sendGet(this.serverIp + api, param0 + AND + param1);

        return JSONObject.parseObject(data);
    }

    /**
     * 查询拉流代理
     *
     * @param app    app
     * @param stream stream
     * @return
     */
    public JSONObject getPlayUrl(String app, String stream) {

        String api = "/api/push/getPlayUrl";

        String param0 = "app=".concat(String.valueOf(app));

        String param1 = "stream=".concat(String.valueOf(stream));

        String param2 = "mediaServerId=".concat(String.valueOf(this.mediaServerId));

        String data = HttpUtils.sendGet(this.serverIp + api, param0 + AND + param1 + AND + param2);

        return JSONObject.parseObject(data);
    }

    /**
     * 查询国标设备
     *
     * @param page  页码
     * @param count 页数
     * @return
     */
    public JSONObject devices(int page, int count) {

        String api = "/api/device/query/devices";

        String param0 = "page=".concat(String.valueOf(page));

        String param1 = "count=".concat(String.valueOf(count));

        String data = HttpUtils.sendGet(this.serverIp + api, param0 + AND + param1);

        return JSONObject.parseObject(data);

    }

    /**
     * 查询通道
     *
     * @param page  页码
     * @param count 页数
     * @return
     */
    public JSONObject channels(int page, int count, String deviceId, String query, Boolean online, Boolean channelType, Boolean catalogUnderDevice) {

        String api = "/api/device/query/devices/" + deviceId + "/channels";

        String param0 = "page=".concat(String.valueOf(page));

        String param1 = "count=".concat(String.valueOf(count));

        String data = HttpUtils.sendGet(this.serverIp + api, param0 + AND + param1);

        return JSONObject.parseObject(data);
    }

    /**
     * 查询拉流代理
     *
     * @param deviceId 设备国标编码
     * @return
     */
    public JSONObject queryVideoDevice(String deviceId) {

        String api = "/api/device/query/devices/" + deviceId;

        String data = HttpUtils.sendGet(this.serverIp + api);

        return JSONObject.parseObject(data);
    }

    /**
     * 查询拉流代理
     *
     * @param deviceId  设备国标编码
     * @param channelId 通道国标编码
     * @return
     */
    public JSONObject play(String deviceId, String channelId) {
        String api = "/api/device/query/devices/" + deviceId + "/" + channelId;

        String data = HttpUtils.sendGet(this.serverIp + api);

        return JSONObject.parseObject(data);
    }
}
