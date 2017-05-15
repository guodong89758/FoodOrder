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
    private List<Printer> printerList;
    private List<Printer> postList;

    private PrinterManager() {
        printerList = new ArrayList<>();
        postList = new ArrayList<>();
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

    public void addPrinter(Printer printer) {
        printerList.add(printer);
    }

    public void addPost(Printer post) {
        postList.add(post);
    }

    public List<Printer> getPrinterList() {
        return printerList;
    }

    public List<Printer> getPostList() {
        return postList;
    }

    public void clearPrinterList() {
        if (printerList != null) {
            printerList.clear();
        }
    }

    public void clearPostList() {
        if (postList != null) {
            postList.clear();
        }
    }

    public void remindOrder(String order_id, String posts) {
        API_Food.ins().remindOrder(AppKey.HTTP_TAG, order_id, posts, new JsonResponseCallback() {
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

    public void printOrder(String order_id, String printers) {
        API_Food.ins().printOrder(AppKey.HTTP_TAG, order_id, printers, new JsonResponseCallback() {
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
                if (postList.size() > 1) {
                    showPrinterDialog(context, AppKey.PRINTER_CUIDAN, order.getId_order());
                } else {
                    if (postList.size() == 1) {
                        remindOrder(order.getId_order(), postList.get(0).getName());
                    } else {
                        remindOrder(order.getId_order(), "");
                    }
                }

            }
        });
        dialog.setButton2(new OrderActionDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, OrderActionDialog dialog, Order order) {
                dialog.dismiss();
                if (printerList.size() > 1) {
                    showPrinterDialog(context, AppKey.PRINTER_DAYIN, order.getId_order());
                } else {
                    if (printerList.size() == 1) {
                        printOrder(order.getId_order(), printerList.get(0).getName());
                        ToastUtil.showBottomToast(printerList.get(0).getName());
                    } else {
                        printOrder(order.getId_order(), "");
                    }
                }

            }
        });
        dialog.show();
    }

    public void showPrinterDialog(Context context, int fromType, final String order_id) {
        List<Printer> curList = null;
        if (fromType == AppKey.PRINTER_CUIDAN) {
            curList = postList;
        } else if (fromType == AppKey.PRINTER_DAYIN) {
            curList = printerList;
        }
        PrinterDialog printerDialog = new PrinterDialog(context, fromType, curList);
        printerDialog.show();
        printerDialog.setOnCheckListener(new PrinterDialog.OnCheckListener() {
            @Override
            public void onCheck(int type, String printer) {
                ToastUtil.showBottomToast(printer);
                if (type == AppKey.PRINTER_CUIDAN) {
                    remindOrder(order_id, printer);
                } else if (type == AppKey.PRINTER_DAYIN) {
                    printOrder(order_id, printer);
                }
            }
        });
    }
}
