package com.things.web.controller.common;

import com.alibaba.fastjson2.JSONObject;

public class TVSMessageHandler {
    public static void main(String[] args) {

        String in = "";

        if (args != null && args.length != 0) {
            in = args[0];
        }
        String data = in.toString();
        //例如 01 03 08 00 1A 00 02 00 03 00 04 A6 15
        String addressStr = data.substring(0, 2);
        int addr = Integer.parseInt(addressStr, 16);

        String typeStr = data.substring(2, 4);
        int type = Integer.parseInt(typeStr, 16);

        String temperatureString = data.substring(6, 10);
        int temperature = Integer.parseInt(temperatureString, 16);

        String xStr = data.substring(10, 14);
        int shake1 = Integer.parseInt(xStr, 16);

        String yStr = data.substring(14, 18);
        int shake2 = Integer.parseInt(yStr, 16);

        String zStr = data.substring(18, 22);
        int shake3 = Integer.parseInt(zStr, 16);


        double temp = (double) temperature / 10;
        double x = (double) shake1 / 10;
        double y = (double) shake2 / 10;
        double z = (double) shake3 / 10;

        JSONObject deviceData = new JSONObject();

        deviceData.put("addr", addr);
        deviceData.put("temp", temp);
        deviceData.put("x", x);
        deviceData.put("y", y);
        deviceData.put("z", z);

        System.out.println(deviceData);
    }
}
