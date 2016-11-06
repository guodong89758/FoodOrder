package com.foodorder.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.foodorder.R;
import com.foodorder.adapter.GoodsAdapter;
import com.foodorder.adapter.SelectAdapter;
import com.foodorder.adapter.TypeAdapter;
import com.foodorder.base.BaseActivity;
import com.foodorder.contant.AppKey;
import com.foodorder.contant.EventTag;
import com.foodorder.db.bean.Good;
import com.foodorder.db.bean.GoodType;
import com.foodorder.dialog.NormalDialog;
import com.foodorder.log.DLOG;
import com.foodorder.logic.CartManager;
import com.foodorder.pop.AttributePop;
import com.foodorder.pop.FormulaPop;
import com.foodorder.pop.OrderSetupPop;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.event.EventListener;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.server.api.API_Food;
import com.foodorder.server.callback.JsonResponseCallback;
import com.foodorder.util.PhoneUtil;
import com.foodorder.util.ToastUtil;
import com.foodorder.widget.HorizontalDividerItemDecoration;
import com.lzy.okhttputils.OkHttpUtils;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.foodorder.contant.EventTag.ACTIVITY_FINISH;
import static com.foodorder.contant.EventTag.POPUP_FORMULA_SHOW;


public class GoodListActivity extends BaseActivity {
    private static final String TAG = "GoodListActivity";
    private ImageButton ib_back, ib_search;
    private RelativeLayout rl_cart;
    private ImageView imgCart, iv_cart_empty;
    private ViewGroup anim_mask_layout;
    private RecyclerView rvType, rvSelected;
    private TextView tvCount, tvCost;
    private Button btn_send;
    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet;
    private StickyListHeadersListView listView;
    private CheckBox cb_pack;

    private List<GoodType> goodTypeList;
    private List<Good> goodList;
    private SparseIntArray groupSelect;

    private GoodsAdapter myAdapter;
    private SelectAdapter selectAdapter;
    private TypeAdapter typeAdapter;

    private NumberFormat nf;
    private Handler mHanlder;
    private int list_type = AppKey.GOOD_LIST_MENU;
    private String id_order = "";
    private String number = "";
    private String persons = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_good_list;
    }

    @Override
    public void initView() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_search = (ImageButton) findViewById(R.id.ib_search);
        rl_cart = (RelativeLayout) findViewById(R.id.rl_cart);
        iv_cart_empty = (ImageView) findViewById(R.id.iv_cart_empty);
        tvCount = (TextView) findViewById(R.id.tvCount);
        tvCost = (TextView) findViewById(R.id.tvCost);
        btn_send = (Button) findViewById(R.id.btn_send);
        rvType = (RecyclerView) findViewById(R.id.typeRecyclerView);
        imgCart = (ImageView) findViewById(R.id.imgCart);
        anim_mask_layout = (RelativeLayout) findViewById(R.id.containerLayout);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);
        listView = (StickyListHeadersListView) findViewById(R.id.itemListView);
        cb_pack = (CheckBox) findViewById(R.id.cb_pack);

        ib_back.setOnClickListener(this);
        ib_search.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        rvType.setLayoutManager(new LinearLayoutManager(this));
        rvType.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(getResources().getColor(R.color.black_10)).size(1).build());

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (goodList != null && goodList.size() > 0) {
                    Good item = goodList.get(firstVisibleItem);
                    if (typeAdapter != null && typeAdapter.selectTypeId != item.getPosition()) {
                        typeAdapter.selectTypeId = item.getPosition();
                        typeAdapter.notifyDataSetChanged();
                        rvType.smoothScrollToPosition(getSelectedGroupPosition(item.getPosition()));
                    }
                }
            }
        });
        imgCart.setVisibility(View.GONE);
        EventManager.ins().registListener(EventTag.GOOD_LIST_REFRESH, eventListener);

        cb_pack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CartManager.ins().isPack = true;
                } else {
                    CartManager.ins().isPack = false;
                }
            }
        });
        EventManager.ins().registListener(ACTIVITY_FINISH, eventListener);
    }

    @Override
    public void initData() {
        if (getIntent() != null) {
            list_type = getIntent().getIntExtra(AppKey.GOOD_LIST_TYPE, AppKey.GOOD_LIST_MENU);
            id_order = getIntent().getStringExtra(AppKey.ID_ORDER);
            number = getIntent().getStringExtra(AppKey.ORDER_NUMBER);
            persons = getIntent().getStringExtra(AppKey.ORDER_PERSON);
        }
        nf = NumberFormat.getCurrencyInstance(RT.locale);
        nf.setMaximumFractionDigits(RT.PRICE_NUM);
        mHanlder = new Handler(getMainLooper());
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

    @Override
    protected void onResume() {
        super.onResume();
        EventManager.ins().registListener(POPUP_FORMULA_SHOW, eventListener);
        EventManager.ins().registListener(EventTag.POPUP_ATTRIBUTE_SHOW, eventListener);
        if (CartManager.ins().isPack) {
            cb_pack.setChecked(true);
        } else {
            cb_pack.setChecked(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventManager.ins().removeListener(POPUP_FORMULA_SHOW, eventListener);
        EventManager.ins().removeListener(EventTag.POPUP_ATTRIBUTE_SHOW, eventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CartManager.ins().clear();
        EventManager.ins().removeListener(EventTag.GOOD_LIST_REFRESH, eventListener);
        EventManager.ins().removeListener(EventTag.ACTIVITY_FINISH, eventListener);
        OkHttpUtils.getInstance().cancelTag(TAG);
    }

    EventListener eventListener = new EventListener() {
        @Override
        public void handleMessage(int what, int arg1, int arg2, Object dataobj) {
            switch (what) {
                case EventTag.GOOD_LIST_REFRESH:
                    update((Boolean) dataobj);
                    break;
                case EventTag.POPUP_FORMULA_SHOW:
                    FormulaPop formulaPop = new FormulaPop(GoodListActivity.this, (Good) dataobj, FormulaPop.TYPE_MENU);
                    formulaPop.showPopup();
                    break;
                case EventTag.POPUP_ATTRIBUTE_SHOW:
                    AttributePop attrPop = new AttributePop(GoodListActivity.this, (Good) dataobj, arg2);
                    attrPop.showPopup();
                    break;
                case EventTag.ACTIVITY_FINISH:
                    finish();
                    break;
            }
        }
    };

    public void playAnimation(int[] start_location) {
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.button_add);
        setAnim(img, start_location);
    }

    private AnimatorSet createAnim(View v, int startX, int startY) {
        int[] des = new int[2];
        rl_cart.getLocationInWindow(des);

        int offset = PhoneUtil.dipToPixel(20, this);

        ObjectAnimator image_transX = ObjectAnimator.ofFloat(v, "translationX", startX, des[0] + offset);
        image_transX.setInterpolator(new LinearInterpolator());
        ObjectAnimator image_transY = ObjectAnimator.ofFloat(v, "translationY", startY - offset, des[1]);
        image_transY.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 1f, 0.3f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.play(image_transX).with(image_transY).with(alpha);
        return set;
    }

    private AnimatorSet createAnimator() {
        ObjectAnimator num_anim_x = ObjectAnimator.ofFloat(rl_cart, "scaleX", 1.0f, 0.7f);
        num_anim_x.setDuration(100);
        ObjectAnimator num_anim_y = ObjectAnimator.ofFloat(rl_cart, "scaleY", 1.0f, 0.7f);
        num_anim_y.setDuration(100);
        ObjectAnimator num_anim_x1 = ObjectAnimator.ofFloat(rl_cart, "scaleX", 0.7f, 1.0f);
        num_anim_x1.setInterpolator(new AnticipateOvershootInterpolator());
        num_anim_x1.setDuration(100);
        ObjectAnimator num_anim_y1 = ObjectAnimator.ofFloat(rl_cart, "scaleY", 0.7f, 1.0f);
        num_anim_y1.setInterpolator(new AnticipateOvershootInterpolator());
        num_anim_y1.setDuration(100);
        ObjectAnimator num_anim_x2 = ObjectAnimator.ofFloat(rl_cart, "scaleX", 1.0f, 1.1f);
        num_anim_x2.setInterpolator(new CycleInterpolator(1f));
        num_anim_x2.setDuration(100);
        ObjectAnimator num_anim_y2 = ObjectAnimator.ofFloat(rl_cart, "scaleY", 1.0f, 1.1f);
        num_anim_y2.setInterpolator(new CycleInterpolator(1f));
        num_anim_y2.setDuration(100);

        AnimatorSet animator = new AnimatorSet();
        animator.play(num_anim_x).with(num_anim_y);
        animator.play(num_anim_y).before(num_anim_x1);
        animator.play(num_anim_x1).with(num_anim_y1);
        animator.play(num_anim_y1).before(num_anim_x2);
        animator.play(num_anim_x2).with(num_anim_y2);
        return animator;
    }

    private void addViewToAnimLayout(final ViewGroup vg, View view,
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
        AnimatorSet set = createAnim(v, start_location[0], start_location[1]);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHanlder.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        anim_mask_layout.removeView(v);
                    }
                }, 100);
                AnimatorSet animatorSet = createAnimator();
                animatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
//        Animation set = createAnim(v, start_location[0], start_location[1]);
//        set.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(final Animation animation) {
//                mHanlder.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        anim_mask_layout.removeView(v);
//                    }
//                }, 100);
//                AnimatorSet animatorSet = createAnimator();
//                animatorSet.start();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        v.startAnimation(set);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                onBackPressed();
                break;
            case R.id.ib_search:
                Intent intent = new Intent(this, GoodSearchActivity.class);
                intent.putExtra(AppKey.GOOD_LIST_TYPE, AppKey.GOOD_LIST_ADD);
                intent.putExtra(AppKey.ID_ORDER, id_order);
                intent.putExtra(AppKey.ORDER_NUMBER, number);
                intent.putExtra(AppKey.ORDER_PERSON, persons);
                startActivity(intent);
                break;
            case R.id.bottom:
                showBottomSheet();
                break;
            case R.id.clear:
                NormalDialog dialog = new NormalDialog(this);
                dialog.setTitle(R.string.cart_clear_dialog_title);
                dialog.setTextDes(getString(R.string.cart_clear_dialog_desc));
                dialog.setButton1(getString(R.string.action_cancel), new NormalDialog.DialogButtonOnClickListener() {
                    @Override
                    public void onClick(View button, NormalDialog dialog) {
                        dialog.dismiss();
                    }
                });
                dialog.setButton2(getString(R.string.action_ok), new NormalDialog.DialogButtonOnClickListener() {
                    @Override
                    public void onClick(View button, NormalDialog dialog) {
                        clearCart();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.btn_send:
                if (TextUtils.isEmpty(id_order)) {
                    OrderSetupPop setupPop = new OrderSetupPop(GoodListActivity.this, id_order);
                    setupPop.showPopup();
                } else {
//                    DLOG.json(CartManager.ins().getOrderGoodJson(false, id_order, number, persons));
                    showOrderGoodDialog(this, id_order, number, persons);
                }

                break;
            default:
                break;
        }
    }

//    //添加商品
//    public void add(Good item, boolean refreshGoodList) {
//
//        int groupCount = groupSelect.get(item.getPosition());
//        if (groupCount == 0) {
//            groupSelect.append(item.getPosition(), 1);
//        } else {
//            groupSelect.append(item.getPosition(), ++groupCount);
//        }
//
//        Good temp = CartManager.ins().cartList.get(item.getId().intValue());
//        if (temp == null) {
//            item.setCount(1);
//            CartManager.ins().cartList.append(item.getId().intValue(), item);
//        } else {
//            temp.setCount(temp.getCount() + 1);
//        }
//        update(refreshGoodList);
//    }
//
//    //移除商品
//    public void remove(Good item, boolean refreshGoodList) {
//
//        int groupCount = groupSelect.get(item.getPosition());
//        if (groupCount == 1) {
//            groupSelect.delete(item.getPosition());
//        } else if (groupCount > 1) {
//            groupSelect.append(item.getPosition(), --groupCount);
//        }
//
//        Good temp = CartManager.ins().cartList.get(item.getId().intValue());
//        if (temp != null) {
//            if (temp.getCount() < 2) {
//                CartManager.ins().cartList.remove(item.getId().intValue());
//            } else {
//                item.setCount(item.getCount() - 1);
//            }
//        }
//        update(refreshGoodList);
//    }

    //刷新布局 总价、购买数量等
    private void update(boolean refreshGoodList) {
        int size = CartManager.ins().cartList.size();
        int count = 0;
        double cost = 0;
        for (int i = 0; i < size; i++) {
            Good item = CartManager.ins().cartList.valueAt(i);
            count += item.getCount();
            cost += item.getCount() * item.getPrice();
        }

        if (count < 1) {
            tvCount.setVisibility(View.GONE);
            imgCart.setVisibility(View.GONE);
        } else {
            tvCount.setVisibility(View.VISIBLE);
            imgCart.setVisibility(View.VISIBLE);
        }

        tvCount.setText(String.valueOf(count));

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
        if (bottomSheetLayout.isSheetShowing() && CartManager.ins().cartList.size() < 1) {
            bottomSheetLayout.dismissSheet();
        }
    }

    //清空购物车
    public void clearCart() {
        CartManager.ins().clear();
        update(true);

    }

    //    //根据商品id获取当前商品的采购数量
//    public int getSelectedItemCountById(int id) {
//        Good temp = CartManager.ins().cartList.get(id);
//        if (temp == null) {
//            return 0;
//        }
//        return temp.getCount();
//    }
//
//    //根据类别Id获取属于当前类别的数量
//    public int getSelectedGroupCountByTypeId(int typeId) {
//        return groupSelect.get(typeId);
//    }
//
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
        rvSelected.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(getResources().getColor(R.color.black_10)).size(1).build());
        TextView clear = (TextView) view.findViewById(R.id.clear);
        clear.setOnClickListener(this);
        selectAdapter = new SelectAdapter(this, CartManager.ins().cartData);
        rvSelected.setAdapter(selectAdapter);
        selectAdapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Good good) {
                if (good.getFormulaList() != null && good.getFormulaList().size() > 0) {
                    FormulaPop formulaPop = new FormulaPop(GoodListActivity.this, good, FormulaPop.TYPE_UPDATE);
                    formulaPop.showPopup();
                } else if (good.getAttributeList() != null && good.getAttributeList().size() > 0) {
                    AttributePop attrPop = new AttributePop(GoodListActivity.this, good, AttributePop.TYPE_UPDATE);
                    attrPop.showPopup();
                }

            }
        });
        return view;
    }

    private void showBottomSheet() {
        if (bottomSheet == null) {
            bottomSheet = createBottomSheetView();
        }
        if (bottomSheetLayout.isSheetShowing()) {
            bottomSheetLayout.dismissSheet();
        } else {
            if (CartManager.ins().cartList.size() != 0) {
                bottomSheetLayout.showWithSheetView(bottomSheet);
            }
        }
    }

    public void showOrderGoodDialog(Context context, final String id_order, final String number, final String persons) {
        NormalDialog dialog = new NormalDialog(context);
        dialog.setTitle(R.string.good_order_dialog_title);
        dialog.setTextDes(RT.getString(R.string.good_order_dialog_desc));
        dialog.setButton1(RT.getString(R.string.action_cancel), new NormalDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, NormalDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.setButton2(getString(R.string.action_ok), new NormalDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, NormalDialog dialog) {
                dialog.dismiss();
                showLoadingDialog(false);
                API_Food.ins().orderGood(TAG, CartManager.ins().getOrderGoodJson(CartManager.ins().isPack, id_order, number, persons), new JsonResponseCallback() {
                    @Override
                    public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
                        hideLoadingDialog();
                        if (errcode == 200) {
                            clearCart();
//                            EventManager.ins().sendEvent(EventTag.ORDER_LIST_REFRESH, 0, 0, null);
//                            Intent intent = new Intent(GoodListActivity.this, OrdersActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
                            EventManager.ins().sendEvent(EventTag.ACTIVITY_FINISH, 0, 0, null);
                            finish();
                            ToastUtil.showToast(RT.getString(R.string.good_order_success));
                        } else {
                            ToastUtil.showToast(RT.getString(R.string.good_order_failed));
                        }
                        return false;
                    }
                });
            }
        });
        dialog.show();
    }
}
