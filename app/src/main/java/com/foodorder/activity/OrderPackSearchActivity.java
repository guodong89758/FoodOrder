package com.foodorder.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.adapter.PackOrderAdapter;
import com.foodorder.base.BaseActivity;
import com.foodorder.base.BaseRecyclerAdapter;
import com.foodorder.contant.AppKey;
import com.foodorder.contant.EventTag;
import com.foodorder.db.OrderDao;
import com.foodorder.db.bean.Order;
import com.foodorder.dialog.OrderActionDialog;
import com.foodorder.log.DLOG;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.server.api.API_Food;
import com.foodorder.server.callback.JsonResponseCallback;
import com.foodorder.util.ToastUtil;
import com.foodorder.widget.HorizontalDividerItemDecoration;
import com.lzy.okhttputils.OkHttpUtils;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderPackSearchActivity extends BaseActivity implements BaseRecyclerAdapter.OnItemClickListener, BaseRecyclerAdapter.OnItemLongClickListener {
    private static final String TAG = "OrderPackSearchActivity";
    private ImageButton ib_clear;
    private Button btn_back;
    private EditText et_search;
    private RecyclerView rv_order;
    private PackOrderAdapter orderAdapter;
    private List<Order> orderData;
    private String search_content = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_pack_search;
    }

    @Override
    public void initView() {
        ib_clear = (ImageButton) findViewById(R.id.ib_clear);
        btn_back = (Button) findViewById(R.id.btn_back);
        et_search = (EditText) findViewById(R.id.et_search);
        rv_order = (RecyclerView) findViewById(R.id.rv_order);

        rv_order.setLayoutManager(new LinearLayoutManager(this));
        rv_order.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(getResources().getColor(R.color.black_10)).size(1).build());
        rv_order.setHasFixedSize(true);

        ib_clear.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        et_search.setTransformationMethod(new AllCapTransformationMethod());
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search_content = et_search.getText().toString();
                    search();
                }
                return true;
            }
        });

        et_search.addTextChangedListener(textWatcher);
    }

    @Override
    public void initData() {
        if (orderData == null) {
            orderData = new ArrayList<>();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(TAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_clear:
                et_search.setText("");
                ib_clear.setVisibility(View.GONE);
                if (orderData != null) {
                    orderData.clear();
                }
                if (orderAdapter != null) {
                    orderAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position, long id) {
        Order order = orderData.get(position);
        if (order == null) {
            return;
        }
        Intent intent = new Intent(OrderPackSearchActivity.this, OrderInfoActivity.class);
        intent.putExtra(AppKey.ORDER_ID, order.getId_order());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(View view, int position, long id) {
        Order order = orderData.get(position);
        if (order != null) {
            showActionDialog(order);
        }
        return true;
    }

    private void showActionDialog(Order order) {
        if (order == null) {
            return;
        }
        OrderActionDialog dialog = new OrderActionDialog(this, order);
        dialog.setButton1(new OrderActionDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, final OrderActionDialog dialog, Order order) {
                dialog.dismiss();
//                ToastUtil.showToast(getResources().getString(R.string.order_action_1));
                API_Food.ins().remindOrder(TAG, order.getId_order(), new JsonResponseCallback() {
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
        });
        dialog.setButton2(new OrderActionDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, OrderActionDialog dialog, Order order) {
                dialog.dismiss();
//                ToastUtil.showToast(getResources().getString(R.string.order_action_2));
                API_Food.ins().printOrder(TAG, order.getId_order(), new JsonResponseCallback() {
                    @Override
                    public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
                        ToastUtil.showToast(errmsg);
                        return false;
                    }
                });
            }
        });
        dialog.show();
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                ib_clear.setVisibility(View.VISIBLE);
            } else {
                ib_clear.setVisibility(View.GONE);
            }
            search_content = s.toString();
            search();
        }
    };

    public void search() {
        if (TextUtils.isEmpty(search_content)) {
            if (orderData != null) {
                orderData.clear();
            }
            if (orderAdapter != null) {
                orderAdapter.notifyDataSetChanged();
            }
//            ToastUtil.showToast(getString(R.string.order_pack_search_hint));
            return;
        }
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                if (orderData != null) {
                    orderData.clear();
                }
                orderData = queryOrder();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {
                orderAdapter = new PackOrderAdapter(OrderPackSearchActivity.this, orderData);
                orderAdapter.setOnItemClickListener(OrderPackSearchActivity.this);
                orderAdapter.setOnItemLongClickListener(OrderPackSearchActivity.this);
                rv_order.setAdapter(orderAdapter);
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

    private List<Order> queryOrder() {
        QueryBuilder<Order> qb = RT.ins().getDaoSession().getOrderDao().queryBuilder();
        qb.where(OrderDao.Properties.Number.like(search_content + "%"));
        return qb.list();
    }

    public class AllCapTransformationMethod extends ReplacementTransformationMethod {

        @Override
        protected char[] getOriginal() {
            char[] aa = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            return aa;
        }

        @Override
        protected char[] getReplacement() {
            char[] cc = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            return cc;
        }

    }
}
