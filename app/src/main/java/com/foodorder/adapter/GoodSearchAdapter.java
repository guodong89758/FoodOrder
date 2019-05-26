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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.contant.EventTag;
import com.foodorder.db.bean.Good;
import com.foodorder.dialog.SetupNumDialog;
import com.foodorder.logic.CartManager;
import com.foodorder.pop.AttributePop;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.util.BitmapLoader;
import com.foodorder.util.PhoneUtil;

import java.text.NumberFormat;
import java.util.List;


/**
 * Created by guodong on 16/9/25.
 */

public class GoodSearchAdapter extends RecyclerView.Adapter<GoodSearchAdapter.GoodViewHolder> {

    private Context mContext;
    List<Good> mDataList;
//    private Good item;

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
        return new GoodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_search, null));
    }

    @Override
    public void onBindViewHolder(GoodViewHolder holder, final int position) {
        final Good item = mDataList.get(position);
        if (item == null) {
            return;
        }
        holder.bindData(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(position, item);
                }
            }
        });

    }

    public class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView img;
        public TextView name, price, tv_code, tv_specification, tvCount;
        ImageButton tvMinus, tvAdd;
        private boolean hasAttribute = false;
        private boolean hasFormula = false;
        private NumberFormat nf;
        private Good item;

        public GoodViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.tvName);
            price = (TextView) itemView.findViewById(R.id.tvPrice);
            tv_code = (TextView) itemView.findViewById(R.id.tv_code);
            tvCount = (TextView) itemView.findViewById(R.id.count);
            tvMinus = (ImageButton) itemView.findViewById(R.id.tvMinus);
            tvAdd = (ImageButton) itemView.findViewById(R.id.tvAdd);
            tv_specification = (TextView) itemView.findViewById(R.id.tv_specification);

            itemView.setOnLongClickListener(this);
            tvMinus.setOnClickListener(this);
            tvAdd.setOnClickListener(this);
            tv_specification.setOnClickListener(this);

            nf = NumberFormat.getCurrencyInstance(RT.locale);
            nf.setMaximumFractionDigits(RT.PRICE_NUM);
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
//            item.setCount(CartManager.ins().getSelectedItemCountById(item.getId().intValue()));
            tvCount.setText(String.valueOf(item.getCount()));
            price.setText(nf.format(item.getPrice()));
            tvAdd.setVisibility(View.VISIBLE);
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

        @Override
        public boolean onLongClick(View v) {

            if ((item.getFormulaList() != null && item.getFormulaList().size() > 0) || (item.getAttributeList() != null && item.getAttributeList().size() > 0)) {
                if (item.getFormulaList() != null && item.getFormulaList().size() > 0) {
                    EventManager.ins().sendEvent(EventTag.POPUP_FORMULA_SHOW, 0, 0, item);
                } else if (item.getAttributeList() != null && item.getAttributeList().size() > 0) {
                    EventManager.ins().sendEvent(EventTag.POPUP_ATTRIBUTE_SHOW, 0, AttributePop.TYPE_MENU, item);
                }
            } else {
                showNumDialog(mContext, item);
            }
            return true;
        }

        private void setGoodNum(int num) {
            int count = CartManager.ins().getSelectedItemCountById(item.getId().intValue());
            if (count < 1) {
                tvCount.setVisibility(View.VISIBLE);
            }
            CartManager.ins().clearGood(item, false);
            for (int i = 0; i < num; i++) {
                CartManager.ins().add(item, true);
            }
            tvCount.setText(String.valueOf(item.getCount()) + "x");
        }

        private void showNumDialog(Context context, Good good) {
            if (good == null) {
                return;
            }
            SetupNumDialog dialog = new SetupNumDialog(context, good.getCount());
            dialog.setOnSetupNumListener(new SetupNumDialog.OnSetupNumListener() {
                @Override
                public void onGetNum(int num) {
                    setGoodNum(num);
                }
            });
            dialog.show();
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

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, Good good);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

}
