package com.foodorder.logic;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.foodorder.contant.EventTag;
import com.foodorder.db.bean.Good;
import com.foodorder.runtime.event.EventManager;

/**
 * Created by guodong on 2016/9/19 17:12.
 */
public class CartManager {

    private volatile static CartManager instance;

    public SparseArray<Good> cartList;
    public SparseIntArray groupSelect;

    public boolean isPack = false; // 是否打包

    private CartManager() {
        cartList = new SparseArray<>();
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
        groupSelect.clear();
    }

    //添加商品
    public void add(Good item, boolean refreshGoodList) {

        int groupCount = groupSelect.get(item.getPosition());
        if (groupCount == 0) {
            groupSelect.append(item.getPosition(), 1);
        } else {
            groupSelect.append(item.getPosition(), ++groupCount);
        }

        Good temp = CartManager.ins().cartList.get(item.getId().intValue());
        if (temp == null) {
            item.setCount(1);
            CartManager.ins().cartList.append(item.getId().intValue(), item);
        } else {
            temp.setCount(temp.getCount() + 1);
        }
        EventManager.ins().sendEvent(EventTag.GOOD_LIST_REFRESH, 0, 0, refreshGoodList);
    }

    //移除商品
    public void remove(Good item, boolean refreshGoodList) {

        int groupCount = groupSelect.get(item.getPosition());
        if (groupCount == 1) {
            groupSelect.delete(item.getPosition());
        } else if (groupCount > 1) {
            groupSelect.append(item.getPosition(), --groupCount);
        }

        Good temp = CartManager.ins().cartList.get(item.getId().intValue());
        if (temp != null) {
            if (temp.getCount() < 2) {
                CartManager.ins().cartList.remove(item.getId().intValue());
            } else {
                item.setCount(item.getCount() - 1);
            }
        }
        EventManager.ins().sendEvent(EventTag.GOOD_LIST_REFRESH, 0, 0, refreshGoodList);
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

}
