package com.foodorder.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by guodong on 2016/9/19 16:33.
 */
@Entity
public class Attribute {
    @Id(autoincrement = true)
    private long id;
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
    @Transient
    private int count;
    @Generated(hash = 1652412708)
    public Attribute(long id, String id_product, String id_product_attribute,
            String zh_name, String fr_name, String value) {
        this.id = id;
        this.id_product = id_product;
        this.id_product_attribute = id_product_attribute;
        this.zh_name = zh_name;
        this.fr_name = fr_name;
        this.value = value;
    }
    @Generated(hash = 959577406)
    public Attribute() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
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
}
