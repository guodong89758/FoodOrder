package com.foodorder.activity;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.foodorder.R;
import com.foodorder.adapter.GoodsAdapter;
import com.foodorder.adapter.SelectAdapter;
import com.foodorder.adapter.TypeAdapter;
import com.foodorder.base.BaseActivity;
import com.foodorder.db.bean.Good;
import com.foodorder.db.bean.GoodType;
import com.foodorder.log.DLOG;
import com.foodorder.runtime.RT;
import com.foodorder.widget.DividerDecoration;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


public class GoodListActivity extends BaseActivity {

    private ImageView imgCart;
    private ViewGroup anim_mask_layout;
    private RecyclerView rvType, rvSelected;
    private TextView tvCount, tvCost, tvSubmit, tvTips;
    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet;
    private StickyListHeadersListView listView;

    private List<GoodType> goodTypeList;
    private List<Good> goodList;
    private SparseArray<Good> selectedList;
    private SparseIntArray groupSelect;

    private GoodsAdapter myAdapter;
    private SelectAdapter selectAdapter;
    private TypeAdapter typeAdapter;

    private NumberFormat nf;
    private Handler mHanlder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_good_list;
    }

    @Override
    public void initView() {
        tvCount = (TextView) findViewById(R.id.tvCount);
        tvCost = (TextView) findViewById(R.id.tvCost);
        tvTips = (TextView) findViewById(R.id.tvTips);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        rvType = (RecyclerView) findViewById(R.id.typeRecyclerView);

        imgCart = (ImageView) findViewById(R.id.imgCart);
        anim_mask_layout = (RelativeLayout) findViewById(R.id.containerLayout);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);

        listView = (StickyListHeadersListView) findViewById(R.id.itemListView);

        rvType.setLayoutManager(new LinearLayoutManager(this));
        rvType.addItemDecoration(new DividerDecoration(this));

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Good item = goodList.get(firstVisibleItem);
                if (typeAdapter != null && typeAdapter.selectTypeId != item.getPosition()) {
                    typeAdapter.selectTypeId = item.getPosition();
                    typeAdapter.notifyDataSetChanged();
                    rvType.smoothScrollToPosition(getSelectedGroupPosition(item.getPosition()));
                }
            }
        });
    }

    @Override
    public void initData() {
        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        mHanlder = new Handler(getMainLooper());
        selectedList = new SparseArray<>();
        groupSelect = new SparseIntArray();
        if (goodTypeList == null) {
            goodTypeList = new ArrayList<>();
        }
        if (goodList == null) {
            goodList = new ArrayList<>();
        }

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                goodTypeList = RT.ins().getDaoSession().getGoodTypeDao().loadAll();
                goodList = RT.ins().getDaoSession().getGoodDao().loadAll();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        typeAdapter = new TypeAdapter(GoodListActivity.this, goodTypeList);
                        rvType.setAdapter(typeAdapter);

                        myAdapter = new GoodsAdapter(goodList, GoodListActivity.this);
                        listView.setAdapter(myAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        DLOG.e(e.getMessage());
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });

    }

    public void playAnimation(int[] start_location) {
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.button_add);
        setAnim(img, start_location);
    }

    private Animation createAnim(int startX, int startY) {
        int[] des = new int[2];
        imgCart.getLocationInWindow(des);

        AnimationSet set = new AnimationSet(false);

        Animation translationX = new TranslateAnimation(0, des[0] - startX, 0, 0);
        translationX.setInterpolator(new LinearInterpolator());
        Animation translationY = new TranslateAnimation(0, 0, 0, des[1] - startY);
        translationY.setInterpolator(new AccelerateInterpolator());
        Animation alpha = new AlphaAnimation(1, 0.5f);
        set.addAnimation(translationX);
        set.addAnimation(translationY);
        set.addAnimation(alpha);
        set.setDuration(500);

        return set;
    }

    private void addViewToAnimLayout(final ViewGroup vg, final View view,
                                     int[] location) {

        int x = location[0];
        int y = location[1];
        int[] loc = new int[2];
        vg.getLocationInWindow(loc);
        view.setX(x);
        view.setY(y - loc[1]);
        vg.addView(view);
    }

    private void setAnim(final View v, int[] start_location) {

        addViewToAnimLayout(anim_mask_layout, v, start_location);
        Animation set = createAnim(start_location[0], start_location[1]);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                mHanlder.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        anim_mask_layout.removeView(v);
                    }
                }, 100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(set);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom:
                showBottomSheet();
                break;
            case R.id.clear:
                clearCart();
                break;
            case R.id.tvSubmit:
                Toast.makeText(GoodListActivity.this, "结算", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    //添加商品
    public void add(Good item, boolean refreshGoodList) {

        int groupCount = groupSelect.get(item.getPosition());
        if (groupCount == 0) {
            groupSelect.append(item.getPosition(), 1);
        } else {
            groupSelect.append(item.getPosition(), ++groupCount);
        }

        Good temp = selectedList.get(item.getId().intValue());
        if (temp == null) {
            item.setCount(1);
            selectedList.append(item.getId().intValue(), item);
        } else {
            temp.setCount(temp.getCount() + 1);
        }
        update(refreshGoodList);
    }

    //移除商品
    public void remove(Good item, boolean refreshGoodList) {

        int groupCount = groupSelect.get(item.getPosition());
        if (groupCount == 1) {
            groupSelect.delete(item.getPosition());
        } else if (groupCount > 1) {
            groupSelect.append(item.getPosition(), --groupCount);
        }

        Good temp = selectedList.get(item.getId().intValue());
        if (temp != null) {
            if (temp.getCount() < 2) {
                selectedList.remove(item.getId().intValue());
            } else {
                item.setCount(item.getCount() - 1);
                ;
            }
        }
        update(refreshGoodList);
    }

    //刷新布局 总价、购买数量等
    private void update(boolean refreshGoodList) {
        int size = selectedList.size();
        int count = 0;
        double cost = 0;
        for (int i = 0; i < size; i++) {
            Good item = selectedList.valueAt(i);
            count += item.getCount();
            cost += item.getCount() * item.getPrice();
        }

        if (count < 1) {
            tvCount.setVisibility(View.GONE);
        } else {
            tvCount.setVisibility(View.VISIBLE);
        }

        tvCount.setText(String.valueOf(count));

        if (cost > 99.99) {
            tvTips.setVisibility(View.GONE);
            tvSubmit.setVisibility(View.VISIBLE);
        } else {
            tvSubmit.setVisibility(View.GONE);
            tvTips.setVisibility(View.VISIBLE);
        }

        tvCost.setText(nf.format(cost));

        if (myAdapter != null && refreshGoodList) {
            myAdapter.notifyDataSetChanged();
        }
        if (selectAdapter != null) {
            selectAdapter.notifyDataSetChanged();
        }
        if (typeAdapter != null) {
            typeAdapter.notifyDataSetChanged();
        }
        if (bottomSheetLayout.isSheetShowing() && selectedList.size() < 1) {
            bottomSheetLayout.dismissSheet();
        }
    }

    //清空购物车
    public void clearCart() {
        selectedList.clear();
        groupSelect.clear();
        update(true);

    }

    //根据商品id获取当前商品的采购数量
    public int getSelectedItemCountById(int id) {
        Good temp = selectedList.get(id);
        if (temp == null) {
            return 0;
        }
        return temp.getCount();
    }

    //根据类别Id获取属于当前类别的数量
    public int getSelectedGroupCountByTypeId(int typeId) {
        return groupSelect.get(typeId);
    }

    //根据类别id获取分类的Position 用于滚动左侧的类别列表
    public int getSelectedGroupPosition(int typeId) {
        for (int i = 0; i < goodTypeList.size(); i++) {
            if (typeId == goodTypeList.get(i).getPosition()) {
                return i;
            }
        }
        return 0;
    }

    public void onTypeClicked(int typeId) {
        listView.setSelection(getSelectedPosition(typeId));
    }

    private int getSelectedPosition(int typeId) {
        int position = 0;
        for (int i = 0; i < goodList.size(); i++) {
            if (goodList.get(i).getPosition() == typeId) {
                position = i;
                break;
            }
        }
        return position;
    }

    private View createBottomSheetView() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet, (ViewGroup) getWindow().getDecorView(), false);
        rvSelected = (RecyclerView) view.findViewById(R.id.selectRecyclerView);
        rvSelected.setLayoutManager(new LinearLayoutManager(this));
        TextView clear = (TextView) view.findViewById(R.id.clear);
        clear.setOnClickListener(this);
        selectAdapter = new SelectAdapter(this, selectedList);
        rvSelected.setAdapter(selectAdapter);
        return view;
    }

    private void showBottomSheet() {
        if (bottomSheet == null) {
            bottomSheet = createBottomSheetView();
        }
        if (bottomSheetLayout.isSheetShowing()) {
            bottomSheetLayout.dismissSheet();
        } else {
            if (selectedList.size() != 0) {
                bottomSheetLayout.showWithSheetView(bottomSheet);
            }
        }
    }
}
