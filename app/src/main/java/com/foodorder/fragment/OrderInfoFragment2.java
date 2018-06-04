package com.foodorder.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.activity.GoodListActivity;
import com.foodorder.activity.GoodListPadActivity;
import com.foodorder.adapter.OrderVoucherAdapter;
import com.foodorder.base.BaseFragment;
import com.foodorder.contant.AppKey;
import com.foodorder.entry.OrderInfo;
import com.foodorder.log.DLOG;
import com.foodorder.logic.CartManager;
import com.foodorder.logic.UserManager;
import com.foodorder.runtime.RT;
import com.foodorder.server.api.API_Food;
import com.foodorder.server.callback.JsonResponseCallback;
import com.foodorder.util.PhoneUtil;
import com.foodorder.util.StringUtil;
import com.foodorder.util.ToastUtil;
import com.foodorder.widget.EmptyLayout;
import com.foodorder.widget.recycler.WrapRecyclerView;
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


public class OrderInfoFragment2 extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "OrderInfoFragment2";
    private WrapRecyclerView rv_voucher;
    private OrderVoucherAdapter goodAdapter;
    private List<OrderInfo> goodData;
    private View headView, footerView;
    private TextView tv_company, tv_address, tv_city, tv_account, tv_time, tv_table, tv_person, tv_total_discount, tv_total_money, tv_good_count;
    private LinearLayout ll_discount, ll_head, ll_foot;
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
        rv_voucher = rootView.findViewById(R.id.rv_voucher);
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.order_info_header_view2, null);
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.order_voucher_footer, null);
        tv_company = headView.findViewById(R.id.tv_company);
        tv_address = headView.findViewById(R.id.tv_address);
        tv_city = headView.findViewById(R.id.tv_city);
        tv_account = (TextView) headView.findViewById(R.id.tv_account);
        tv_time = (TextView) headView.findViewById(R.id.tv_time);
        tv_table = (TextView) headView.findViewById(R.id.tv_table);
        tv_person = (TextView) headView.findViewById(R.id.tv_person);
        ll_head = headView.findViewById(R.id.ll_head);
        tv_total_discount = (TextView) footerView.findViewById(R.id.tv_total_discount);
        tv_total_money = (TextView) footerView.findViewById(R.id.tv_total_money);
        tv_good_count = (TextView) footerView.findViewById(R.id.tv_good_count);
        ll_discount = footerView.findViewById(R.id.ll_discount);
        ll_foot = footerView.findViewById(R.id.ll_foot);

        LinearLayout.LayoutParams head_params = (LinearLayout.LayoutParams) ll_head.getLayoutParams();
        head_params.width = RT.getScreenWidth();
        LinearLayout.LayoutParams foot_params = (LinearLayout.LayoutParams) ll_foot.getLayoutParams();
        foot_params.width = RT.getScreenWidth();

        rv_voucher.addHeaderView(headView);
        rv_voucher.addFootView(footerView);

        rv_voucher.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        rv_voucher.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).color(getResources().getColor(R.color.black_10)).size(1).build());
//        rv_good.setHasFixedSize(true);

        nf = NumberFormat.getCurrencyInstance(RT.locale);
        nf.setMaximumFractionDigits(RT.PRICE_NUM);

        emptyLayout = new EmptyLayout(getActivity(), rv_voucher);
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
                    goodAdapter = new OrderVoucherAdapter(getActivity(), goodData);
                    rv_voucher.setAdapter(goodAdapter);
                    tv_company.setText(UserManager.getInstance().getName());
                    tv_address.setText(UserManager.getInstance().getAddress());
                    tv_city.setText(UserManager.getInstance().getCity());
                    tv_account.setText(UserManager.getInstance().getUsername() + " " + UserManager.getInstance().getPassword());
                    tv_table.setText(number + "");
                    tv_person.setText(persons + "");
                    tv_time.setText(time);
                    if (total_reduction == 0.0) {
                        ll_discount.setVisibility(View.GONE);
                    } else {
                        ll_discount.setVisibility(View.VISIBLE);
                        tv_total_discount.setText(nf.format(total_reduction));
                    }
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


    JsonResponseCallback infoCallback = new JsonResponseCallback() {
        @Override
        public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
            if (errcode == 200 && json != null) {
                emptyLayout.showContent();
                parseJson(json);
                goodAdapter = new OrderVoucherAdapter(getActivity(), goodData);
                rv_voucher.setAdapter(goodAdapter);
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
