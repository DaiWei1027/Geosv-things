package com.things.rule.bean;

import com.alibaba.fastjson2.JSONObject;
import com.things.common.constant.RedisConstants;
import com.things.common.core.redis.RedisCache;
import com.things.common.enums.RuleEnum;
import com.things.device.bean.RealTimeLogHandler;
import com.things.influxdb.vo.DeviceData;
import com.things.product.domain.EventParam;
import com.things.product.domain.vo.ProdEventParams;
import com.things.rule.domain.RuleCondition;
import com.things.rule.domain.vo.ActionVo;
import com.things.rule.domain.vo.RuleVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 规则处理器
 *
 * @author DaiWei
 * @date 2023/05/09 15:25
 **/
@Slf4j
@Component
@AllArgsConstructor
public class RuleHandler {

    public static final String LT = "<";
    public static final String LE = "<=";
    public static final String GT = ">";
    public static final String GE = ">=";
    public static final String EQ = "=";

    public static final String SCOPE = "设置范围";

    /**
     * 产品事件字段
     */
    public static final String EVENT = "event";

    private final RedisCache redisCache;

    private final ActionHandler actionHandler;

    private final RealTimeLogHandler realTimeLogHandler;

    @Async("ruleExecutor")
    public void handle(DeviceData deviceData) {

        String productId = deviceData.getProductId();

        JSONObject data = JSONObject.parseObject(deviceData.getData());
        //获取事件标识
        Object eventObject = data.get(EVENT);

        if (!Objects.isNull(eventObject)) {

            String event = eventObject.toString();

            //查询规则
            List<RuleVo> ruleVos = redisCache.getCacheList(RedisConstants.RULE + productId);

            ruleVos.forEach(ruleVo -> {

                List<Boolean> flags = Lists.newArrayList();

                List<RuleCondition> ruleConditions = ruleVo.getRuleConditions();

                for (RuleCondition ruleCondition : ruleConditions) {

                    if (event.equals(ruleCondition.getEventIdentify())) {

                        //通过规则设置的字段取设备数据中的值
                        Object value = data.get(ruleCondition.getParam());

                        if (null != value) {

                            flags.add(rule(ruleCondition, value));

                        }

                    }

                }

                //是否满足条件、执行动作
                boolean actionFlag = false;

                if (!Objects.isNull(ruleVo.getTriggering())) {

                    switch (ruleVo.getTriggering()) {

                        case RuleEnum.ALL:
                            actionFlag = flags.stream().allMatch(s -> s.equals(true));
                            break;
                        case RuleEnum.ANY:
                            actionFlag = flags.stream().anyMatch(s -> s.equals(true));
                            break;
                        default:
                            break;
                    }

                }

                if (actionFlag) {

                    List<ActionVo> actionVos = ruleVo.getActionVos();

                    if (!CollectionUtils.isEmpty(actionVos)) {
                        actionHandler.action(actionVos, deviceData);
                    }
                }

            });
        }
    }


    public boolean rule(RuleCondition ruleCondition, Object value) {

        //阈值
        String thresholdValue = ruleCondition.getValue();

        //关系运算符
        String operator = ruleCondition.getOperator();

        //判断参数是否为全数字
        boolean result = isNumeric(value.toString());

        boolean flag = false;

        if (result) {

            //如果参数为存数字则用大于小于换算
            BigDecimal threshold = BigDecimal.valueOf(Double.parseDouble(thresholdValue));

            BigDecimal target = BigDecimal.valueOf(Double.parseDouble(value.toString()));

            int compareTo = target.compareTo(threshold);

            switch (operator) {
                case LT:
                    flag = compareTo < 0;
                    break;
                case LE:
                    flag = compareTo < 0 || compareTo == 0;
                    break;
                case GT:
                    flag = compareTo > 0;
                    break;
                case GE:
                    flag = compareTo > 0 || compareTo == 0;
                    break;
                case EQ:
                    flag = compareTo == 0;
                    break;
                case SCOPE:
                    String[] scopes = thresholdValue.split(",");
                    BigDecimal mini = BigDecimal.valueOf(Double.parseDouble(scopes[0]));
                    BigDecimal max = BigDecimal.valueOf(Double.parseDouble(scopes[1]));
                    int miniCompare = target.compareTo(mini);
                    int maxCompare = target.compareTo(max);
                    flag = (miniCompare > 0 || miniCompare == 0) && (maxCompare < 0 || maxCompare == 0);
                default:
                    break;
            }

        } else {
            //如果参数不是存数字，那不可能运用大于小于运算。字符则用eq
            if (thresholdValue.equals(value.toString())) {
                flag = true;
            }

        }

        return flag;
    }

    public static boolean isNumeric(String str) {

        Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

}
