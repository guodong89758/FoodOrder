package com.foodorder.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.foodorder.db.DaoSession;
import com.foodorder.db.FormulaDao;
import com.foodorder.db.AttributeDao;
import com.foodorder.db.GoodDao;

/**
 * Created by guodong on 2016/9/19 16:22.
 */
@Entity
public class Good {

    @Id(autoincrement = true)
    private long id;
    @Property
    private String id_product;
    @Property
    private String reference;
    @Property
    private String name;
    @Property
    private int quantity;
    @Property
    private String unit;
    @Property
    private double price;
    @Property
    private boolean reducable;
    @Property
    private int max_attributes_choose;
    @Property
    private String image_url;
    @Transient
    private int count;
    @ToMany(referencedJoinProperty = "id_product")
    private List<Attribute> attributeList;
    @ToMany(referencedJoinProperty = "id_product")
    private List<Formula> formulaList;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1249866486)
    private transient GoodDao myDao;
    @Generated(hash = 1085566672)
    public Good(long id, String id_product, String reference, String name,
            int quantity, String unit, double price, boolean reducable,
            int max_attributes_choose, String image_url) {
        this.id = id;
        this.id_product = id_product;
        this.reference = reference;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.reducable = reducable;
        this.max_attributes_choose = max_attributes_choose;
        this.image_url = image_url;
    }
    @Generated(hash = 2016981037)
    public Good() {
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
    public String getReference() {
        return this.reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getQuantity() {
        return this.quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public boolean getReducable() {
        return this.reducable;
    }
    public void setReducable(boolean reducable) {
        this.reducable = reducable;
    }
    public int getMax_attributes_choose() {
        return this.max_attributes_choose;
    }
    public void setMax_attributes_choose(int max_attributes_choose) {
        this.max_attributes_choose = max_attributes_choose;
    }
    public String getImage_url() {
        return this.image_url;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1481748649)
    public List<Attribute> getAttributeList() {
        if (attributeList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AttributeDao targetDao = daoSession.getAttributeDao();
            List<Attribute> attributeListNew = targetDao._queryGood_AttributeList(id);
            synchronized (this) {
                if(attributeList == null) {
                    attributeList = attributeListNew;
                }
            }
        }
        return attributeList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 577828666)
    public synchronized void resetAttributeList() {
        attributeList = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 524137696)
    public List<Formula> getFormulaList() {
        if (formulaList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FormulaDao targetDao = daoSession.getFormulaDao();
            List<Formula> formulaListNew = targetDao._queryGood_FormulaList(id);
            synchronized (this) {
                if(formulaList == null) {
                    formulaList = formulaListNew;
                }
            }
        }
        return formulaList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1578763732)
    public synchronized void resetFormulaList() {
        formulaList = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1127442251)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGoodDao() : null;
    }
}
