package com.john.software.helpeachother.code.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * 项目名:   PullLoadMore
 * 包名:     com.john.software.pullloadmore.code.Fragment
 * 文件名:   BaseFragMent
 * 创建者:   software.John
 * 创建时间: 2019/5/10 10:12
 * 描述:      TODO
 */
public abstract class BaseFragMent extends Fragment {
    Activity mActivity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=initView();
        return view;
    }

    public abstract View initView();
    public abstract void initData();

}
