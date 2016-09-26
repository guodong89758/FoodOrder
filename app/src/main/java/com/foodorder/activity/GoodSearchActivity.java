package com.foodorder.activity;

import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.foodorder.R;
import com.foodorder.adapter.GoodSearchAdapter;
import com.foodorder.adapter.KeycodeAdapter;
import com.foodorder.base.BaseActivity;
import com.foodorder.base.BaseRecyclerAdapter;
import com.foodorder.contant.EventTag;
import com.foodorder.db.GoodDao;
import com.foodorder.db.bean.Good;
import com.foodorder.log.DLOG;
import com.foodorder.logic.CartManager;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.event.EventListener;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.util.ToastUtil;
import com.foodorder.widget.HorizontalDividerItemDecoration;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GoodSearchActivity extends BaseActivity implements BaseRecyclerAdapter.OnItemClickListener {

    private GridLayout gl_keybord;
    private RecyclerView rv_good, rv_code;
    private Button btn_pack;
    private KeycodeAdapter keycodeAdapter;
    private GoodSearchAdapter goodAdapter;
    private List<Good> codeData;
    private String search_content = "";

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
        btn_pack = (Button) findViewById(R.id.btn_pack);
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
        btn_pack.setOnClickListener(this);

        EventManager.ins().registListener(EventTag.GOOD_SEARCH_LIST_REFRESH, eventListener);

        rv_good.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_good.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(getResources().getColor(R.color.black_10)).size(1).build());
        rv_code.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void initData() {
        if (codeData == null) {
            codeData = new ArrayList<>();
        }
        goodAdapter = new GoodSearchAdapter(GoodSearchActivity.this, CartManager.ins().cartList);
        rv_good.setAdapter(goodAdapter);
        if (CartManager.ins().isPack) {
            btn_pack.setBackgroundResource(R.drawable.bg_pack_checked_true);
            btn_pack.setTextColor(getResources().getColor(R.color.white));
            btn_pack.setText(getString(R.string.good_check_pack));
        } else {
            btn_pack.setBackgroundResource(R.drawable.bg_pack_checked_false);
            btn_pack.setTextColor(getResources().getColor(R.color.black_50));
            btn_pack.setText(getString(R.string.good_check_pack_false));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.ins().removeListener(EventTag.GOOD_SEARCH_LIST_REFRESH, eventListener);
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
                ToastUtil.showToast(search_content);
                break;
            case R.id.btn_0:
                search_content = search_content + "0";
                search();
                break;
            case R.id.btn_pack:
                if (CartManager.ins().isPack) {
                    CartManager.ins().isPack = false;
                    btn_pack.setBackgroundResource(R.drawable.bg_pack_checked_false);
                    btn_pack.setTextColor(getResources().getColor(R.color.black_50));
                    btn_pack.setText(getString(R.string.good_check_pack_false));
                } else {
                    CartManager.ins().isPack = true;
                    btn_pack.setBackgroundResource(R.drawable.bg_pack_checked_true);
                    btn_pack.setTextColor(getResources().getColor(R.color.white));
                    btn_pack.setText(getString(R.string.good_check_pack));
                }
                break;
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
            }
        }
    };

    private void search() {
        if (TextUtils.isEmpty(search_content)) {
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
                        keycodeAdapter = new KeycodeAdapter(GoodSearchActivity.this, codeData);
                        keycodeAdapter.setOnItemClickListener(GoodSearchActivity.this);
                        rv_code.setAdapter(keycodeAdapter);
//                    } else {
//                        keycodeAdapter.notifyItemRangeChanged(0, codeData.size() - 1);
//                    }
                } else {
                    if (keycodeAdapter != null) {
                        keycodeAdapter.notifyDataSetChanged();
                    }
                }
                ToastUtil.showToast(codeData.size() + "");
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
                ToastUtil.showToast("规格");
            } else {
                if (CartManager.ins().cartList.get(good.getId().intValue()) != null) {
                    good.setCount(good.getCount() + 1);
                    goodAdapter.notifyDataSetChanged();
                } else {
                    CartManager.ins().add(good, false);
                    good.setCount(1);
                    goodAdapter.notifyItemInserted(CartManager.ins().cartList.size() - 1);
                    rv_good.smoothScrollToPosition(CartManager.ins().cartList.size() - 1);
                }

            }

        }
    }
}