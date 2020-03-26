package com.tsyang.miaosha.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (Order)实体类
 *
 * @author makejava
 * @since 2020-03-25 14:08:30
 */
@Data
public class Order implements Serializable {

    private static final long serialVersionUID = 5155479201019054327L;
    private Integer id;
    /**
    * 库存ID
    */
    private Integer sid;
    /**
    * 商品名称
    */
    private String name;
    /**
    * 创建时间
    */
    private Date createTime;

}