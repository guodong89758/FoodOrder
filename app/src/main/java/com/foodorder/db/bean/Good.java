package com.foodorder.db.bean;

import com.foodorder.db.AttributeDao;
import com.foodorder.db.DaoSession;
import com.foodorder.db.FormulaDao;
import com.foodorder.db.GoodDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guodong on 2016/9/19 16:22.
 */
@Entity
public class Good implements Cloneable {

    @Id(autoincrement = true)
    private Long id;
    @Property
    private String id_category;
    @Property
    private String zh_category_name;
    @Property
    private String fr_category_name;
    @Property
    private int position;
    @Property
    private String search_num;
    @Property
    private String id_product;
    @Property
    private String reference;
    @Property
    private String zh_name;
    @Property
    private String fr_name;
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
    @Property
    private boolean isFormula;
    @Transient
    private int count;
    @ToMany(referencedJoinProperty = "id_product")
    private List<Attribute> attributeList;
    @ToMany(referencedJoinProperty = "id_product")
    private List<Formula> formulaList;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1249866486)
    private transient GoodDao myDao;

    @Generated(hash = 451851001)
    public Good(Long id, String id_category, String zh_category_name,
                String fr_category_name, int position, String search_num,
                String id_product, String reference, String zh_name, String fr_name,
                int quantity, String unit, double price, boolean reducable,
                int max_attributes_choose, String image_url, boolean isFormula) {
        this.id = id;
        this.id_category = id_category;
        this.zh_category_name = zh_category_name;
        this.fr_category_name = fr_category_name;
        this.position = position;
        this.search_num = search_num;
        this.id_product = id_product;
        this.reference = reference;
        this.zh_name = zh_name;
        this.fr_name = fr_name;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.reducable = reducable;
        this.max_attributes_choose = max_attributes_choose;
        this.image_url = image_url;
        this.isFormula = isFormula;
    }

    @Generated(hash = 2016981037)
    public Good() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getId_category() {
        return this.id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public String getZh_category_name() {
        return this.zh_category_name;
    }

    public void setZh_category_name(String zh_category_name) {
        this.zh_category_name = zh_category_name;
    }

    public String getFr_category_name() {
        return this.fr_category_name;
    }

    public void setFr_category_name(String fr_category_name) {
        this.fr_category_name = fr_category_name;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getSearch_num() {
        return this.search_num;
    }

    public void setSearch_num(String search_num) {
        this.search_num = search_num;
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

    public boolean getIsFormula() {
        return this.isFormula;
    }

    public void setIsFormula(boolean isFormula) {
        this.isFormula = isFormula;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
//    @Generated(hash = 1481748649)
    @Keep
    public List<Attribute> getAttributeList() {
        if (attributeList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AttributeDao targetDao = daoSession.getAttributeDao();
            List<Attribute> attributeListNew = targetDao._queryGood_AttributeList(id_product);
            synchronized (this) {
                if (attributeList == null) {
                    attributeList = attributeListNew;
                }
            }
        }
        return attributeList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 577828666)
    public synchronized void resetAttributeList() {
        attributeList = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
//    @Generated(hash = 524137696)
    @Keep
    public List<Formula> getFormulaList() {
        if (formulaList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FormulaDao targetDao = daoSession.getFormulaDao();
            List<Formula> formulaListNew = targetDao._queryGood_FormulaList(id_product);
            synchronized (this) {
                if (formulaList == null) {
                    formulaList = formulaListNew;
                }
            }
        }
        return formulaList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1127442251)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGoodDao() : null;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    public void setFormulaList(List<Formula> formulaList) {
        this.formulaList = formulaList;
    }

    @Override
    public Good clone() {
        Good o = null;
        try {
            o = (Good) super.clone();
            List<Formula> formulaData = new ArrayList<>();
            List<Attribute> attrData = new ArrayList<>();
            if (formulaList != null && formulaList.size() > 0) {
                for (int i = 0; i < formulaList.size(); i++) {
                    Formula formula = formulaList.get(i);
                    formulaData.add(formula.clone());
                }
            }
            if (attributeList != null && attributeList.size() > 0) {
                for (int i = 0; i < attributeList.size(); i++) {
                    Attribute attribute = attributeList.get(i);
                    attrData.add(attribute.clone());
                }
            }
            o.setAttributeList(attrData);
            o.setFormulaList(formulaData);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    public Good(JSONObject json) {
        if (json == null) {
            return;
        }

        this.id_product = json.optString("id_product");
        this.reference = json.optString("product_reference");
        this.fr_name = json.optString("product_name");
        JSONObject product_zh = json.optJSONObject("zh");
        if (product_zh != null) {
            this.zh_name = product_zh.optString("name");
        }
        this.count = json.optInt("product_quantity", 0);
        this.price = json.optDouble("product_price", 0);
        this.image_url = json.optString("image");
//        this.id_product = json.optString("tax_rate");
//        this.id_product = json.optString("product_price_with_tax");
        JSONArray formulaArray = json.optJSONArray("Formula_Items");
        List<Formula> formulaData = new ArrayList<>();
        if (formulaArray != null && formulaArray.length() > 0) {
            for (int i = 0; i < formulaArray.length(); i++) {
                JSONObject formulaJson = formulaArray.optJSONObject(i);
                int count = formulaJson.optInt("quantity", 0);
                String product_name_fr = formulaJson.optString("product_name");
                JSONObject zh = formulaJson.optJSONObject("zh");
                String product_name_zh = "";
                if (zh != null) {
                    product_name_zh = zh.optString("name");
                }
                Formula formula = new Formula();
                formula.setFr_name(product_name_fr);
                formula.setZh_name(product_name_zh);
                formula.setCount(count);
                formulaData.add(formula);
            }
        }
        this.formulaList = formulaData;
//        JSONArray attrArray = json.optJSONArray("Attributes");
//        List<Attribute> attrData = new ArrayList<>();
//        if (attrArray != null && attrArray.length() > 0) {
//            for (int i = 0; i < attrArray.length(); i++) {
//                JSONObject attrJson = attrArray.optJSONObject(i);
//                int count = attrJson.optInt("quantity", 0);
//                Attribute attr = new Attribute();
//                attr.setFr_name("规格" + i);
//                attr.setZh_name("规格" + i);
//                attr.setCount(count);
//                attrData.add(attr);
//            }
//        }
//        this.attributeList = attrData;

    }

}
