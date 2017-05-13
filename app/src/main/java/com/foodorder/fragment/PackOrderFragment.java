package com.foodorder.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodorder.R;
import com.foodorder.activity.OrderInfoActivity;
import com.foodorder.adapter.PackOrderAdapter;
import com.foodorder.base.BaseFragment;
import com.foodorder.base.BaseRecyclerAdapter;
import com.foodorder.contant.AppKey;
import com.foodorder.contant.EventTag;
import com.foodorder.db.OrderDao;
import com.foodorder.db.bean.Order;
import com.foodorder.log.DLOG;
import com.foodorder.logic.PrinterManager;
import com.foodorder.parse.OrdersParse;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.event.EventListener;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.server.api.API_Food;
import com.foodorder.server.callback.JsonResponseCallback;
import com.foodorder.util.ToastUtil;
import com.foodorder.widget.EmptyLayout;
import com.foodorder.widget.HorizontalDividerItemDecoration;
import com.lzy.okhttputils.OkHttpUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.lzy.okhttputils.interceptor.LoggerInterceptor.TAG;


public class PackOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseRecyclerAdapter.OnItemClickListener, BaseRecyclerAdapter.OnItemLongClickListener {

    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView rv_pack;
    private EmptyLayout emptyLayout;
    private PackOrderAdapter orderAdapter;
    private List<Order> orderData;

    public PackOrderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pack, container, false);
        swipe_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swip_refresh);
        rv_pack = (RecyclerView) rootView.findViewById(R.id.rv_pack);

        rv_pack.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_pack.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).color(getResources().getColor(R.color.black_10)).size(1).build());
        rv_pack.setHasFixedSize(true);

        swipe_refresh.setColorSchemeResources(R.color.refresh_progress_blue, R.color.refresh_progress_green, R.color.refresh_progress_red, R.color.refresh_progress_yellow);
        swipe_refresh.setOnRefreshListener(this);

        emptyLayout = new EmptyLayout(getActivity(), swipe_refresh);
        emptyLayout.setShowTemp(true);
        emptyLayout.setEmptyText(RT.getString(R.string.order_list_empty));
        emptyLayout.setEmptyButtonShow(true);
        emptyLayout.setErrorButtonShow(true);
        emptyLayout.showLoading();
        emptyLayout.setEmptyButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyLayout.showLoading();
                API_Food.ins().getOrderList(TAG, refreshCallback);
            }
        });
        emptyLayout.setErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyLayout.showLoading();
                API_Food.ins().getOrderList(TAG, refreshCallback);
            }
        });

        EventManager.ins().registListener(EventTag.ORDER_PACK_LIST_REFRESH, eventListener);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (orderData == null) {
            orderData = new ArrayList<>();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventManager.ins().removeListener(EventTag.ORDER_PACK_LIST_REFRESH, eventListener);
        OkHttpUtils.getInstance().cancelTag(TAG);
    }

    @Override
    public void onRefresh() {
        API_Food.ins().getOrderList(TAG, refreshCallback);
    }

    @Override
    public void onItemClick(View view, int position, long id) {
        Order order = orderData.get(position);
        if (order == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
        intent.putExtra(AppKey.ORDER_ID, order.getId_order());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(View view, int position, long id) {
        Order order = orderData.get(position);
        if (order != null) {
            PrinterManager.ins().showActionDialog(getActivity(), order);
        }
        return true;
    }


    EventListener eventListener = new EventListener() {
        @Override
        public void handleMessage(int what, int arg1, int arg2, Object dataobj) {
            switch (what) {
                case EventTag.ORDER_PACK_LIST_REFRESH:
                    if (RT.DEBUG) {
                        testData();
                    } else {
                        parseJson();
                    }
                    break;
            }
        }
    };

    JsonResponseCallback refreshCallback = new JsonResponseCallback() {
        @Override
        public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
            swipe_refresh.setRefreshing(false);
            if (errcode == 200 && json != null) {
                OrdersParse.parseJson(json);
                parseJson();
            } else {
                emptyLayout.showEmptyOrError(errcode);
                ToastUtil.showToast(errmsg);
            }
            return false;
        }
    };

    private void parseJson() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                orderData = RT.ins().getDaoSession().getOrderDao().queryBuilder().where(OrderDao.Properties.Type.eq(AppKey.ORDER_TYPE_EMPORTER)).build().list();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {
//                if (orderAdapter == null) {
                orderAdapter = new PackOrderAdapter(getActivity(), orderData);
                orderAdapter.setOnItemClickListener(PackOrderFragment.this);
                orderAdapter.setOnItemLongClickListener(PackOrderFragment.this);
                rv_pack.setAdapter(orderAdapter);
//                } else {
//                    orderAdapter.notifyDataSetChanged();
//                }
                if (orderData.size() > 0) {
                    emptyLayout.showContent();
                } else {
                    emptyLayout.showEmpty();
                }

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


    private void testData() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
//                String order_json = StringUtil.getJson(getActivity(), "orders.json");
//                try {
//                    OrdersParse.parseJson(new JSONObject(order_json));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                orderData = RT.ins().getDaoSession().getOrderDao().queryBuilder().where(OrderDao.Properties.Type.eq(AppKey.ORDER_TYPE_EMPORTER)).build().list();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {
                orderAdapter = new PackOrderAdapter(getActivity(), orderData);
                orderAdapter.setOnItemClickListener(PackOrderFragment.this);
                orderAdapter.setOnItemLongClickListener(PackOrderFragment.this);
                rv_pack.setAdapter(orderAdapter);
                if (orderData.size() > 0) {
                    emptyLayout.showContent();
                } else {
                    emptyLayout.showEmpty();
                }
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
}
