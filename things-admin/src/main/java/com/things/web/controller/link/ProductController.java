package com.things.web.controller.link;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.things.common.constant.DeviceConstants;
import com.things.common.constant.RedisConstants;
import com.things.common.core.controller.BaseController;
import com.things.common.core.domain.AjaxResult;
import com.things.common.core.redis.RedisCache;
import com.things.common.enums.DeviceStatus;
import com.things.common.utils.StringUtils;
import com.things.device.domain.Device;
import com.things.device.domain.SubDevice;
import com.things.device.service.IDeviceService;
import com.things.device.service.ISubDeviceService;
import com.things.product.domain.Product;
import com.things.product.domain.vo.ProductParams;
import com.things.product.service.IProductService;
import com.things.product.domain.vo.ProductVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 产品管理
 *
 * @author DaiWei
 * @date 2023/04/03 14:41
 **/
@Api(tags = "产品管理")
@RestController
@RequestMapping("/link/product")
public class ProductController extends BaseController {

    @Autowired
    private IProductService productService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private ISubDeviceService subDeviceService;
    @Autowired
    private RedisCache redisCache;


    @ApiOperation("新增")
    @PostMapping("/insert")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult insert(@RequestBody Product product) {
        product.setCreateBy(getUsername());
        product.setCreateTime(new Date());
        productService.save(product);
//        ProductParams productParams = new ProductParams();
//        productParams.setProduct(product);
//        redisCache.setCacheObject(RedisConstants.PRODUCT + product.getId(), productParams);
        return AjaxResult.success();
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult update(@RequestBody Product product) {
        product.setUpdateBy(getUsername());
        product.setUpdateTime(new Date());
        redisCache.setCacheObject(RedisConstants.PRODUCT + product.getId(), product);
        return toAjax(productService.updateById(product));
    }

    @ApiOperation("启用|停用")
    @GetMapping("/status")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    public AjaxResult status(Integer id, String status) {
        Product product = productService.getById(id);
        product.setStatus(status);
        product.setUpdateBy(getUsername());

        if (DeviceStatus.ENABLE.getCode().equals(status)) {
            redisCache.setCacheObject(RedisConstants.PRODUCT + product.getId(), product);
        } else if (DeviceStatus.DISABLE.getCode().equals(status)) {
            redisCache.deleteObject(RedisConstants.PRODUCT + product.getId());
        }

        return AjaxResult.success(productService.updateById(product));
    }

    @ApiOperation("删除")
    @PostMapping("/delete/{id}")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult delete(@PathVariable Integer id) {
        int deviceNum = deviceService.count(new LambdaQueryWrapper<Device>().eq(Device::getProductId, id));
        int subDeviceNum = subDeviceService.count(new LambdaQueryWrapper<SubDevice>().eq(SubDevice::getProductId, id));

        if (deviceNum > 0 || subDeviceNum > 0){

            return AjaxResult.error("产品下存在设备或子设备，无法删除");

        }

        redisCache.deleteObject(RedisConstants.PRODUCT + id);
        return toAjax(productService.removeById(id));
    }

    @ApiOperation("分页查询")
    @PostMapping("/page")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult page(@RequestBody ProductVo param) {

        Page<Product> page = new Page<>(param.getPageNum(), param.getPageSize());

        Page<Product> pageData = productService.page(page, new LambdaQueryWrapper<Product>()
                .like(StringUtils.isNotEmpty(param.getProductName()), Product::getProductName, param.getProductName())
                .eq(StringUtils.isNotEmpty(param.getConnectionType()), Product::getConnectType, param.getConnectionType())
        );

        return AjaxResult.success(pageData);
    }

    @ApiOperation("id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult getById(@PathVariable Integer id){
        Product prod = productService.getById(id);
        return AjaxResult.success(prod);
    }

    @ApiOperation("查询全部")
    @GetMapping("/all")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult all() {
        return AjaxResult.success(productService.list());
    }

    @ApiOperation("统计产品启用禁用数量")
    @GetMapping("/count")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public AjaxResult count() {
        return AjaxResult.success(productService.countNumber());
    }
}
