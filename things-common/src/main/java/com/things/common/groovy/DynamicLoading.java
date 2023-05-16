package com.things.common.groovy;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;

public class DynamicLoading {

    /**
     * 执行脚本
     */
    public static void execGroovy(String script) throws InstantiationException, IllegalAccessException {

        GroovyPlugin objClass = GroovyUtils.instanceTaskGroovyScript(script);

        String hello = "010308001A000200030004A615";

        List<JSONObject> load = objClass.load(hello);

        System.out.println("返回值是：" + load);
    }

}