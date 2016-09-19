package com.foodorder.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by guodong on 2016/9/19 16:48.
 */
@Entity
public class Formula {
    @Id(autoincrement = true)
    private long id;
    @Property
    private String id_product;
    @Property
    private String id_product_formula;
    @Property
    private String name;
    @Property
    private String max_choose;
    @Property
    private String id_product_formula_item;
    @Property
    private String id_product_item;
    @Property
    private int position;
    @Generated(hash = 1469881166)
    public Formula(long id, String id_product, String id_product_formula,
            String name, String max_choose, String id_product_formula_item,
            String id_product_item, int position) {
        this.id = id;
        this.id_product = id_product;
        this.id_product_formula = id_product_formula;
        this.name = name;
        this.max_choose = max_choose;
        this.id_product_formula_item = id_product_formula_item;
        this.id_product_item = id_product_item;
        this.position = position;
    }
    @Generated(hash = 775514140)
    public Formula() {
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
    public String getId_product_formula() {
        return this.id_product_formula;
    }
    public void setId_product_formula(String id_product_formula) {
        this.id_product_formula = id_product_formula;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMax_choose() {
        return this.max_choose;
    }
    public void setMax_choose(String max_choose) {
        this.max_choose = max_choose;
    }
    public String getId_product_formula_item() {
        return this.id_product_formula_item;
    }
    public void setId_product_formula_item(String id_product_formula_item) {
        this.id_product_formula_item = id_product_formula_item;
    }
    public String getId_product_item() {
        return this.id_product_item;
    }
    public void setId_product_item(String id_product_item) {
        this.id_product_item = id_product_item;
    }
    public int getPosition() {
        return this.position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

}
