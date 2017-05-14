package com.foodorder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.contant.EventTag;
import com.foodorder.db.bean.Good;
import com.foodorder.logic.CartManager;
import com.foodorder.pop.AttributePop;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.util.BitmapLoader;
import com.foodorder.util.PhoneUtil;
import com.foodorder.widget.SwipeMenuLayout;
import com.tonicartos.superslim.GridSLM;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by guodong on 2017/5/14.
 */

public class GoodsPadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TITLE = 1;
    private static final int TYPE_CONTENT = 2;
    private Context mContext;
    public NumberFormat nf;
    private List<Good> goodList;

    public GoodsPadAdapter(Context context, List<Good> goodList) {
        this.mContext = context;
        this.goodList = goodList;
        nf = NumberFormat.getCurrencyInstance(RT.locale);
        nf.setMaximumFractionDigits(RT.PRICE_NUM);
    }

    @Override
    public int getItemViewType(int position) {
        return goodList.get(position).isTitle() ? TYPE_TITLE : TYPE_CONTENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == TYPE_TITLE) {
            holder = new TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_view, parent, false));
        } else if (viewType == TYPE_CONTENT) {
            holder = new GoodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_pad, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        Good good = goodList.get(position);
        if (good == null) {
            return;
        }

        GridSLM.LayoutParams params = GridSLM.LayoutParams.from(mHolder.itemView.getLayoutParams());
        if (getItemViewType(position) == TYPE_TITLE) {
            TitleViewHolder holder = (TitleViewHolder) mHolder;
            String type_name = "";
            if (PhoneUtil.isZh()) {
                type_name = good.getZh_category_name();
            } else {
                type_name = good.getFr_category_name();
            }
            ((TextView) holder.itemView).setText(type_name);
//            GridSLM.LayoutParams params = GridSLM.LayoutParams.from(holder.itemView.getLayoutParams());
//            params.headerDisplay =  LayoutManager.LayoutParams.HEADER_STICKY;
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            params.headerEndMarginIsAuto = false;
//            params.headerStartMarginIsAuto = false;
//            params.setSlm(good.getSectionManager());
//            params.setFirstPosition(good.getSectionFirstPosition());
//            holder.itemView.setLayoutParams(params);

        } else if (getItemViewType(position) == TYPE_CONTENT) {
            GoodViewHolder holder = (GoodViewHolder) mHolder;
            holder.bindData(good);
            params.width = (RT.getScreenWidth() - PhoneUtil.dipToPixel(100, mContext)) / 3;

        }
        params.setSlm(good.getSectionManager());
        params.setNumColumns(3);
        params.setFirstPosition(good.getSectionFirstPosition());
        mHolder.itemView.setLayoutParams(params);


    }

    @Override
    public int getItemCount() {
        if (goodList == null) {
            return 0;
        }
        return goodList.size();
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {

        public TitleViewHolder(View itemView) {
            super(itemView);
        }
    }

    class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SwipeMenuLayout swipe_layout;
        LinearLayout ll_content;
        ImageView img;
        TextView name, price, tv_code, tvCount;
        Button btn_delete;
        Good item;

        public GoodViewHolder(View itemView) {
            super(itemView);
            swipe_layout = (SwipeMenuLayout) itemView.findViewById(R.id.swipe_layout);
            ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);
            img = (ImageView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.tvName);
            price = (TextView) itemView.findViewById(R.id.tvPrice);
            tv_code = (TextView) itemView.findViewById(R.id.tv_code);
            tvCount = (TextView) itemView.findViewById(R.id.count);
            btn_delete = (Button) itemView.findViewById(R.id.btn_delete);

            ll_content.setOnClickListener(this);
            btn_delete.setOnClickListener(this);
        }

        public void bindData(Good item) {
            this.item = item;
            BitmapLoader.ins().loadImage(item.getImage_url(), R.drawable.ic_def_image, img);
            String good_name = "";
            if (PhoneUtil.isZh()) {
                good_name = item.getZh_name();
            } else {
                good_name = item.getFr_name();
            }
            name.setText(good_name);
            tv_code.setText(item.getReference());
            item.setCount(CartManager.ins().getSelectedItemCountById(item.getId().intValue()));
            tvCount.setText(String.valueOf(item.getCount()) + "x");
            price.setText(nf.format(item.getPrice()));
            if (item.getCount() < 1) {
                tvCount.setVisibility(View.GONE);
                swipe_layout.setSwipeEnable(false);
            } else {
                tvCount.setVisibility(View.VISIBLE);
                swipe_layout.setSwipeEnable(true);
            }

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_content:
                    swipe_layout.setSwipeEnable(true);
                    if ((item.getFormulaList() != null && item.getFormulaList().size() > 0) || (item.getAttributeList() != null && item.getAttributeList().size() > 0)) {
                        if (item.getFormulaList() != null && item.getFormulaList().size() > 0) {
                            EventManager.ins().sendEvent(EventTag.POPUP_FORMULA_SHOW, 0, 0, item);
                        } else if (item.getAttributeList() != null && item.getAttributeList().size() > 0) {
                            EventManager.ins().sendEvent(EventTag.POPUP_ATTRIBUTE_SHOW, 0, AttributePop.TYPE_MENU, item);
                        }
                    } else {
                        int count = CartManager.ins().getSelectedItemCountById(item.getId().intValue());
                        if (count < 1) {
                            tvCount.setVisibility(View.VISIBLE);
                        }
                        CartManager.ins().add(item, true);
                        tvCount.setText(String.valueOf(item.getCount()) + "x");
                    }

                    break;
                case R.id.btn_delete:
                    swipe_layout.setSwipeEnable(false);
                    swipe_layout.smoothClose();
                    CartManager.ins().clearGood(item, false);
                    item.setCount(0);
                    tvCount.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }
}
