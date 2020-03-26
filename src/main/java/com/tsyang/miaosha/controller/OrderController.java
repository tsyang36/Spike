package com.tsyang.miaosha.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.tsyang.miaosha.entity.Order;
import com.tsyang.miaosha.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * (Order)表控制层
 *
 * @author makejava
 * @since 2020-03-25 14:08:32
 */
@Slf4j
@RestController
@RequestMapping("order")
public class OrderController {
    /**
     * 服务对象
     */
    @Resource
    private OrderService orderService;


    RateLimiter rateLimiter = RateLimiter.create(10);
    /**
     * 乐观锁更新库存 + 令牌桶限流
     * @param sid
     * @return
     */
    @RequestMapping("/createOptimisticOrder/{sid}")
    @ResponseBody
    public String createOptimisticOrder(@PathVariable int sid) {
        // 阻塞式获取令牌
        //LOGGER.info("等待时间" + rateLimiter.acquire());
        // 非阻塞式获取令牌
//        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
//            log.warn("你被限流了，真不幸，直接返回失败");
//            return "购买失败，库存不足";
//        }
        int id;
        try {
            rateLimiter.acquire();
            id = orderService.createOptimisticOrder(sid);
            log.info("购买成功，剩余库存为: [{}]", id);
        } catch (Exception e) {
            log.error("购买失败：[{}]", e.getMessage());
            return "购买失败，库存不足";
        }
        return String.format("购买成功，剩余库存为：%d", id);
    }


    //每秒放行10个请求
    @RequestMapping("/createWrongOrder/{sid}")
    @ResponseBody
    public String createWrongOrder(@PathVariable int sid) {
//        log.info("购买物品编号sid=[{}]", sid);
//        int id = 0;
//        try {
//            id = orderService.createWrongOrder(sid);
//            log.info("创建订单id: [{}]", id);
//        } catch (Exception e) {
//            log.error("Exception", e);
//        }
//        return String.valueOf(id);

        int id;
        try {
            id = orderService.createOptimisticOrder(sid);
            log.info("购买成功，剩余库存为: [{}]", id);
        } catch (Exception e) {
//            log.error("购买失败：[{}]");
            return "购买失败，库存不足";
        }
        return String.format("购买成功，剩余库存为：%d", id);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public Order selectOne(Integer id) {
        return this.orderService.queryById(id);
    }

}