package com.foodorder.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.foodorder.R;
import com.foodorder.adapter.PrinterAdapter;
import com.foodorder.base.BaseRecyclerAdapter;
import com.foodorder.contant.AppKey;
import com.foodorder.entry.Printer;
import com.foodorder.util.PhoneUtil;
import com.foodorder.widget.HorizontalDividerItemDecoration;

import java.util.List;


public class PrinterDialog extends Dialog implements View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener {

    private OnCheckListener listener;
    private Context context;
    private RecyclerView rv_printer;
    private LinearLayout ll_bottom;
    private Button btn_ok;
    private PrinterAdapter printerAdapter;
    private int fromType;
    private List<Printer> printerList;

    public PrinterDialog(Context context, int fromType, List<Printer> printerList) {
        super(context, R.style.NormalDialog);
        setCanceledOnTouchOutside(true);
        getWindow().getAttributes().width = -2;
        getWindow().getAttributes().height = -2;
        getWindow().getAttributes().y = 0;
        getWindow().setGravity(Gravity.CENTER_VERTICAL);
        getWindow().setAttributes(getWindow().getAttributes());
        if (context instanceof Activity)
            setOwnerActivity((Activity) context);
        this.context = context;
        this.fromType = fromType;
        this.printerList = printerList;

        setContentView(R.layout.dialog_printer);
        rv_printer = (RecyclerView) findViewById(R.id.rv_printer);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        if (fromType == AppKey.PRINTER_CUIDAN) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rv_printer.getLayoutParams();
            params.height = PhoneUtil.dipToPixel(225, context);
            ll_bottom.setVisibility(View.VISIBLE);
        } else if (fromType == AppKey.PRINTER_DAYIN) {
            if(printerList != null && printerList.size() > 5){
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rv_printer.getLayoutParams();
                params.height = PhoneUtil.dipToPixel(225, context);
                ll_bottom.setVisibility(View.VISIBLE);
            }
            ll_bottom.setVisibility(View.GONE);
        }
        rv_printer.setLayoutManager(new LinearLayoutManager(context));
        rv_printer.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context).color(ContextCompat.getColor(context, R.color.black_10)).size(1).build());
        rv_printer.setHasFixedSize(true);
        printerAdapter = new PrinterAdapter(context, printerList, fromType);
        printerAdapter.setOnItemClickListener(this);
        rv_printer.setAdapter(printerAdapter);
    }


    public void setOnCheckListener(OnCheckListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(View view, int position, long id) {
        Printer printer = printerList.get(position);
        if (printer == null) {
            return;
        }
        if (fromType == AppKey.PRINTER_CUIDAN) {
            if (printer.isChecked()) {
                printer.setChecked(false);
            } else {
                printer.setChecked(true);
            }
            printerAdapter.notifyItemChanged(position);
        } else if (fromType == AppKey.PRINTER_DAYIN) {
            if (listener != null) {
                listener.onCheck(fromType, printer.getId());
            }
            this.dismiss();
        }
    }


    public interface OnCheckListener {
        void onCheck(int type, String printer);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok) {
            if (listener != null) {
                listener.onCheck(fromType, getPrinters());
            }
            this.dismiss();
        }
    }

    private String getPrinters() {
        String printers = "";
        for (int i = 0; i < printerList.size(); i++) {
            Printer printer = printerList.get(i);
            if (printer.isChecked()) {
                if (TextUtils.isEmpty(printers)) {
                    printers = printer.getName();
                } else {
                    printers = printers + "," + printer.getName();
                }
            }
        }
        return printers;
    }
}
