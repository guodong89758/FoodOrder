package com.foodorder.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

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
    private String name;
    @Property
    private String value;
    @Generated(hash = 2053077576)
    public Attribute(long id, String id_product, String id_product_attribute,
            String name, String value) {
        this.id = id;
        this.id_product = id_product;
        this.id_product_attribute = id_product_attribute;
        this.name = name;
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
