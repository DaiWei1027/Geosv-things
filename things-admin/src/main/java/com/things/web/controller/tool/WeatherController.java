package com.things.web.controller.tool;

import com.alibaba.fastjson2.JSONObject;
import com.things.common.core.domain.AjaxResult;
import com.things.framework.utils.WeatherUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 天气API
 *
 * @author DaiWei
 * @date 2023/07/03 14:14
 **/
@Api(tags = "天气API")
@RestController
@RequestMapping("/weather")
@AllArgsConstructor
public class WeatherController {


    @ApiOperation("获取天气")
    @PostMapping("/get/{districtId}")
    public AjaxResult getWeather(@PathVariable String districtId) {

        try {
            JSONObject weather = WeatherUtil.getWeather(districtId, "all");

            if (0 == weather.getInteger("status")){

                return AjaxResult.success(weather.get("result"));

            }else {

                return AjaxResult.error(weather.getString("message"));

            }

        } catch (Exception e) {
            return AjaxResult.error("查询失败请稍后再试");
        }
    }
}
