package com.tsyang.miaosha.service.impl;

import com.tsyang.miaosha.entity.Stock;
import com.tsyang.miaosha.dao.StockDao;
import com.tsyang.miaosha.service.StockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Stock)表服务实现类
 *
 * @author makejava
 * @since 2020-03-25 14:09:14
 */
@Service("stockService")
public class StockServiceImpl implements StockService {
    @Resource
    private StockDao stockDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Stock queryById(Integer id) {
        return this.stockDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Stock> queryAllByLimit(int offset, int limit) {
        return this.stockDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param stock 实例对象
     * @return 实例对象
     */
    @Override
    public Stock insert(Stock stock) {
        this.stockDao.insert(stock);
        return stock;
    }

    /**
     * 修改数据
     *
     * @param stock 实例对象
     * @return 实例对象
     */
    @Override
    public Integer update(Stock stock) {
        return this.stockDao.update(stock);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.stockDao.deleteById(id) > 0;
    }

    @Override
    public int updateStockByOptimistic(Stock stock) {
        return stockDao.updateStockByOptimistic(stock);
    }

    @Override
    public Stock getStockByIdForUpdate(int sid) {
        return stockDao.getStockByIdForUpdate(sid);
    }
}