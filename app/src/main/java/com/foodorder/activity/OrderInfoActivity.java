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
import com.foodorder.db.bean.Good;
import com.foodorder.log.DLOG;
import com.foodorder.pop.FormulaPop;
import com.foodorder.runtime.RT;
import com.foodorder.util.StringUtil;

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
    private ImageButton ib_back;
    private ListView lv_good;
    private OrderGoodAdapter goodAdapter;
    private List<Good> goodData;
    private View headView;
    private LinearLayout ll_number, ll_person;
    private TextView tv_order_num, tv_time, tv_number, tv_person_count, tv_total;
    private Button btn_add;
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

        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(RT.PRICE_NUM);
    }

    @Override
    public void initData() {
        if (goodData == null) {
            goodData = new ArrayList<>();
        }

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                String order_json = StringUtil.getJson(OrderInfoActivity.this, "order.json");
                try {
                    parseJson(new JSONObject(order_json));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {
                goodAdapter = new OrderGoodAdapter(OrderInfoActivity.this);
                goodAdapter.setData(goodData);
                lv_good.setAdapter(goodAdapter);
                tv_order_num.setText(id_order);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                onBackPressed();
                break;
            case R.id.btn_add:
                Intent intent = new Intent(OrderInfoActivity.this, GoodListActivity.class);
                intent.putExtra(AppKey.GOOD_LIST_TYPE, AppKey.GOOD_LIST_ADD);
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

    public void parseJson(JSONObject json) {
        if (json == null) {
            return;
        }
        if (goodData != null) {
            goodData.clear();
        }
        JSONObject data = json.optJSONObject("data");
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
