package com.foodorder.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
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
    private String zh_type_name;
    @Property
    private String fr_type_name;
    @Property
    private int max_choose;
    @Property
    private String id_product_formula_item;
    @Property
    private String id_product_item;
    @Property
    private int position;
    @Property
    private String zh_name;
    @Property
    private String fr_name;
    @Property
    private String image_url;
    @Transient
    private int count;
    @Generated(hash = 1747633380)
    public Formula(long id, String id_product, String id_product_formula,
            String zh_type_name, String fr_type_name, int max_choose,
            String id_product_formula_item, String id_product_item, int position,
            String zh_name, String fr_name, String image_url) {
        this.id = id;
        this.id_product = id_product;
        this.id_product_formula = id_product_formula;
        this.zh_type_name = zh_type_name;
        this.fr_type_name = fr_type_name;
        this.max_choose = max_choose;
        this.id_product_formula_item = id_product_formula_item;
        this.id_product_item = id_product_item;
        this.position = position;
        this.zh_name = zh_name;
        this.fr_name = fr_name;
        this.image_url = image_url;
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
    public String getZh_type_name() {
        return this.zh_type_name;
    }
    public void setZh_type_name(String zh_type_name) {
        this.zh_type_name = zh_type_name;
    }
    public String getFr_type_name() {
        return this.fr_type_name;
    }
    public void setFr_type_name(String fr_type_name) {
        this.fr_type_name = fr_type_name;
    }
    public int getMax_choose() {
        return this.max_choose;
    }
    public void setMax_choose(int max_choose) {
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
    public String getImage_url() {
        return this.image_url;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


}
