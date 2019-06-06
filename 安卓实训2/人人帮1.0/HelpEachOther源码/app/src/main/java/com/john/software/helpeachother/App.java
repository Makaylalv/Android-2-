package com.john.software.helpeachother;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import cn.bmob.v3.Bmob;

/*
 * 项目名:   PullLoadMore
 * 包名:     com.john.software.pullloadmore
 * 文件名:   App
 * 创建者:   software.John
 * 创建时间: 2019/5/9 11:11
 * 描述:      TODO
 */
public class App extends Application {
    @Override
    public void onCreate() {
        Bmob.initialize(this, "b3646f4197dc70a1bb71fccf86025339");

        //注意作用域
        SDKInitializer.initialize(this);

    }

}
