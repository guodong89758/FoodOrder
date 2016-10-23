package com.foodorder.server.api;

import com.foodorder.server.HttpMethod;
import com.foodorder.server.NetworkInterface;
import com.foodorder.server.callback.ResponseCallback;
import com.lzy.okhttputils.model.HttpParams;

/**
 * Created by guodong on 16/9/20.
 */
public class API_Food {

    private static volatile API_Food instance = null;

    public static final String GET_MENU = "get_menu_json";//获取菜单json数据
    public static final String LOGIN = "auth_json";//登陆
    public static final String GET_ORDERS = "get_orders_json";//获取订单数据
    public static final String GET_ORDER_INFO = "get_order_json";//获取订单详情数据
    public static final String POST_ORDER = "post_order_json";//提交订单
    public static final String PRINT_ORDER = "print_order";//打印订单
    public static final String REMIND_ORDER = "remind_order";//催单


    private API_Food() {

    }

    public static API_Food ins() {
        if (instance == null) {
            synchronized (API_Food.class) {
                if (instance == null) {
                    instance = new API_Food();
                }
            }
        }
        return instance;
    }

    /**
     * 获取菜单数据
     *
     * @param tag
     * @param callback
     */
    public void getGoodMenu(String tag, ResponseCallback callback) {
        HttpParams params = new HttpParams();
        NetworkInterface.ins().connected(HttpMethod.GET, GET_MENU, tag, params, callback);
    }

    /**
     * 登陆
     *
     * @param tag
     * @param name
     * @param password
     * @param callback
     */
    public void login(String tag, String name, String password, ResponseCallback callback) {
        HttpParams params = new HttpParams();
        params.put("login", name);
        params.put("pwd", password);
        NetworkInterface.ins().connected(HttpMethod.GET, LOGIN, tag, params, callback);
    }

    /**
     * 获取订单列表
     *
     * @param tag
     * @param callback
     */
    public void getOrderList(String tag, ResponseCallback callback) {
        HttpParams params = new HttpParams();
        NetworkInterface.ins().connected(HttpMethod.GET, GET_ORDERS, tag, params, callback);
    }

    /**
     * 获取订单详情
     *
     * @param tag
     * @param id_order
     * @param callback
     */
    public void getOrderInfo(String tag, String id_order, ResponseCallback callback) {
        HttpParams params = new HttpParams();
        params.put("id_order", id_order);
        NetworkInterface.ins().connected(HttpMethod.GET, GET_ORDER_INFO, tag, params, callback);
    }

    /**
     * 下单（加菜）
     *
     * @param tag
     * @param orderjson
     * @param callback
     */
    public void orderGood(String tag, String orderjson, ResponseCallback callback) {
        HttpParams params = new HttpParams();
        params.put("order_json", orderjson);
        NetworkInterface.ins().connected(HttpMethod.POST, POST_ORDER, tag, params, callback);
    }

    /**
     * 打印订单
     *
     * @param tag
     * @param id_order
     * @param callback
     */
    public void printOrder(String tag, String id_order, ResponseCallback callback) {
        HttpParams params = new HttpParams();
        params.put("id_order", id_order);
        NetworkInterface.ins().connected(HttpMethod.GET, PRINT_ORDER, tag, params, callback);
    }

    /**
     * 催单
     *
     * @param tag
     * @param id_order
     * @param callback
     */
    public void remindOrder(String tag, String id_order, ResponseCallback callback) {
        HttpParams params = new HttpParams();
        params.put("id_order", id_order);
        NetworkInterface.ins().connected(HttpMethod.GET, REMIND_ORDER, tag, params, callback);
    }

}
