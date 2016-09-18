package com.foodorder.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.nextjoy.library.dialog.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by guodong on 2016/6/28 11:57.
 */
public class BaseFragment extends Fragment {
    private LoadingDialog mLoadingDialog;

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
    }

    protected void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getActivity());
        }
        mLoadingDialog.show();
    }

    protected void showLoadingDialog(int resid) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getActivity());
        }
        mLoadingDialog.setContent(getResources().getString(resid));
        mLoadingDialog.show();
    }

    protected void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public boolean isVisible(Context context) {
        return isAdded() && !isHidden() && getView() != null
                && getView().getVisibility() == View.VISIBLE;
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        try {
//            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
//            childFragmentManager.setAccessible(true);
//            childFragmentManager.set(this, null);
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
