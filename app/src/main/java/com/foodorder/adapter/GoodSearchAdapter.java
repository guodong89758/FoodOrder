package com.foodorder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.db.bean.Good;
import com.foodorder.logic.CartManager;
import com.foodorder.util.PhoneUtil;

import java.util.List;


/**
 * Created by guodong on 16/9/25.
 */

public class GoodSearchAdapter extends RecyclerView.Adapter<GoodSearchAdapter.GoodViewHolder> {

    private Context mContext;
    List<Good> mDataList;
    private Good item;

    public GoodSearchAdapter(Context mContext, List<Good> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    @Override
    public GoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, null));
    }

    @Override
    public void onBindViewHolder(GoodViewHolder holder, int position) {
        item = mDataList.get(position);
        if (item == null) {
            return;
        }
        holder.bindData(item);

    }

    public static class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public TextView name, price, tv_code, tvAdd, tv_specification, tvMinus, tvCount;
        private Good item;
        private boolean hasAttribute = false;
        private boolean hasFormula = false;

        public GoodViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.tvName);
            price = (TextView) itemView.findViewById(R.id.tvPrice);
            tv_code = (TextView) itemView.findViewById(R.id.tv_code);
            tvCount = (TextView) itemView.findViewById(R.id.count);
            tvMinus = (TextView) itemView.findViewById(R.id.tvMinus);
            tvAdd = (TextView) itemView.findViewById(R.id.tvAdd);
            tv_specification = (TextView) itemView.findViewById(R.id.tv_specification);

            tvMinus.setOnClickListener(this);
            tvAdd.setOnClickListener(this);
            tv_specification.setOnClickListener(this);
        }

        public void bindData(Good item) {
            this.item = item;
            String good_name = "";
            if (PhoneUtil.isZh()) {
                good_name = item.getZh_name();
            } else {
                good_name = item.getFr_name();
            }
            name.setText(good_name);
            tv_code.setText(item.getReference());
//            item.setCount(CartManager.ins().getSelectedItemCountById(item.getId().intValue()));
            tvCount.setText(String.valueOf(item.getCount()));
            price.setText(String.valueOf(item.getPrice()));
            if (item.getCount() < 1) {
                tvCount.setVisibility(View.GONE);
                tvMinus.setVisibility(View.GONE);
            } else {
                tvCount.setVisibility(View.VISIBLE);
                tvMinus.setVisibility(View.VISIBLE);
            }
//            if (item.getFormulaList() != null && item.getFormulaList().size() > 0) {
//                hasFormula = true;
//            } else {
//                hasFormula = false;
//            }
//            if (item.getAttributeList() != null && item.getAttributeList().size() > 0) {
//                hasAttribute = true;
//            } else {
//                hasAttribute = false;
//            }
//            if (hasFormula || hasAttribute) {
//                tv_specification.setVisibility(View.VISIBLE);
//                tvAdd.setVisibility(View.GONE);
//            } else {
//                tv_specification.setVisibility(View.GONE);
//                tvAdd.setVisibility(View.VISIBLE);
//            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvAdd: {
//                    int count = CartManager.ins().getSelectedItemCountById(item.getId().intValue());
//                    if (count < 1) {
//                        tvMinus.setAnimation(getShowAnimation());
//                        tvMinus.setVisibility(View.VISIBLE);
//                        tvCount.setVisibility(View.VISIBLE);
//                    }
//                    count++;
//                    CartManager.ins().add(item, true);
//                    tvCount.setText(String.valueOf(item.getCount()));
                    CartManager.ins().cartAdd(item, true);
//                    EventManager.ins().sendEvent(EventTag.GOOD_SEARCH_LIST_REFRESH, 0, 0, null);
                }
                break;
                case R.id.tvMinus: {
//                    int count = CartManager.ins().getSelectedItemCountById(item.getId().intValue());
//                    if (count < 2) {
//                        tvMinus.setAnimation(getHiddenAnimation());
//                        tvMinus.setVisibility(View.GONE);
//                        tvCount.setVisibility(View.GONE);
//                    }
//                    count--;
//                    CartManager.ins().remove(item, true);
//                    tvCount.setText(String.valueOf(item.getCount()));
                    CartManager.ins().cartRemove(item, true);
//                    EventManager.ins().sendEvent(EventTag.GOOD_SEARCH_LIST_REFRESH, 0, 0, null);
                }
                break;
                case R.id.tv_specification:
//                    ToastUtil.showToast("规格");
                    break;
            }
        }

        private Animation getShowAnimation() {
            AnimationSet set = new AnimationSet(true);
            RotateAnimation rotate = new RotateAnimation(0, 720, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            set.addAnimation(rotate);
            TranslateAnimation translate = new TranslateAnimation(
                    TranslateAnimation.RELATIVE_TO_SELF, 2f
                    , TranslateAnimation.RELATIVE_TO_SELF, 0
                    , TranslateAnimation.RELATIVE_TO_SELF, 0
                    , TranslateAnimation.RELATIVE_TO_SELF, 0);
            set.addAnimation(translate);
            AlphaAnimation alpha = new AlphaAnimation(0, 1);
            set.addAnimation(alpha);
            set.setDuration(500);
            return set;
        }

        private Animation getHiddenAnimation() {
            AnimationSet set = new AnimationSet(true);
            RotateAnimation rotate = new RotateAnimation(0, 720, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            set.addAnimation(rotate);
            TranslateAnimation translate = new TranslateAnimation(
                    TranslateAnimation.RELATIVE_TO_SELF, 0
                    , TranslateAnimation.RELATIVE_TO_SELF, 2f
                    , TranslateAnimation.RELATIVE_TO_SELF, 0
                    , TranslateAnimation.RELATIVE_TO_SELF, 0);
            set.addAnimation(translate);
            AlphaAnimation alpha = new AlphaAnimation(1, 0);
            set.addAnimation(alpha);
            set.setDuration(500);
            return set;
        }
    }


}
