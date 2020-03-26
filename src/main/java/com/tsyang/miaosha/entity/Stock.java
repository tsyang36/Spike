package com.tsyang.miaosha.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (Stock)实体类
 *
 * @author makejava
 * @since 2020-03-25 14:09:14
 */
@Data
public class Stock implements Serializable {

    private static final long serialVersionUID = -4694064110125433532L;
    private Integer id;
    /**
    * 名称
    */
    private String name;
    /**
    * 库存
    */
    private Integer count;
    /**
    * 已售
    */
    private Integer sale;
    /**
    * 乐观锁，版本号
    */
    private Integer version;


}