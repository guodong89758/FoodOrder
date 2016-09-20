package com.foodorder.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by guodong on 2016/9/19 16:11.
 */
@Entity
public class GoodType {
    @Id(autoincrement = true)
    private long id;
    @Property
    private String id_category;
    @Property
    private String zh_name;
    @Property
    private String fr_name;
    @Property
    private int position;
    @Property
    private boolean active;
    @Transient
    private int count;
    @Generated(hash = 1847776811)
    public GoodType(long id, String id_category, String zh_name, String fr_name,
            int position, boolean active) {
        this.id = id;
        this.id_category = id_category;
        this.zh_name = zh_name;
        this.fr_name = fr_name;
        this.position = position;
        this.active = active;
    }
    @Generated(hash = 386185201)
    public GoodType() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getId_category() {
        return this.id_category;
    }
    public void setId_category(String id_category) {
        this.id_category = id_category;
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
    public int getPosition() {
        return this.position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public boolean getActive() {
        return this.active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

}
