package com.things.common.groovy;

import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSDSProtocol implements GroovyPlugin {

    @Override
    public List<JSONObject> load(String data) {

        String addressStr = data.substring(0, 2);
        int addr = Integer.parseInt(addressStr, 16);

        String lenStr = data.substring(4, 6);
        int len = Integer.parseInt(lenStr, 16);

        List<JSONObject> jsonObjects = new ArrayList<>();
        JSONObject deviceData = new JSONObject();

        if (4 == len) {
            //总有功电能数据
            String energyStr = data.substring(6, 14);
            int energy = Integer.parseInt(energyStr, 16);

            double v = Double.parseDouble(String.valueOf(energy)) / 100;

            deviceData.put("event","energy");
            deviceData.put("energy", v);
            jsonObjects.add(deviceData);
            return jsonObjects;

        } else if (100 == len) {

            //100字节数据 包含电压、电流、有功、无功、功率、功因
            String voltageAStr = data.substring(30, 38);
            int voltageAInt = Integer.parseInt(voltageAStr, 16);

            String voltageBStr = data.substring(38, 46);
            int voltageBInt = Integer.parseInt(voltageBStr, 16);

            String voltageCStr = data.substring(46, 54);
            int voltageCInt = Integer.parseInt(voltageCStr, 16);

            String currentAStr = data.substring(54, 62);
            int currentAInt = Integer.parseInt(currentAStr, 16);

            String currentBStr = data.substring(62, 70);
            int currentBInt = Integer.parseInt(currentBStr, 16);

            String currentCStr = data.substring(70, 78);
            int currentCInt = Integer.parseInt(currentCStr, 16);

            String apStr = data.substring(102, 110);
            int apInt = Integer.parseInt(apStr, 16);

            String rpStr = data.substring(134, 142);
            int rpInt = Integer.parseInt(rpStr, 16);

            String pStr = data.substring(166, 174);
            int pInt = Integer.parseInt(pStr, 16);

            String pfStr = data.substring(198, 206);
            int pfInt = Integer.parseInt(pfStr, 16);

            double voltageA = (double) voltageAInt / 100;
            double voltageB = (double) voltageBInt / 100;
            double voltageC = (double) voltageCInt / 100;
            double currentA = (double) currentAInt / 1000;
            double currentB = (double) currentBInt / 1000;
            double currentC = (double) currentCInt / 1000;
            double pf = (double) pfInt / 1000;

            //modbus浮点型
            double ap = Float.intBitsToFloat(apInt) / 10;
            double rp = Float.intBitsToFloat(rpInt) / 10;
            double p = Float.intBitsToFloat(pInt) / 10;

            //保留一位小数点
            String apFormat = String.format("%.1f", ap);
            String rpFormat = String.format("%.1f", rp);
            String pFormat = String.format("%.1f", p);

            deviceData.put("event",   "currentAndVoltage");
            deviceData.put("deviceNo", addr);
            deviceData.put("voltageA", voltageA);
            deviceData.put("voltageB", voltageB);
            deviceData.put("voltageC", voltageC);
            deviceData.put("currentA", currentA);
            deviceData.put("currentB", currentB);
            deviceData.put("currentC", currentC);
            deviceData.put("ap", Double.parseDouble(apFormat));
            deviceData.put("rp", Double.parseDouble(rpFormat));
            deviceData.put("p", Double.parseDouble(pFormat));
            deviceData.put("pf", pf);
            jsonObjects.add(deviceData);
            return jsonObjects;
        }
        return jsonObjects;
    }
}
