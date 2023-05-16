package com.things.common.groovy;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class TVSProtocol implements GroovyPlugin{
    @Override
    public List<JSONObject> load(String param) {
        //例如 01 03 08 00 1A 00 02 00 03 00 04 A6 15
        String addressStr = param.substring(0, 2);
        int addr = Integer.parseInt(addressStr, 16);

        String typeStr = param.substring(2, 4);
        int type = Integer.parseInt(typeStr, 16);

        String temperatureString = param.substring(6, 10);
        int temperature = Integer.parseInt(temperatureString, 16);

        String xStr = param.substring(10, 14);
        int shake1 = Integer.parseInt(xStr, 16);

        String yStr = param.substring(14, 18);
        int shake2 = Integer.parseInt(yStr, 16);

        String zStr = param.substring(18, 22);
        int shake3 = Integer.parseInt(zStr, 16);


        double temp = (double) temperature / 10;
        double x = (double) shake1 / 10;
        double y = (double) shake2 / 10;
        double z = (double) shake3 / 10;

        List<JSONObject> jsonObjects = Lists.newArrayList();
        JSONObject deviceData = new JSONObject();

        deviceData.put("addr", addr);
        deviceData.put("temp", temp);
        deviceData.put("x", x);
        deviceData.put("y", y);
        deviceData.put("z", z);
        jsonObjects.add(deviceData);
        return jsonObjects;
    }
}
