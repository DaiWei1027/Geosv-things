package com.things.common.groovy;

import com.alibaba.fastjson2.JSONObject;

/**
 * 脚本父级接口
 *
 * @author DaiWei
 * @date 2023-5/11
 */
public interface GroovyPlugin {

    /**
     * 执行脚本方法
     *
     * @param param 参数
     * @return String
     */
    JSONObject load(String param);
}