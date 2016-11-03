package com.foodorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.activity.GoodListActivity;
import com.foodorder.contant.EventTag;
import com.foodorder.db.bean.Good;
import com.foodorder.logic.CartManager;
import com.foodorder.pop.AttributePop;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.util.BitmapLoader;
import com.foodorder.util.PhoneUtil;

import java.text.NumberFormat;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


public class GoodsAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<Good> dataList;
    private GoodListActivity mContext;
    private NumberFormat nf;
    private LayoutInflater mInflater;
    private boolean hasAttribute = false;
    private boolean hasFormula = false;

    public GoodsAdapter(List<Good> dataList, GoodListActivity mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
        nf = NumberFormat.getCurrencyInstance(RT.locale);
        nf.setMaximumFractionDigits(RT.PRICE_NUM);
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_header_view, parent, false);
        }
        String type_name = "";
        if (PhoneUtil.isZh()) {
            type_name = dataList.get(position).getZh_category_name();
        } else {
            type_name = dataList.get(position).getFr_category_name();
        }
        ((TextView) (convertView)).setText(type_name);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return dataList.get(position).getPosition();
    }

    @Override
    public int getCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_goods, parent, false);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }
        Good item = dataList.get(position);
        holder.bindData(item);
        return convertView;
    }

    class ItemViewHolder implements View.OnClickListener {
        private ImageView img;
        private TextView name, price, tv_code, tvAdd, tv_specification, tvMinus, tvCount;
        private Good item;

        public ItemViewHolder(View itemView) {
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
            tvCount.setText(String.valueOf(item.getCount()));
            price.setText(nf.format(item.getPrice()));
            if (item.getCount() < 1) {
                tvCount.setVisibility(View.GONE);
                tvMinus.setVisibility(View.GONE);
            } else {
                tvCount.setVisibility(View.VISIBLE);
                tvMinus.setVisibility(View.VISIBLE);
            }
            if (item.getFormulaList() != null && item.getFormulaList().size() > 0) {
                hasFormula = true;
            } else {
                hasFormula = false;
            }
            if (item.getAttributeList() != null && item.getAttributeList().size() > 0) {
                hasAttribute = true;
            } else {
                hasAttribute = false;
            }
            if (hasFormula || hasAttribute) {
                tv_specification.setVisibility(View.VISIBLE);
                tvAdd.setVisibility(View.GONE);
            } else {
                tv_specification.setVisibility(View.GONE);
                tvAdd.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onClick(View v) {
            GoodListActivity activity = mContext;
            switch (v.getId()) {
                case R.id.tvAdd: {
                    int count = CartManager.ins().getSelectedItemCountById(item.getId().intValue());
                    if (count < 1) {
                        tvMinus.setAnimation(getShowAnimation());
                        tvMinus.setVisibility(View.VISIBLE);
                        tvCount.setVisibility(View.VISIBLE);
                    }
                    CartManager.ins().add(item, false);
                    count++;
                    tvCount.setText(String.valueOf(item.getCount()));
                    int[] loc = new int[2];
                    v.getLocationInWindow(loc);
                    activity.playAnimation(loc);
                }
                break;
                case R.id.tvMinus: {
                    int count = CartManager.ins().getSelectedItemCountById(item.getId().intValue());
                    if (count < 2) {
                        tvMinus.setAnimation(getHiddenAnimation());
                        tvMinus.setVisibility(View.GONE);
                        tvCount.setVisibility(View.GONE);
                    }
                    count--;
                    CartManager.ins().remove(item, false);//activity.getSelectedItemCountById(item.id)
                    tvCount.setText(String.valueOf(item.getCount()));

                }
                break;
                case R.id.tv_specification:
                    if (item.getFormulaList() != null && item.getFormulaList().size() > 0) {
                        EventManager.ins().sendEvent(EventTag.POPUP_FORMULA_SHOW, 0, 0, item);
                    } else if (item.getAttributeList() != null && item.getAttributeList().size() > 0) {
                        EventManager.ins().sendEvent(EventTag.POPUP_ATTRIBUTE_SHOW, 0, AttributePop.TYPE_MENU, item);
                    }

                    break;
                default:
                    break;
            }
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
