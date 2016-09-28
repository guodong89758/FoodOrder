package com.foodorder.activity;

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
import com.foodorder.base.BaseActivity;

public class OrderEatinSearchActivity extends BaseActivity {
    private ImageButton ib_clear;
    private Button btn_back;
    private EditText et_search;
    private RecyclerView rv_order;

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
                if(actionId == EditorInfo.IME_ACTION_SEARCH){

                }
                return true;
            }
        });

        et_search.addTextChangedListener(textWatcher);
    }

    @Override
    public void initData() {

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
            if(s.length() > 0){
                ib_clear.setVisibility(View.VISIBLE);
            }else{
                ib_clear.setVisibility(View.GONE);
            }
        }
    };
}
