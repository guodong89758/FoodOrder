package com.foodorder.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by guodong on 2016/9/19 16:33.
 */
@Entity
public class Attribute {
    @Id(autoincrement = true)
    private Long id;
    @Property
    private String id_product;
    @Property
    private String id_product_attribute;
    @Property
    private String zh_name;
    @Property
    private String fr_name;
    @Property
    private String value;
    @Property
    private int max_choose;
    @Transient
    private int count;
    @Transient
    private int sel_count;


    @Generated(hash = 1878088058)
    public Attribute(Long id, String id_product, String id_product_attribute,
                     String zh_name, String fr_name, String value, int max_choose) {
        this.id = id;
        this.id_product = id_product;
        this.id_product_attribute = id_product_attribute;
        this.zh_name = zh_name;
        this.fr_name = fr_name;
        this.value = value;
        this.max_choose = max_choose;
    }

    @Generated(hash = 959577406)
    public Attribute() {
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getId_product() {
        return this.id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getId_product_attribute() {
        return this.id_product_attribute;
    }

    public void setId_product_attribute(String id_product_attribute) {
        this.id_product_attribute = id_product_attribute;
    }

    public String getZh_name() {
        return this.zh_name;
    }

    public void setZh_name(String zh_name) {
        this.zh_name = zh_name;
    }

    public String getFr_name() {
        return this.fr_name;
    }

    public void setFr_name(String fr_name) {
        this.fr_name = fr_name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getMax_choose() {
        return this.max_choose;
    }

    public void setMax_choose(int max_choose) {
        this.max_choose = max_choose;
    }

    public int getSel_count() {
        return sel_count;
    }

    public void setSel_count(int sel_count) {
        this.sel_count = sel_count;
    }
}
