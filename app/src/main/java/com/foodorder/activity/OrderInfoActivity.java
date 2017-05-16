package com.foodorder.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.adapter.OrderGoodAdapter;
import com.foodorder.base.BaseActivity;
import com.foodorder.contant.AppKey;
import com.foodorder.contant.EventTag;
import com.foodorder.db.bean.Good;
import com.foodorder.log.DLOG;
import com.foodorder.logic.CartManager;
import com.foodorder.pop.FormulaPop;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.event.EventListener;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.server.api.API_Food;
import com.foodorder.server.callback.JsonResponseCallback;
import com.foodorder.util.PhoneUtil;
import com.foodorder.util.StringUtil;
import com.foodorder.util.ToastUtil;
import com.foodorder.widget.EmptyLayout;
import com.lzy.okhttputils.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "OrderInfoActivity";
    private ImageButton ib_back;
    private ListView lv_good;
    private OrderGoodAdapter goodAdapter;
    private List<Good> goodData;
    private View headView;
    private LinearLayout ll_number, ll_person;
    private TextView tv_order_num, tv_time, tv_number, tv_person_count, tv_total;
    private Button btn_add;
    private EmptyLayout emptyLayout;
    private NumberFormat nf;
    private String id_order, time, persons, number;
    private double total;
    private String type;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_info;
    }

    @Override
    public void initView() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        lv_good = (ListView) findViewById(R.id.lv_good);
        tv_total = (TextView) findViewById(R.id.tv_total);
        btn_add = (Button) findViewById(R.id.btn_add);
        headView = LayoutInflater.from(this).inflate(R.layout.order_info_header_view, null);
        ll_number = (LinearLayout) headView.findViewById(R.id.ll_number);
        ll_person = (LinearLayout) headView.findViewById(R.id.ll_person);
        tv_order_num = (TextView) headView.findViewById(R.id.tv_order_num);
        tv_time = (TextView) headView.findViewById(R.id.tv_time);
        tv_number = (TextView) headView.findViewById(R.id.tv_number);
        tv_person_count = (TextView) headView.findViewById(R.id.tv_person_count);
        lv_good.addHeaderView(headView, null, false);

        ib_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        lv_good.setOnItemClickListener(this);

        nf = NumberFormat.getCurrencyInstance(RT.locale);
        nf.setMaximumFractionDigits(RT.PRICE_NUM);

        emptyLayout = new EmptyLayout(this, lv_good);
        emptyLayout.showLoading();
        emptyLayout.setErrorButtonShow(true);
        emptyLayout.setEmptyButtonShow(true);
        emptyLayout.setEmptyButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyLayout.showLoading();
                API_Food.ins().getOrderInfo(TAG, id_order, infoCallback);
            }
        });

        emptyLayout.setErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyLayout.showLoading();
                API_Food.ins().getOrderInfo(TAG, id_order, infoCallback);
            }
        });
        EventManager.ins().registListener(EventTag.ACTIVITY_FINISH, eventListener);
    }

    @Override
    public void initData() {
        id_order = getIntent().getStringExtra(AppKey.ORDER_ID);
        if (goodData == null) {
            goodData = new ArrayList<>();
        }
        if (RT.DEBUG) {
            Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    String order_json = StringUtil.getJson(OrderInfoActivity.this, "order.json");
                    try {
                        parseJson(new JSONObject(order_json).optJSONObject("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
                @Override
                public void onCompleted() {
                    emptyLayout.showContent();
                    goodAdapter = new OrderGoodAdapter(OrderInfoActivity.this);
                    goodAdapter.setData(goodData);
                    lv_good.setAdapter(goodAdapter);
                    tv_order_num.setText(number);
                    tv_time.setText(time);
                    if (type.equals(AppKey.ORDER_TYPE_EMPORTER)) {
                        ll_number.setVisibility(View.GONE);
                        ll_person.setVisibility(View.GONE);
                    } else {
                        ll_number.setVisibility(View.VISIBLE);
                        ll_person.setVisibility(View.VISIBLE);
                        tv_number.setText(number);
                        tv_person_count.setText(persons);
                    }
                    tv_total.setText(nf.format(total));
                }

                @Override
                public void onError(Throwable e) {
                    DLOG.e(e.getMessage());
                }

                @Override
                public void onNext(Object o) {

                }
            });
        } else {
            API_Food.ins().getOrderInfo(TAG, id_order, infoCallback);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(TAG);
        EventManager.ins().removeListener(EventTag.ACTIVITY_FINISH, eventListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                onBackPressed();
                break;
            case R.id.btn_add:
                if (type.equals(AppKey.ORDER_TYPE_EMPORTER)) {
                    CartManager.ins().isPack = true;
                } else {
                    CartManager.ins().isPack = false;
                }
                Intent intent = null;
                if (PhoneUtil.isPad(OrderInfoActivity.this)) {
                    intent = new Intent(OrderInfoActivity.this, GoodListPadActivity.class);
                } else {
                    intent = new Intent(OrderInfoActivity.this, GoodListActivity.class);
                }
                intent.putExtra(AppKey.GOOD_LIST_TYPE, AppKey.GOOD_LIST_ADD);
                intent.putExtra(AppKey.ID_ORDER, id_order);
                if (type.equals(AppKey.ORDER_TYPE_SURPLACE)) {
                    intent.putExtra(AppKey.ORDER_NUMBER, number);
                }
                intent.putExtra(AppKey.ORDER_PERSON, persons);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Good good = (Good) parent.getAdapter().getItem(position);
        if (good == null) {
            return;
        }
        if (good.getFormulaList() != null && good.getFormulaList().size() > 0) {
            FormulaPop pop = new FormulaPop(this, good, FormulaPop.TYPE_ORDER);
            pop.showPopup();
        }
    }

    EventListener eventListener = new EventListener() {
        @Override
        public void handleMessage(int what, int arg1, int arg2, Object dataobj) {
            switch (what) {
                case EventTag.ACTIVITY_FINISH:
                    finish();
                    break;
            }
        }
    };

    JsonResponseCallback infoCallback = new JsonResponseCallback() {
        @Override
        public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
            if (errcode == 200 && json != null) {
                emptyLayout.showContent();
                parseJson(json);
                goodAdapter = new OrderGoodAdapter(OrderInfoActivity.this);
                goodAdapter.setData(goodData);
                lv_good.setAdapter(goodAdapter);
                tv_order_num.setText(number);
                tv_time.setText(time);
                if (type.equals(AppKey.ORDER_TYPE_EMPORTER)) {
                    ll_number.setVisibility(View.GONE);
                    ll_person.setVisibility(View.GONE);
                } else {
                    ll_number.setVisibility(View.VISIBLE);
                    ll_person.setVisibility(View.VISIBLE);
                    tv_number.setText(number);
                    tv_person_count.setText(persons);
                }
                tv_total.setText(nf.format(total));
            } else {
                ToastUtil.showToast(errmsg);
                emptyLayout.showError();
            }
            return false;
        }
    };

    public void parseJson(JSONObject data) {
        if (data == null) {
            return;
        }
        if (goodData != null) {
            goodData.clear();
        }
        this.id_order = data.optString("id_order");
        this.time = data.optString("time");
        this.persons = data.optString("persons");
        this.type = data.optString("type");
        this.number = data.optString("number");
        this.total = data.optDouble("total", 0);
        JSONArray detailArray = data.optJSONArray("Detail");
        if (detailArray != null && detailArray.length() > 0) {
            for (int i = 0; i < detailArray.length(); i++) {
                Good good = new Good(detailArray.optJSONObject(i));
                goodData.add(good);
            }
        }

    }

}
