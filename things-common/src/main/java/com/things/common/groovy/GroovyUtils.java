package com.things.common.groovy;

import groovy.lang.GroovyClassLoader;
 
/**
 * 实例化脚本
 *
 * @author DaiWei
 */
public class GroovyUtils {
    /**
     * .
     * 获取实例化对象
     * @param script groovy脚本内容
     * @param <T>
     */
    public static <T> T instanceTaskGroovyScript(String script) throws IllegalAccessException, InstantiationException {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        Class taskClz = groovyClassLoader.parseClass(script);
        return (T) taskClz.newInstance();
    }
}