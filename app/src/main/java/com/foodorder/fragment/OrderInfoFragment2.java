package com.foodorder.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.activity.GoodListActivity;
import com.foodorder.activity.GoodListPadActivity;
import com.foodorder.adapter.OrderVoucherAdapter;
import com.foodorder.base.BaseFragment;
import com.foodorder.contant.AppKey;
import com.foodorder.db.bean.Good;
import com.foodorder.entry.OrderInfo;
import com.foodorder.log.DLOG;
import com.foodorder.logic.CartManager;
import com.foodorder.logic.UserManager;
import com.foodorder.pop.FormulaPop;
import com.foodorder.runtime.RT;
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


public class OrderInfoFragment2 extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "OrderInfoFragment2";
    private ListView lv_voucher;
    private OrderVoucherAdapter goodAdapter;
    private List<OrderInfo> goodData;
    private View headView, footerView;
    private TextView tv_company, tv_address, tv_city, tv_account, tv_time, tv_table, tv_total_discount, tv_total_money, tv_good_count;
    private EmptyLayout emptyLayout;
    private NumberFormat nf;
    private String id_order, time, persons, number;
    private double total, total_reduction;
    private String type;

    public static OrderInfoFragment2 newInstance(String id_order) {
        OrderInfoFragment2 infoFragment = new OrderInfoFragment2();
        Bundle bundle = new Bundle();
        bundle.putString(AppKey.ORDER_ID, id_order);
        infoFragment.setArguments(bundle);
        return infoFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        id_order = getArguments().getString(AppKey.ORDER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_info_2, container, false);
        lv_voucher = (ListView) rootView.findViewById(R.id.lv_voucher);
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.order_info_header_view2, null);
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.order_voucher_footer, null);
        tv_company = headView.findViewById(R.id.tv_company);
        tv_address = headView.findViewById(R.id.tv_address);
        tv_city = headView.findViewById(R.id.tv_city);
        tv_account = (TextView) headView.findViewById(R.id.tv_account);
        tv_time = (TextView) headView.findViewById(R.id.tv_time);
        tv_table = (TextView) headView.findViewById(R.id.tv_table);
        tv_total_discount = (TextView) footerView.findViewById(R.id.tv_total_discount);
        tv_total_money = (TextView) footerView.findViewById(R.id.tv_total_money);
        tv_good_count = (TextView) footerView.findViewById(R.id.tv_good_count);
        lv_voucher.addHeaderView(headView, null, false);
        lv_voucher.addFooterView(footerView, null, false);

        lv_voucher.setOnItemClickListener(this);

        nf = NumberFormat.getCurrencyInstance(RT.locale);
        nf.setMaximumFractionDigits(RT.PRICE_NUM);

        emptyLayout = new EmptyLayout(getActivity(), lv_voucher);
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
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (goodData == null) {
            goodData = new ArrayList<>();
        }
        if (RT.DEBUG) {
            Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    String order_json = StringUtil.getJson(getActivity(), "order.json");
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
                    goodAdapter = new OrderVoucherAdapter(getActivity());
                    goodAdapter.setData(goodData);
                    lv_voucher.setAdapter(goodAdapter);
                    tv_company.setText(UserManager.getInstance().getName());
                    tv_address.setText(UserManager.getInstance().getAddress());
                    tv_city.setText(UserManager.getInstance().getCity());
                    tv_account.setText(UserManager.getInstance().getUsername() + " " + UserManager.getInstance().getPassword());
                    tv_table.setText(number + "，" + persons);
                    tv_time.setText(time);
                    tv_total_discount.setText(nf.format(total_reduction));
                    tv_total_money.setText(nf.format(total));
                    tv_good_count.setText("" + goodData.size());
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
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(TAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if (type.equals(AppKey.ORDER_TYPE_EMPORTER)) {
                    CartManager.ins().isPack = true;
                } else {
                    CartManager.ins().isPack = false;
                }
                Intent intent = null;
                if (PhoneUtil.isPad(getActivity())) {
                    intent = new Intent(getActivity(), GoodListPadActivity.class);
                } else {
                    intent = new Intent(getActivity(), GoodListActivity.class);
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
            FormulaPop pop = new FormulaPop(getActivity(), good, FormulaPop.TYPE_ORDER);
            pop.showPopup();
        }
    }

    JsonResponseCallback infoCallback = new JsonResponseCallback() {
        @Override
        public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
            if (errcode == 200 && json != null) {
                emptyLayout.showContent();
                parseJson(json);
                goodAdapter = new OrderVoucherAdapter(getActivity());
                goodAdapter.setData(goodData);
                lv_voucher.setAdapter(goodAdapter);
                tv_company.setText(UserManager.getInstance().getName());
                tv_address.setText(UserManager.getInstance().getAddress());
                tv_city.setText(UserManager.getInstance().getCity());
                tv_account.setText(UserManager.getInstance().getUsername() + " " + UserManager.getInstance().getPassword());
                tv_table.setText(number + "，" + persons);
                tv_time.setText(time);
                tv_total_discount.setText(nf.format(total_reduction));
                tv_total_money.setText(nf.format(total));
                tv_good_count.setText("" + goodData.size());
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
        this.total_reduction = data.optDouble("total_reduction", 0);
        JSONArray detailArray = data.optJSONArray("Detail");
        if (detailArray != null && detailArray.length() > 0) {
            for (int i = 0; i < detailArray.length(); i++) {
                OrderInfo info = new OrderInfo(detailArray.optJSONObject(i));
                goodData.add(info);
            }
        }

    }
}
