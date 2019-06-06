package com.john.software.helpeachother.code.Record_ViewPager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.john.software.helpeachother.R;

/*
 * 项目名:   PullLoadMore
 * 包名:     com.john.software.pullloadmore.code.Record_ViewPager
 * 文件名:   BaseRecordViewPager
 * 创建者:   software.John
 * 创建时间: 2019/5/28 8:19
 * 描述:      TODO
 */
public class BaseRecordViewPager {
    public Activity mActivity;
    public View rootView;
    public FrameLayout fl_record_pager;
    public BaseRecordViewPager(Activity mActivity){
        this.mActivity=mActivity;
        rootView=initView();
    }
    public View initView(){
        rootView=View.inflate(mActivity, R.layout.base_record_pager,null);
        fl_record_pager= (FrameLayout) rootView.findViewById(R.id.fl_record_pager);
        return rootView;
    }
    public  void initData(){}
}
