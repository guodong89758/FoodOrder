package com.foodorder.logic;

import android.util.SparseArray;

import com.foodorder.db.bean.Good;

/**
 * Created by guodong on 2016/9/19 17:12.
 */
public class CartManager {

    private volatile static CartManager instance;

    public SparseArray<Good> cartList;

    public boolean isPack = false; // 是否打包

    private CartManager() {
        cartList = new SparseArray<>();
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
    }

    public void add(int key, Good good) {
        cartList.put(key, good);
    }

    public void remove() {

    }


}
