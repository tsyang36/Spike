package com.tsyang.miaosha.service.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.tsyang.miaosha.entity.Order;
import com.tsyang.miaosha.dao.OrderDao;
import com.tsyang.miaosha.entity.Stock;
import com.tsyang.miaosha.service.OrderService;
import com.tsyang.miaosha.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Order)表服务实现类
 *
 * @author makejava
 * @since 2020-03-25 14:08:32
 */
@Service("orderService")
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;
    @Autowired
    private StockService stockService;


    @Override
    public int createWrongOrder(int sid) {
        //校验库存
        Stock stock = checkStock(sid);
        //扣库存
        saleStock(stock);
        //创建订单
        int id = createOrder(stock);
        return id;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public int createOptimisticOrder(int sid) {
        //校验库存(悲观锁for update)
        Stock stock = checkStockForUpdate(sid);
        //更新库存
        saleStock(stock);


//        //校验库存
//        Stock stock = checkStock(sid);
        //乐观锁更新库存
//       saleStockOptimistic(stock);
        //创建订单
        int id = createOrder(stock);

        return stock.getCount() - stock.getSale();

    }
    private Stock checkStockForUpdate(int sid) {
        Stock stock = stockService.getStockByIdForUpdate(sid);
        if (stock.getSale().equals(stock.getCount())) {
            throw new RuntimeException("库存不足");
        }
        return stock;
    }
    private void saleStockOptimistic(Stock stock) {
//        log.info("查询数据库，尝试更新库存");
        int count = stockService.updateStockByOptimistic(stock);
        if (count == 0){
//            log.error("并发更新库存失败，version不匹配");
            throw new RuntimeException("并发更新库存失败，version不匹配") ;
//            return false;
        }
//        return true;
    }
    private Stock checkStock(int sid) {
        Stock stock = stockService.queryById(sid);
        if (stock.getSale()>=stock.getCount()) {
            log.error("库存不足");
            throw new RuntimeException("库存不足");
        }
        return stock;
    }
    private int saleStock(Stock stock) {
        stock.setSale(stock.getSale() + 1);
        return stockService.update(stock);
    }

    private int createOrder(Stock stock) {
//        log.info("购买成功");
        Order order = new Order();
        order.setSid(stock.getId());
        order.setName(stock.getName());
        int id = orderDao.insert(order);
        return id;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Order queryById(Integer id) {
        return this.orderDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Order> queryAllByLimit(int offset, int limit) {
        return this.orderDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param order 实例对象
     * @return 实例对象
     */
    @Override
    public Order insert(Order order) {
        this.orderDao.insert(order);
        return order;
    }

    /**
     * 修改数据
     *
     * @param order 实例对象
     * @return 实例对象
     */
    @Override
    public Order update(Order order) {
        this.orderDao.update(order);
        return this.queryById(order.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.orderDao.deleteById(id) > 0;
    }


}