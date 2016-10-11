package com.foodorder.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.adapter.OrderGoodAdapter;
import com.foodorder.base.BaseActivity;
import com.foodorder.db.bean.Good;
import com.foodorder.pop.FormulaPop;

import java.util.ArrayList;
import java.util.List;

public class OrderInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ImageButton ib_back;
    private ListView lv_good;
    private OrderGoodAdapter goodAdapter;
    private List<Good> goodData;
    private View headView;
    private TextView tv_order_num, tv_time;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_info;
    }

    @Override
    public void initView() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        lv_good = (ListView) findViewById(R.id.lv_good);
        headView = LayoutInflater.from(this).inflate(R.layout.order_info_header_view, null);
        tv_order_num = (TextView) headView.findViewById(R.id.tv_order_num);
        tv_time = (TextView) headView.findViewById(R.id.tv_time);
        lv_good.addHeaderView(headView, null, false);

        ib_back.setOnClickListener(this);
        lv_good.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        if (goodData == null) {
            goodData = new ArrayList<>();
        }
        goodAdapter = new OrderGoodAdapter(this);
        goodAdapter.setData(goodData);
        lv_good.setAdapter(goodAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Good good = (Good) goodAdapter.getItem(position);
        if (good == null) {
            return;
        }
        if(good.getFormulaList() != null && good.getFormulaList().size() > 0){
            FormulaPop pop = new FormulaPop(this, good, FormulaPop.TYPE_ORDER);
            pop.showPopup();
        }
    }
}
