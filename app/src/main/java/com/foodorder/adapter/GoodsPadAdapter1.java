package com.foodorder.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.CycleInterpolator;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
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
import com.foodorder.widget.stickygrid.StickyGridHeadersSimpleAdapter;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by guodong on 2017/5/14.
 */

public class GoodsPadAdapter1 extends BaseAdapter implements StickyGridHeadersSimpleAdapter {
    private Context mContext;
    public NumberFormat nf;
    private LayoutInflater mInflater;
    private List<Good> goodList;

    public GoodsPadAdapter1(Context context, List<Good> goodList) {
        this.mContext = context;
        this.goodList = goodList;
        nf = NumberFormat.getCurrencyInstance(RT.locale);
        nf.setMaximumFractionDigits(RT.PRICE_NUM);
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public long getHeaderId(int position) {
        return goodList.get(position).getPosition();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_header_view, parent, false);
        }
        String type_name = "";
        if (PhoneUtil.isZh()) {
            type_name = goodList.get(position).getZh_category_name();
        } else {
            type_name = goodList.get(position).getFr_category_name();
        }
        ((TextView) (convertView)).setText(type_name);
        return convertView;
    }

    @Override
    public int getCount() {
        if (goodList == null) {
            return 0;
        }
        return goodList.size();
    }

    @Override
    public Object getItem(int position) {
        if (goodList == null) {
            return null;
        }
        return goodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoodViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_goods_pad, parent, false);
            holder = new GoodViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GoodViewHolder) convertView.getTag();
        }
        Good item = goodList.get(position);
        holder.bindData(item);
        return convertView;
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

            AbsListView.LayoutParams params = (AbsListView.LayoutParams) itemView.getLayoutParams();
            params.width = (RT.getScreenWidth() - PhoneUtil.dipToPixel(100, mContext)) / 3;

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
                        CartManager.ins().add(item, false);
                        tvCount.setText(String.valueOf(item.getCount()) + "x");
                        AnimatorSet animatorSet = createAnimator(tvCount);
                        animatorSet.start();
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

        private AnimatorSet createAnimator(View view) {
            ObjectAnimator num_anim_x = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.5f);
            num_anim_x.setDuration(50);
            ObjectAnimator num_anim_y = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.5f);
            num_anim_y.setDuration(50);
            ObjectAnimator num_anim_x1 = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1.0f);
            num_anim_x1.setInterpolator(new AnticipateOvershootInterpolator());
            num_anim_x1.setDuration(50);
            ObjectAnimator num_anim_y1 = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.0f);
            num_anim_y1.setInterpolator(new AnticipateOvershootInterpolator());
            num_anim_y1.setDuration(50);
            ObjectAnimator num_anim_x2 = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.1f);
            num_anim_x2.setInterpolator(new CycleInterpolator(1f));
            num_anim_x2.setDuration(50);
            ObjectAnimator num_anim_y2 = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.1f);
            num_anim_y2.setInterpolator(new CycleInterpolator(1f));
            num_anim_y2.setDuration(50);

            AnimatorSet animator = new AnimatorSet();
            animator.play(num_anim_x).with(num_anim_y);
            animator.play(num_anim_y).before(num_anim_x1);
            animator.play(num_anim_x1).with(num_anim_y1);
            animator.play(num_anim_y1).before(num_anim_x2);
            animator.play(num_anim_x2).with(num_anim_y2);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            return animator;
        }
    }
}
