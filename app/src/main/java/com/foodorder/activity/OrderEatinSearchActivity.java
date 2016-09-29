package com.foodorder.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.adapter.EatinOrderAdapter;
import com.foodorder.base.BaseActivity;
import com.foodorder.base.BaseRecyclerAdapter;
import com.foodorder.db.bean.Order;
import com.foodorder.dialog.OrderActionDialog;
import com.foodorder.util.ToastUtil;
import com.foodorder.widget.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class OrderEatinSearchActivity extends BaseActivity implements BaseRecyclerAdapter.OnItemClickListener, BaseRecyclerAdapter.OnItemLongClickListener {
    private ImageButton ib_clear;
    private Button btn_back;
    private EditText et_search;
    private RecyclerView rv_order;
    private EatinOrderAdapter orderAdapter;
    private List<Order> orderData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_eatin_search;
    }

    @Override
    public void initView() {
        ib_clear = (ImageButton) findViewById(R.id.ib_clear);
        btn_back = (Button) findViewById(R.id.btn_back);
        et_search = (EditText) findViewById(R.id.et_search);
        rv_order = (RecyclerView) findViewById(R.id.rv_order);

        ib_clear.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

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
        for (int i = 0; i < 5; i++) {
            orderData.add(new Order());
        }
        rv_order.setLayoutManager(new LinearLayoutManager(this));
        rv_order.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(getResources().getColor(R.color.black_10)).size(1).build());
        rv_order.setHasFixedSize(true);
        orderAdapter = new EatinOrderAdapter(this, orderData);
        orderAdapter.setOnItemClickListener(this);
        orderAdapter.setOnItemLongClickListener(this);
        rv_order.setAdapter(orderAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_clear:
                et_search.setText("");
                ib_clear.setVisibility(View.GONE);
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }
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
        }
    };

    @Override
    public void onItemClick(View view, int position, long id) {
        startActivity(new Intent(OrderEatinSearchActivity.this, OrderInfoActivity.class));
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
        OrderActionDialog dialog = new OrderActionDialog(this, order);
        dialog.setButton1(new OrderActionDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, OrderActionDialog dialog, Order order) {
                ToastUtil.showToast(getResources().getString(R.string.order_action_1));
            }
        });
        dialog.setButton2(new OrderActionDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, OrderActionDialog dialog, Order order) {
                ToastUtil.showToast(getResources().getString(R.string.order_action_2));
            }
        });
        dialog.show();
    }
}
