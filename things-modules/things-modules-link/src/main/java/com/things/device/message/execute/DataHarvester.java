package com.things.device.message.execute;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.things.common.constant.DeviceConstants;
import com.things.device.domain.Device;
import com.things.device.domain.SubDevice;
import com.things.device.mapper.DeviceMapper;
import com.things.device.service.ISubDeviceService;
import com.things.utils.ByteUtil;
import com.things.utils.Crc16Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 设备数据采集器
 *
 * @author DaiWei
 * @date 2023/05/22 15:17
 **/
@Slf4j
@Component("harvester")
public class DataHarvester {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ISubDeviceService subDeviceService;

    @Autowired
    private CommandExecute commandExecute;

    @Autowired
    @Qualifier("commonExecutor")
    private ThreadPoolTaskExecutor commonExecutor;

    /**
     * 该方法由定时器执行
     *
     * @param productId 产品id
     * @param command 指令
     */
    public void execute(String productId, String command) {

        commonExecutor.execute(() -> {

            List<Device> devices = deviceMapper.selectList(new LambdaQueryWrapper<Device>().eq(Device::getProductId, productId));

            devices.forEach(device -> {

                if (DeviceConstants.GATEWAY.equals(device.getDeviceType())) {

                    List<SubDevice> subDevices = subDeviceService.listByGatewayId(device.getId());

                    subDevices.forEach(subDevice -> {

                        if (DeviceConstants.MODBUS.equals(device.getRuleType())) {

                            String modbus = modbus(subDevice.getDeviceId(), command);
                            // 同一个网关下的设备 下发指令需要间隔时间 否则会有数据冲突
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            commandExecute.execute(device, modbus);

                        } else if (DeviceConstants.MQTT.equals(device.getRuleType())) {

                            commandExecute.execute(device, command);

                        }

                    });

                    log.info("执行设备采集器：设备名称：{}，设备类型：{}，子设备数量：{}", device.getDeviceName(), device.getDeviceType(), subDevices.size());

                } else {

                    commandExecute.execute(device, command);

                }

            });

        });

    }

    /**
     * 组装modbus指令
     *
     * @param addr    modbus地址
     * @param command 指令
     * @return 完整指令 地址+指令+CRC16
     */
    public String modbus(String addr, String command) {

        if (!addr.contains("0")) {
            addr = "0" + addr;
        }

        command = addr + command;

        int[] bytes = ByteUtil.hexString2Ints(command);

        String crc16 = Crc16Utils.crc16(bytes);

        return command + crc16;
    }
}
