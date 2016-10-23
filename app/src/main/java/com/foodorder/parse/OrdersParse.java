package com.foodorder.parse;

import com.foodorder.db.OrderDao;
import com.foodorder.db.bean.Order;
import com.foodorder.runtime.RT;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by guodong on 16/10/5.
 */
public class OrdersParse {

    public static void parseJson(JSONObject data) {
        if (data == null) {
            return;
        }
//        JSONObject data = json.optJSONObject("data");
//        if (data == null) {
//            return;
//        }

        JSONArray orderArray = data.optJSONArray("Orders");
        if (orderArray != null && orderArray.length() > 0) {
//            RT.ins().getDaoSession().getOrderDao().deleteAll();
            for (int i = 0; i < orderArray.length(); i++) {
                JSONObject orderJson = orderArray.optJSONObject(i);
                String id_order = orderJson.optString("id_order");
                int persons = orderJson.optInt("persons", 0);
                String number = orderJson.optString("number");
                String id_order_type = orderJson.optString("id_order_type");
                String time = orderJson.optString("time");
                double total = orderJson.optDouble("total", 0);
                String type = orderJson.optString("type");

                Order order = new Order();
                order.setId_order(id_order);
                order.setPersons(persons);
                order.setNumber(number);
                order.setId_order_type(id_order_type);
                order.setTime(time);
                order.setTotal(total);
                order.setType(type);

                synchronized (OrdersParse.class) {
                    Order findOrder = RT.ins().getDaoSession().getOrderDao().queryBuilder().where(OrderDao.Properties.Id_order.eq(id_order)).build().unique();
                    if (findOrder != null) {
                        RT.ins().getDaoSession().getOrderDao().deleteByKey(findOrder.getId());
                    }
                    RT.ins().getDaoSession().getOrderDao().insertOrReplace(order);
                }
            }
        }
    }
}
