package com.foodorder.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.db.bean.Formula;
import com.foodorder.db.bean.Good;
import com.foodorder.pop.FormulaPop;
import com.foodorder.util.PhoneUtil;
import com.foodorder.util.ToastUtil;

import java.util.List;

/**
 * Created by guodong on 16/9/24.
 */

public class FormulaCell extends LinearLayout implements ListCell, View.OnClickListener {
    private RelativeLayout rl_title;
    private TextView tv_type, tv_max_count, tv_name, tv_add, tv_minus, tv_count;
    private Formula formula;
    private Good good;
    private int type;

    public FormulaCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_max_count = (TextView) findViewById(R.id.tv_max_count);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_minus = (TextView) findViewById(R.id.tv_minus);
        tv_count = (TextView) findViewById(R.id.tv_count);

        tv_add.setOnClickListener(this);
        tv_minus.setOnClickListener(this);
    }

    @Override
    public void onGetData(Object data, int position, BaseAdapter adapter) {
        if (data == null) {
            return;
        }
        formula = (Formula) data;
        String type_name = "";
        if (PhoneUtil.isZh()) {
            type_name = formula.getZh_type_name();
        } else {
            type_name = formula.getFr_type_name();
        }
        if (formula.getShow_title()) {
            rl_title.setVisibility(View.VISIBLE);
            tv_type.setText(type_name);
            tv_max_count.setText(getResources().getString(R.string.formula_max_count_desc, formula.getMax_choose()));
        } else {
            rl_title.setVisibility(View.GONE);
        }
        String name = "";
//        if (PhoneUtil.isZh()) {
//            name = formula.getZh_name();
//        } else {
        name = formula.getFr_name();
//        }
        tv_name.setText(name);
        tv_count.setText(String.valueOf(formula.getCount()));
        if(type == FormulaPop.TYPE_MENU){
            tv_add.setVisibility(View.VISIBLE);
        }else{

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                add();
                break;
            case R.id.tv_minus:
                remove();
                break;
        }
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public void add() {
        if (formula.getSel_count() == formula.getMax_choose()) {
            ToastUtil.showToast(getContext().getResources().getString(R.string.formula_max_count_desc, formula.getMax_choose()));
            return;
        }
        formula.setCount(formula.getCount() + 1);
        tv_count.setText(String.valueOf(formula.getCount()));
        List<Formula> formulaData = good.getFormulaList();
        for (int i = 0; i < formulaData.size(); i++) {
            Formula temp = formulaData.get(i);
            if (formula.getId_product_formula().equals(temp.getId_product_formula())) {
                temp.setSel_count(temp.getSel_count() + 1);
            }
        }
    }

    public void setType(int type){
        this.type = type;
    }

    public void remove() {
//        if (formula.getCount() < 1) {
//            return;
//        }
        formula.setCount(formula.getCount() - 1);
        tv_count.setText(String.valueOf(formula.getCount()));
        List<Formula> formulaData = good.getFormulaList();
        for (int i = 0; i < formulaData.size(); i++) {
            Formula temp = formulaData.get(i);
            if (formula.getId_product_formula().equals(temp.getId_product_formula())) {
                temp.setSel_count(temp.getSel_count() - 1);
            }
        }
    }
}
