package com.foodorder.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by guodong on 16/9/19.
 */
@Entity
public class Order {
    @Id(autoincrement = true)
    private long id;

}
