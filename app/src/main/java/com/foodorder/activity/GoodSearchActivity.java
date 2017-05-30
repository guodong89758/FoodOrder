package com.foodorder.activity;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.adapter.GoodSearchAdapter;
import com.foodorder.adapter.KeycodeAdapter;
import com.foodorder.base.BaseActivity;
import com.foodorder.base.BaseRecyclerAdapter;
import com.foodorder.contant.AppKey;
import com.foodorder.contant.EventTag;
import com.foodorder.db.GoodDao;
import com.foodorder.db.bean.Good;
import com.foodorder.dialog.NormalDialog;
import com.foodorder.log.DLOG;
import com.foodorder.logic.CartManager;
import com.foodorder.logic.PrinterManager;
import com.foodorder.pop.AttributePop;
import com.foodorder.pop.FormulaPop;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.event.EventListener;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.server.api.API_Food;
import com.foodorder.server.callback.JsonResponseCallback;
import com.foodorder.util.PhoneUtil;
import com.foodorder.util.ToastUtil;
import com.foodorder.widget.HorizontalDividerItemDecoration;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.foodorder.contant.EventTag.POPUP_FORMULA_SHOW;

public class GoodSearchActivity extends BaseActivity implements BaseRecyclerAdapter.OnItemClickListener {
    private GridLayout gl_keybord;
    private RecyclerView rv_good, rv_code;
    //    private Button btn_pack;
    private TextView tv_empty;
    private KeycodeAdapter keycodeAdapter;
    private GoodSearchAdapter goodAdapter;
    private List<Good> codeData;
    private String search_content = "";
    private String id_order = "";
    private String number = "";
    private String persons = "";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_good_search;
    }


    @Override
    public void initView() {
        findViewById(R.id.ib_back).setOnClickListener(this);
        gl_keybord = (GridLayout) findViewById(R.id.gl_keybord);
        rv_good = (RecyclerView) findViewById(R.id.rv_good);
        rv_code = (RecyclerView) findViewById(R.id.rv_code);
//        btn_pack = (Button) findViewById(btn_pack);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_0).setOnClickListener(this);
//        btn_pack.setOnClickListener(this);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) gl_keybord.getLayoutParams();
        params.width = RT.getScreenWidth();

        int btn_width = (RT.getScreenWidth() - PhoneUtil.dipToPixel(10, this)) / 4;
        int btn_height = RT.getScreenHeight()/12;
        GridLayout.LayoutParams params1 = (GridLayout.LayoutParams) findViewById(R.id.btn_1).getLayoutParams();
        params1.width = btn_width;
        params1.height = btn_height;
        GridLayout.LayoutParams params2 = (GridLayout.LayoutParams) findViewById(R.id.btn_2).getLayoutParams();
        params2.width = btn_width;
        params2.height = btn_height;
        GridLayout.LayoutParams params3 = (GridLayout.LayoutParams) findViewById(R.id.btn_3).getLayoutParams();
        params3.width = btn_width;
        params3.height = btn_height;
        GridLayout.LayoutParams params4 = (GridLayout.LayoutParams) findViewById(R.id.btn_4).getLayoutParams();
        params4.width = btn_width;
        params4.height = btn_height;
        GridLayout.LayoutParams params5 = (GridLayout.LayoutParams) findViewById(R.id.btn_5).getLayoutParams();
        params5.width = btn_width;
        params5.height = btn_height;
        GridLayout.LayoutParams params6 = (GridLayout.LayoutParams) findViewById(R.id.btn_6).getLayoutParams();
        params6.width = btn_width;
        params6.height = btn_height;
        GridLayout.LayoutParams params7 = (GridLayout.LayoutParams) findViewById(R.id.btn_7).getLayoutParams();
        params7.width = btn_width;
        params7.height = btn_height;
        GridLayout.LayoutParams params8 = (GridLayout.LayoutParams) findViewById(R.id.btn_8).getLayoutParams();
        params8.width = btn_width;
        params8.height = btn_height;
        GridLayout.LayoutParams params9 = (GridLayout.LayoutParams) findViewById(R.id.btn_9).getLayoutParams();
        params9.width = btn_width;
        params9.height = btn_height;
        GridLayout.LayoutParams params0 = (GridLayout.LayoutParams) findViewById(R.id.btn_0).getLayoutParams();
        params0.width = btn_width * 3;
        params0.height = btn_height;
        GridLayout.LayoutParams params_back = (GridLayout.LayoutParams) findViewById(R.id.btn_back).getLayoutParams();
        params_back.width = btn_width;
        params_back.height = btn_height;
        GridLayout.LayoutParams params_clear = (GridLayout.LayoutParams) findViewById(R.id.btn_clear).getLayoutParams();
        params_clear.width = btn_width;
        params_clear.height = btn_height;
//        GridLayout.LayoutParams params_pack = (GridLayout.LayoutParams) findViewById(R.id.btn_pack).getLayoutParams();
//        params_pack.width = btn_width * 2;
        GridLayout.LayoutParams params_send = (GridLayout.LayoutParams) findViewById(R.id.btn_send).getLayoutParams();
        params_send.width = btn_width;
        params_send.height = btn_height*2;

        EventManager.ins().registListener(EventTag.GOOD_SEARCH_LIST_REFRESH, eventListener);
        EventManager.ins().registListener(POPUP_FORMULA_SHOW, eventListener);
        EventManager.ins().registListener(EventTag.POPUP_ATTRIBUTE_SHOW, eventListener);

        rv_good.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_good.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(getResources().getColor(R.color.black_10)).size(1).build());
        rv_code.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void initData() {
        id_order = getIntent().getStringExtra(AppKey.ID_ORDER);
        number = getIntent().getStringExtra(AppKey.ORDER_NUMBER);
        persons = getIntent().getStringExtra(AppKey.ORDER_PERSON);
        if (codeData == null) {
            codeData = new ArrayList<>();
        }
        goodAdapter = new GoodSearchAdapter(GoodSearchActivity.this, CartManager.ins().cartData);
        rv_good.setAdapter(goodAdapter);
//        if (CartManager.ins().isPack) {
//            btn_pack.setBackgroundResource(R.drawable.bg_pack_checked_true);
//            btn_pack.setTextColor(getResources().getColor(R.color.white));
//            btn_pack.setText(getString(R.string.good_check_pack));
//        } else {
//            btn_pack.setBackgroundResource(R.drawable.bg_pack_checked_false);
//            btn_pack.setTextColor(getResources().getColor(R.color.black_50));
//            btn_pack.setText(getString(R.string.good_check_pack_false));
//        }
        goodAdapter.setOnItemClickListener(new GoodSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Good good) {
                if (good.getFormulaList() != null && good.getFormulaList().size() > 0) {
                    FormulaPop formulaPop = new FormulaPop(GoodSearchActivity.this, good, FormulaPop.TYPE_UPDATE);
                    formulaPop.showPopup();
                } else if (good.getAttributeList() != null && good.getAttributeList().size() > 0) {
                    AttributePop attrPop = new AttributePop(GoodSearchActivity.this, good, AttributePop.TYPE_UPDATE);
                    attrPop.showPopup();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.ins().removeListener(EventTag.GOOD_SEARCH_LIST_REFRESH, eventListener);
        EventManager.ins().removeListener(POPUP_FORMULA_SHOW, eventListener);
        EventManager.ins().removeListener(EventTag.POPUP_ATTRIBUTE_SHOW, eventListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                onBackPressed();
                break;
            case R.id.btn_1:
                search_content = search_content + "1";
                search();
                break;
            case R.id.btn_2:
                search_content = search_content + "2";
                search();
                break;
            case R.id.btn_3:
                search_content = search_content + "3";
                search();
                break;
            case R.id.btn_back:
                if (!TextUtils.isEmpty(search_content)) {
                    if (search_content.length() > 1) {
                        search_content = search_content.substring(0, search_content.length() - 1);
                    } else {
                        search_content = "";
                    }
                }
                search();
                break;
            case R.id.btn_4:
                search_content = search_content + "4";
                search();
                break;
            case R.id.btn_5:
                search_content = search_content + "5";
                search();
                break;
            case R.id.btn_6:
                search_content = search_content + "6";
                search();
                break;
            case R.id.btn_clear:
                search_content = "";
                search();
                break;
            case R.id.btn_7:
                search_content = search_content + "7";
                search();
                break;
            case R.id.btn_8:
                search_content = search_content + "8";
                search();
                break;
            case R.id.btn_9:
                search_content = search_content + "9";
                search();
                break;
            case R.id.btn_send:
//                if (TextUtils.isEmpty(id_order)) {
//                    OrderSetupPop setupPop = new OrderSetupPop(GoodSearchActivity.this, id_order);
//                    setupPop.showPopup();
//                } else {
//                    DLOG.json(CartManager.ins().getOrderGoodJson(false, id_order, "", ""));
                if (CartManager.ins().cartData != null && CartManager.ins().cartData.size() > 0) {
                    showOrderGoodDialog(this, id_order, number, persons);
                } else {
                    ToastUtil.showBottomToast(getString(R.string.cart_empty));
                }
//                }

                break;
            case R.id.btn_0:
                search_content = search_content + "0";
                search();
                break;
//            case btn_pack:
//                if (CartManager.ins().isPack) {
//                    CartManager.ins().isPack = false;
//                    btn_pack.setBackgroundResource(R.drawable.bg_pack_checked_false);
//                    btn_pack.setTextColor(getResources().getColor(R.color.black_50));
//                    btn_pack.setText(getString(R.string.good_check_pack_false));
//                } else {
//                    CartManager.ins().isPack = true;
//                    btn_pack.setBackgroundResource(R.drawable.bg_pack_checked_true);
//                    btn_pack.setTextColor(getResources().getColor(R.color.white));
//                    btn_pack.setText(getString(R.string.good_check_pack));
//                }
//                break;
        }
    }

    EventListener eventListener = new EventListener() {
        @Override
        public void handleMessage(int what, int arg1, int arg2, Object dataobj) {
            switch (what) {
                case EventTag.GOOD_SEARCH_LIST_REFRESH:
                    if (goodAdapter != null) {
                        goodAdapter.notifyDataSetChanged();
                    }
                    break;
                case EventTag.POPUP_FORMULA_SHOW:
                    FormulaPop formulaPop = new FormulaPop(GoodSearchActivity.this, (Good) dataobj, FormulaPop.TYPE_MENU);
                    formulaPop.showPopup();
                    break;
                case EventTag.POPUP_ATTRIBUTE_SHOW:
                    AttributePop attrPop = new AttributePop(GoodSearchActivity.this, (Good) dataobj, arg2);
                    attrPop.showPopup();
                    break;
            }
        }
    };

    private void search() {
        if (TextUtils.isEmpty(search_content)) {
            tv_empty.setText("");
            if (codeData != null && keycodeAdapter != null) {
                codeData.clear();
                keycodeAdapter.notifyDataSetChanged();
            }
            return;
        }
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                codeData.clear();
                codeData = queryCode();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {
                if (codeData != null && codeData.size() > 0) {
//                    if (keycodeAdapter == null) {
                    tv_empty.setVisibility(View.GONE);
                    keycodeAdapter = new KeycodeAdapter(GoodSearchActivity.this, codeData);
                    keycodeAdapter.setOnItemClickListener(GoodSearchActivity.this);
                    rv_code.setAdapter(keycodeAdapter);
//                    } else {
//                        keycodeAdapter.notifyItemRangeChanged(0, codeData.size() - 1);
//                    }
                } else {
                    tv_empty.setVisibility(View.VISIBLE);
                    tv_empty.setText(search_content);
                    if (keycodeAdapter != null) {
                        keycodeAdapter.notifyDataSetChanged();
                    }
                }
//                ToastUtil.showToast(codeData.size() + "");
            }

            @Override
            public void onError(Throwable e) {
                DLOG.e(e.getMessage());
            }

            @Override
            public void onNext(Object o) {

            }
        });

    }

    private List<Good> queryCode() {
        QueryBuilder<Good> qb = RT.ins().getDaoSession().getGoodDao().queryBuilder();
        qb.where(GoodDao.Properties.Search_num.like(search_content + "%"));
        return qb.list();
    }

    @Override
    public void onItemClick(View view, int position, long id) {
        Good good = codeData.get(position);
        if (good != null) {
            if ((good.getAttributeList() != null && good.getAttributeList().size() > 0) || (good.getFormulaList() != null && good.getFormulaList().size() > 0)) {
                if (good.getFormulaList() != null && good.getFormulaList().size() > 0) {
                    EventManager.ins().sendEvent(EventTag.POPUP_FORMULA_SHOW, 0, 0, good);
                } else {
                    if (good.getAttributeList() != null && good.getAttributeList().size() > 0) {
                        EventManager.ins().sendEvent(EventTag.POPUP_ATTRIBUTE_SHOW, 0, AttributePop.TYPE_MENU, good);
                    }
                }
                clearCode();
            } else {
                CartManager.ins().add(good, true);
                if (goodAdapter != null) {
                    goodAdapter.notifyDataSetChanged();
                    rv_good.smoothScrollToPosition(CartManager.ins().cartData.size() - 1);
                }
                clearCode();
//                if (CartManager.ins().cartList.get(good.getId().intValue()) != null) {
//                    good.setCount(good.getCount() + 1);
//                    goodAdapter.notifyDataSetChanged();
//                } else {
//                    CartManager.ins().add(good, false);
//                    good.setCount(1);
//                    goodAdapter.notifyItemInserted(CartManager.ins().cartList.size() - 1);
//                    rv_good.smoothScrollToPosition(CartManager.ins().cartList.size() - 1);
//                }

            }

        }
    }

    private void clearCode() {
        search_content = "";
        search();
    }

    public void showOrderGoodDialog(Context context, final String id_order, final String number, final String persons) {
        NormalDialog dialog = new NormalDialog(context);
        dialog.setTitle(R.string.good_order_dialog_title);
        dialog.setTextDes(RT.getString(R.string.good_order_dialog_desc));
        dialog.setButton1(RT.getString(R.string.action_cancel), new NormalDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, NormalDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.setButton2(getString(R.string.action_ok), new NormalDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, NormalDialog dialog) {
                dialog.dismiss();
                showLoadingDialog(false);
                API_Food.ins().orderGood(AppKey.HTTP_TAG, CartManager.ins().getOrderGoodJson(CartManager.ins().isPack, id_order, number, persons), new JsonResponseCallback() {
                    @Override
                    public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
                        hideLoadingDialog();
                        if (errcode == 200 && json != null) {
                            if(CartManager.ins().isPack){
                                PrinterManager.ins().printText("Order Number: " + json.optString("order_id"));
                            }else{
                                PrinterManager.ins().printText("Number: " + number);
                            }
                            CartManager.ins().clear();
                            EventManager.ins().sendEvent(EventTag.GOOD_LIST_REFRESH, 0, 0, true);
                            EventManager.ins().sendEvent(EventTag.GOOD_SEARCH_LIST_REFRESH, 0, 0, null);
                            EventManager.ins().sendEvent(EventTag.ORDER_LIST_REFRESH, 0, 0, null);
//                            Intent intent = new Intent(GoodSearchActivity.this, OrdersActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
                            EventManager.ins().sendEvent(EventTag.ACTIVITY_FINISH, 0, 0, null);
                            finish();
                            ToastUtil.showToast(RT.getString(R.string.good_order_success));
                        } else {
                            ToastUtil.showToast(RT.getString(R.string.good_order_failed));
                        }
                        return false;
                    }
                });
            }
        });
        dialog.show();
    }
}
