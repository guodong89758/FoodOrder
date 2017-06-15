package com.foodorder.logic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;

import com.foodorder.R;
import com.foodorder.contant.AppKey;
import com.foodorder.contant.EventTag;
import com.foodorder.db.bean.Order;
import com.foodorder.dialog.OrderActionDialog;
import com.foodorder.dialog.PrinterDialog;
import com.foodorder.entry.Printer;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.server.api.API_Food;
import com.foodorder.server.callback.JsonResponseCallback;
import com.foodorder.util.PreferenceHelper;
import com.foodorder.util.ToastUtil;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.DataForSendToPrinterPos80;
import net.posprinter.utils.DataForSendToPrinterTSC;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by guodong on 2017/5/13.
 */

public class PrinterManager {

    private volatile static PrinterManager instance;
    private List<Printer> printerList;
    private List<Printer> postList;
    public boolean isConnect;

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

    public static IMyBinder binder;//IMyBinder接口，所有可供调用的连接和发送数据的方法都封装在这个接口内
    //bindService的参数conn
    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            //绑定成功
            binder = (IMyBinder) service;
            connectPrinter();
        }
    };

    public void bindPrintService(Activity activity) {
        //绑定service，获取ImyBinder对象
        Intent intent = new Intent(activity, PosprinterService.class);
        activity.bindService(intent, conn, BIND_AUTO_CREATE);
    }

    public void destory(Activity activity) {
        if(binder == null || !isConnect){
            return;
        }
        binder.disconnectCurrentPort(new UiExecute() {

            @Override
            public void onsucess() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onfailed() {
                // TODO Auto-generated method stub

            }
        });
        activity.unbindService(conn);
    }

    public void connectPrinter() {
        String bluetooth = PreferenceHelper.ins().getStringShareData(AppKey.DEFAULT_BLUETOOTCH, "");
        if (TextUtils.isEmpty(bluetooth)) {
            ToastUtil.showBottomToast(RT.getString(R.string.bluetootch_empty));
            return;
        }
        binder.connectBtPort(bluetooth, new UiExecute() {

            @Override
            public void onsucess() {
                // TODO Auto-generated method stub
                //连接成功后在UI线程中的执行
                isConnect = true;
                ToastUtil.showBottomToast(RT.getString(R.string.bluetootch_connect_success));
                //此处也可以开启读取打印机的数据
                //参数同样是一个实现的UiExecute接口对象
                //如果读的过程重出现异常，可以判断连接也发生异常，已经断开
                //这个读取的方法中，会一直在一条子线程中执行读取打印机发生的数据，
                //直到连接断开或异常才结束，并执行onfailed
                binder.acceptdatafromprinter(new UiExecute() {

                    @Override
                    public void onsucess() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onfailed() {
                        // TODO Auto-generated method stub
                        isConnect = false;
                        ToastUtil.showBottomToast(RT.getString(R.string.bluetootch_connect_failed));
                    }
                });
            }

            @Override
            public void onfailed() {
                // TODO Auto-generated method stub
                //连接失败后在UI线程中的执行
                isConnect = false;
                ToastUtil.showBottomToast(RT.getString(R.string.bluetootch_connect_failed));
            }
        });
    }

    /**
     * 断开打印机连接
     */
    public void disconnect() {
        if (isConnect) {//如果是连接状态才执行断开操作
            binder.disconnectCurrentPort(new UiExecute() {

                @Override
                public void onsucess() {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onfailed() {
                    // TODO Auto-generated method stub
                }
            });
        } else {
        }
    }

    /**
     * 打印文字
     *
     * @param text
     */
    public void printText(final String text) {
        //此处用binder里的另外一个发生数据的方法,同样，也要按照编程手册上的示例一样，先设置标签大小
        //如果数据处理较为复杂，请勿选择此方法
        //上面的发送方法的数据处理是在工作线程中完成的，不会阻塞UI线程
//        DataForSendToPrinterTSC.setCharsetName("gbk");//不设置，默认为gbk
//        byte[] data0 = DataForSendToPrinterTSC.sizeBydot(480, 240);
//        byte[] data1 = DataForSendToPrinterTSC.cls();
//        byte[] data2 = DataForSendToPrinterTSC.text(10, 10, "0", 0, 2, 2, text);
//        byte[] data3 = DataForSendToPrinterTSC.print(1);
//        byte[] data = byteMerger(byteMerger(byteMerger(data0, data1), data2), data3);
//        if (isConnect) {
//            binder.write(data, new UiExecute() {
//
//                @Override
//                public void onsucess() {
//                    // TODO Auto-generated method stub
//                    ToastUtil.showBottomToast(RT.getString(R.string.bluetootch_print_success));
//                }
//
//                @Override
//                public void onfailed() {
//                    // TODO Auto-generated method stub
//                    ToastUtil.showBottomToast(RT.getString(R.string.bluetootch_print_failed));
//                }
//            });
//        } else {
//            ToastUtil.showBottomToast(RT.getString(R.string.bluetootch_connect_failed));
//        }
        if(!isConnect){
            return;
        }
        DataForSendToPrinterTSC.setCharsetName("gbk");//不设置，默认为gbk

        binder.writeDataByYouself(new UiExecute() {

            @Override
            public void onsucess() {
                // TODO Auto-generated method stub
                ToastUtil.showBottomToast(RT.getString(R.string.bluetootch_print_success));

            }

            @Override
            public void onfailed() {
                // TODO Auto-generated method stub
                ToastUtil.showBottomToast(RT.getString(R.string.bluetootch_print_failed));

            }
        }, new ProcessData() {

            @Override
            public List<byte[]> processDataBeforeSend() {
                // TODO Auto-generated method stub
                List<byte[]> list = new ArrayList<byte[]>();
                //创建一段我们想打印的文本,转换为byte[]类型，并添加到要发送的数据的集合list中
                list.add(DataForSendToPrinterPos80.printAndFeedLine());
                list.add(DataForSendToPrinterPos80.printAndFeedLine());
                byte[] data1 = strTobytes(text);
                list.add(data1);
                //追加一个打印换行指令，因为，pos打印机满一行才打印，不足一行，不打印
                list.add(DataForSendToPrinterPos80.printAndFeedLine());
                list.add(DataForSendToPrinterPos80.printAndFeedLine());
                return list;
            }
        });
    }

    /**
     * 字符串转byte数组
     */
    public static byte[] strTobytes(String str) {
        byte[] b = null, data = null;
        try {
            b = str.getBytes("utf-8");
            data = new String(b, "utf-8").getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }

    /**
     * byte数组拼接
     */
    private byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
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

    public void printOrder(final Order order, String order_id, String printers) {
        if (RT.DEBUG) {
            String text = "";
            if (TextUtils.equals(order.getType(), AppKey.ORDER_TYPE_EMPORTER)) {
                text = "Order Number: " + order.getNumber();
            } else {
                text = "Number: " + order.getNumber();
            }
            printText(text);

        }
        API_Food.ins().printOrder(AppKey.HTTP_TAG, order_id, printers, new JsonResponseCallback() {
            @Override
            public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
                if (errcode == 200) {
                    String text = "";
                    if (TextUtils.equals(order.getType(), AppKey.ORDER_TYPE_EMPORTER)) {
                        text = "Order Number: " + order.getNumber();
                    } else {
                        text = "Number: " + order.getNumber();
                    }
                    printText(text);
                }
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
                    showPrinterDialog(context, AppKey.PRINTER_CUIDAN, order, order.getId_order());
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
                    showPrinterDialog(context, AppKey.PRINTER_DAYIN, order, order.getId_order());
                } else {
                    if (printerList.size() == 1) {
                        printOrder(order, order.getId_order(), printerList.get(0).getName());
                        ToastUtil.showBottomToast(printerList.get(0).getName());
                    } else {
                        printOrder(order, order.getId_order(), "");
                    }
                }

            }
        });
        dialog.show();
    }

    public void showPrinterDialog(Context context, int fromType, final Order order, final String order_id) {
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
                    printOrder(order, order_id, printer);
                }
            }
        });
    }
}
