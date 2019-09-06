package com.example.zjq.mobileplayer.base;


/*
*
* 基类，公共类
* */

import android.content.Context;
import android.view.View;

public abstract class BasePager {

    public  Context context;
    public View rootview;
    public boolean isInitData;


    public BasePager(Context context){
        this.context=context;
        rootview=initView();
    }

    public abstract View initView() ;

    /*
    * 当子页面需要初始化数据，连网请求
    * */
    public void initDate(){

    };

}
