package com.foodorder.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by guodong on 16/9/19.
 */
@Entity
public class Order {
    @Id(autoincrement = true)
    private Long id;
    @Property
    private String id_order;
    @Property
    private int persons;
    @Property
    private String number;
    @Property
    private String id_order_type;
    @Property
    private String time;
    @Property
    private double total;
    @Property
    private String type;

    @Generated(hash = 216559327)
    public Order(Long id, String id_order, int persons, String number,
                 String id_order_type, String time, double total, String type) {
        this.id = id;
        this.id_order = id_order;
        this.persons = persons;
        this.number = number;
        this.id_order_type = id_order_type;
        this.time = time;
        this.total = total;
        this.type = type;
    }

    @Generated(hash = 1105174599)
    public Order() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getId_order() {
        return this.id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public int getPersons() {
        return this.persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId_order_type() {
        return this.id_order_type;
    }

    public void setId_order_type(String id_order_type) {
        this.id_order_type = id_order_type;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTotal() {
        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
