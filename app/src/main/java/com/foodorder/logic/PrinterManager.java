package com.foodorder.logic;

import android.content.Context;
import android.view.View;

import com.foodorder.contant.AppKey;
import com.foodorder.contant.EventTag;
import com.foodorder.db.bean.Order;
import com.foodorder.dialog.OrderActionDialog;
import com.foodorder.dialog.PrinterDialog;
import com.foodorder.entry.Printer;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.server.api.API_Food;
import com.foodorder.server.callback.JsonResponseCallback;
import com.foodorder.util.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guodong on 2017/5/13.
 */

public class PrinterManager {

    private volatile static PrinterManager instance;

    private PrinterManager() {

    }

    public static PrinterManager ins() {
        if (instance == null) {
            synchronized (PrinterManager.class) {
                if (instance == null) {
                    instance = new PrinterManager();
                }
            }
        }
        return instance;
    }

    public void remindOrder(String order_id) {
        API_Food.ins().remindOrder(AppKey.HTTP_TAG, order_id, new JsonResponseCallback() {
            @Override
            public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
                if (errcode == 200) {
                    EventManager.ins().sendEvent(EventTag.ORDER_LIST_REFRESH, 0, 0, null);
                }
                ToastUtil.showToast(errmsg);
                return false;
            }
        });
    }

    public void printOrder(String order_id) {
        API_Food.ins().printOrder(AppKey.HTTP_TAG, order_id, new JsonResponseCallback() {
            @Override
            public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
                ToastUtil.showToast(errmsg);
                return false;
            }
        });
    }

    public void showActionDialog(final Context context, Order order) {
        if (order == null) {
            return;
        }
        OrderActionDialog dialog = new OrderActionDialog(context, order);
        dialog.setButton1(new OrderActionDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, final OrderActionDialog dialog, Order order) {
                dialog.dismiss();
                showPrinterDialog(context, AppKey.PRINTER_CUIDAN, order.getId_order());
//                ToastUtil.showToast(getResources().getString(R.string.order_action_1));

            }
        });
        dialog.setButton2(new OrderActionDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, OrderActionDialog dialog, Order order) {
                dialog.dismiss();
                showPrinterDialog(context, AppKey.PRINTER_DAYIN, order.getId_order());
//                ToastUtil.showToast(getResources().getString(R.string.order_action_2));

            }
        });
        dialog.show();
    }

    public void showPrinterDialog(Context context, int fromType, final String order_id) {
        List<Printer> printerList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Printer printer = new Printer();
            printer.setId(String.valueOf(i + 1));
            printerList.add(printer);
        }
        PrinterDialog printerDialog = new PrinterDialog(context, fromType, printerList);
        printerDialog.show();
        printerDialog.setOnCheckListener(new PrinterDialog.OnCheckListener() {
            @Override
            public void onCheck(int type, String printer) {
                ToastUtil.showBottomToast(printer);
                if (type == AppKey.PRINTER_CUIDAN) {
                    remindOrder(order_id);
                } else if (type == AppKey.PRINTER_DAYIN) {
                    printOrder(order_id);
                }
            }
        });
    }
}
