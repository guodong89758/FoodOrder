package com.foodorder.logic;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.foodorder.contant.EventTag;
import com.foodorder.db.bean.Attribute;
import com.foodorder.db.bean.Formula;
import com.foodorder.db.bean.Good;
import com.foodorder.entry.OrderGood;
import com.foodorder.runtime.event.EventManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.foodorder.contant.EventTag.GOOD_LIST_REFRESH;

/**
 * Created by guodong on 2016/9/19 17:12.
 */
public class CartManager {

    private volatile static CartManager instance;

    public SparseArray<Good> cartList;
    public List<Good> cartData;
    public SparseIntArray groupSelect;

    public boolean isPack = false; // 是否打包

    private CartManager() {
        cartList = new SparseArray<>();
        cartData = new ArrayList<>();
        groupSelect = new SparseIntArray();
    }

    public static CartManager ins() {
        if (instance == null) {
            synchronized (CartManager.class) {
                if (instance == null) {
                    instance = new CartManager();
                }
            }
        }
        return instance;
    }

    public void clear() {
        cartList.clear();
        cartData.clear();
        groupSelect.clear();
        isPack = false;
    }

    //添加商品
    public void add(Good item, boolean refreshGoodList) {

        int groupCount = groupSelect.get(item.getPosition());
        if (groupCount == 0) {
            groupSelect.append(item.getPosition(), 1);
        } else {
            groupSelect.append(item.getPosition(), ++groupCount);
        }

        Good temp = cartList.get(item.getId().intValue());
        if (temp == null) {
            item.setCount(1);
            cartList.append(item.getId().intValue(), item);
            if ((item.getFormulaList() != null && item.getFormulaList().size() > 0) || (item.getAttributeList() != null && item.getAttributeList().size() > 0)) {
                Good newGood = item.clone();
                cartData.add(newGood);
                clearTempData(item);
            } else {
                cartData.add(item);
            }
        } else {
            if ((item.getFormulaList() != null && item.getFormulaList().size() > 0) || (item.getAttributeList() != null && item.getAttributeList().size() > 0)) {
                Good newGood = item.clone();
                newGood.setCount(1);
                cartData.add(newGood);
                clearTempData(item);
            }
            temp.setCount(temp.getCount() + 1);
        }
        EventManager.ins().sendEvent(GOOD_LIST_REFRESH, 0, 0, refreshGoodList);
        EventManager.ins().sendEvent(EventTag.GOOD_SEARCH_LIST_REFRESH, 0, 0, refreshGoodList);
    }

    //移除商品
    public void remove(Good item, boolean refreshGoodList) {

        int groupCount = groupSelect.get(item.getPosition());
        if (groupCount == 1) {
            groupSelect.delete(item.getPosition());
        } else if (groupCount > 1) {
            groupSelect.append(item.getPosition(), --groupCount);
        }

        Good temp = cartList.get(item.getId().intValue());
        if (temp != null) {
            if (temp.getCount() < 2) {
                cartList.remove(item.getId().intValue());
                cartData.remove(item);
            } else {
                if ((item.getFormulaList() != null && item.getFormulaList().size() > 0) || (item.getAttributeList() != null && item.getAttributeList().size() > 0)) {
                    cartData.remove(item);
                }
                temp.setCount(temp.getCount() - 1);
            }
        }
        EventManager.ins().sendEvent(GOOD_LIST_REFRESH, 0, 0, refreshGoodList);
        EventManager.ins().sendEvent(EventTag.GOOD_SEARCH_LIST_REFRESH, 0, 0, refreshGoodList);
    }

    //添加商品
    public void cartAdd(Good item, boolean refreshGoodList) {

        int groupCount = groupSelect.get(item.getPosition());
        if (groupCount == 0) {
            groupSelect.append(item.getPosition(), 1);
        } else {
            groupSelect.append(item.getPosition(), ++groupCount);
        }

        Good temp = cartList.get(item.getId().intValue());
        if (temp == null) {
            item.setCount(1);
            cartList.append(item.getId().intValue(), item);
            cartData.add(item);
        } else {
            if ((item.getFormulaList() != null && item.getFormulaList().size() > 0) || (item.getAttributeList() != null && item.getAttributeList().size() > 0)) {
                item.setCount(item.getCount() + 1);
            }
            temp.setCount(temp.getCount() + 1);
        }
        EventManager.ins().sendEvent(GOOD_LIST_REFRESH, 0, 0, refreshGoodList);
        EventManager.ins().sendEvent(EventTag.GOOD_SEARCH_LIST_REFRESH, 0, 0, refreshGoodList);
    }

    //移除商品
    public void cartRemove(Good item, boolean refreshGoodList) {

        int groupCount = groupSelect.get(item.getPosition());
        if (groupCount == 1) {
            groupSelect.delete(item.getPosition());
        } else if (groupCount > 1) {
            groupSelect.append(item.getPosition(), --groupCount);
        }

        Good temp = cartList.get(item.getId().intValue());
        if (temp != null) {
            if (temp.getCount() < 2) {
                cartList.remove(item.getId().intValue());
                cartData.remove(item);
            } else {
                if ((item.getFormulaList() != null && item.getFormulaList().size() > 0) || (item.getAttributeList() != null && item.getAttributeList().size() > 0)) {
                    if (item.getCount() < 2) {
                        cartData.remove(item);
                    }
                    item.setCount(item.getCount() - 1);
                }
                temp.setCount(temp.getCount() - 1);
            }
        }
        EventManager.ins().sendEvent(GOOD_LIST_REFRESH, 0, 0, refreshGoodList);
        EventManager.ins().sendEvent(EventTag.GOOD_SEARCH_LIST_REFRESH, 0, 0, refreshGoodList);
    }

    //根据商品id获取当前商品的采购数量
    public int getSelectedItemCountById(int id) {
        Good temp = CartManager.ins().cartList.get(id);
        if (temp == null) {
            return 0;
        }
        return temp.getCount();
    }

    //根据类别Id获取属于当前类别的数量
    public int getSelectedGroupCountByTypeId(int typeId) {
        return groupSelect.get(typeId);
    }

    public void clearTempData(Good good) {
        if (good.getAttributeList() != null && good.getAttributeList().size() > 0) {
            for (int j = 0; j < good.getAttributeList().size(); j++) {
                Attribute attribute = good.getAttributeList().get(j);
                if (attribute.getCount() == 0) {
                    continue;
                }
                attribute.setCount(0);
                attribute.setSel_count(0);
            }
        }
        if (good.getFormulaList() != null && good.getFormulaList().size() > 0) {
            for (int j = 0; j < good.getFormulaList().size(); j++) {
                Formula formula = good.getFormulaList().get(j);
                if (formula.getCount() == 0) {
                    continue;
                }
                formula.setCount(0);
                formula.setSel_count(0);
            }
        }

    }

    public String getOrderGoodJson(boolean isPack, String id_order, String number, String persons) {
        if (cartData == null || cartData.size() < 0) {
            return "";
        }

        OrderGood orderGood = new OrderGood();
        orderGood.setId_order(id_order);
        orderGood.setPack(isPack);
        orderGood.setNumber(number);
        orderGood.setPersons(persons);
        List<OrderGood.Product> products = new ArrayList<>();
        for (int i = 0; i < cartData.size(); i++) {
            Good good = cartData.get(i);
            OrderGood.Product product = new OrderGood.Product();
            product.setId_product(good.getId_product());
            product.setCount(good.getCount());
//            good.setCount(0);
            if (good.getAttributeList() != null && good.getAttributeList().size() > 0) {
                List<OrderGood.Attribute> attributes = new ArrayList<>();
                for (int j = 0; j < good.getAttributeList().size(); j++) {
                    Attribute attribute = good.getAttributeList().get(j);
                    if (attribute.getCount() == 0) {
                        continue;
                    }
                    OrderGood.Attribute attr = new OrderGood.Attribute();
                    attr.setId_product_attribute(attribute.getId_product_attribute());
                    attr.setCount(attribute.getCount());
                    attributes.add(attr);
//                    attribute.setCount(0);
//                    attribute.setSel_count(0);
                }
                product.setAttributes(attributes);
            }
            if (good.getFormulaList() != null && good.getFormulaList().size() > 0) {
                List<OrderGood.Formula> formulas = new ArrayList<>();
                for (int j = 0; j < good.getFormulaList().size(); j++) {
                    Formula formula = good.getFormulaList().get(j);
                    if (formula.getCount() == 0) {
                        continue;
                    }
                    OrderGood.Formula form = new OrderGood.Formula();
                    form.setId_product_formula_item(formula.getId_product_formula_item());
                    form.setId_product_item(formula.getId_product_item());
                    form.setCount(formula.getCount());
                    formulas.add(form);
//                    formula.setCount(0);
//                    formula.setSel_count(0);
                }
                product.setFormuals(formulas);
            }
            products.add(product);

        }
        orderGood.setProducts(products);
        return new Gson().toJson(orderGood);
    }

}
