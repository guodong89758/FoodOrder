package com.foodorder.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodorder.R;
import com.foodorder.base.BaseFragment;


public class PackFragment extends BaseFragment {


    public PackFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pack, container, false);
    }

}
