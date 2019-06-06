package com.john.software.helpeachother.code.Pagers;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.john.software.helpeachother.App;
import com.john.software.helpeachother.Bean.Information;
import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.Main2Activity;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.Activity.EditForHelp;
import com.john.software.helpeachother.code.Activity.ReceiveAddressList;
import com.john.software.helpeachother.code.Util.MyToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import static cn.bmob.v3.Bmob.getApplicationContext;

/*
 * 项目名:   PullLoadMore
 * 包名:     com.john.software.pullloadmore.code.Pagers
 * 文件名:   ForhelpPager
 * 创建者:   software.John
 * 创建时间: 2019/5/10 14:19
 * 描述:      TODO
 */
public class ForhelpPager extends BasePager {

    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationClient locationClient;
    private LocationClientOption option;

    private TextView for_kuaidi, for_waimai, for_things, for_study, for_qiandao, for_others, for_address, for_settings;
    private Intent it;
    private List<Information> item;
    private Activity mActivity;
    private MyUser userInfo;
    private Handler handler;
    private Date date1;
    private LinearLayout but_ding;


    public ForhelpPager(final Activity mActivity) {


        super(mActivity);


        this.mActivity = mActivity;

        //定
        judgePermission();


        it = new Intent(mActivity, EditForHelp.class);


    }


    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //获取经纬度
            double latitude = bdLocation.getLatitude();
            double longitude = bdLocation.getLongitude();

            List<Poi> pois = bdLocation.getPoiList();

            //
            if (pois!=null){
                for (Poi poi : pois) {
                    String poiName = poi.getName();
                    Log.i("lopi", "poiName" + poiName);
                    Log.i("lww", bdLocation.getAddrStr());
                }
            }


            float radius = bdLocation.getRadius();

            float direction = bdLocation.getDirection();


            MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,
                    true,
                    BitmapDescriptorFactory.fromResource(R.mipmap.loc));


            //设置定位数据
            MyLocationData locationData = new MyLocationData.Builder().direction(direction).latitude(latitude).longitude(longitude).build();

            //更新地图
            baiduMap.setMyLocationConfiguration(configuration);
            baiduMap.setMyLocationData(locationData);
            LatLng latLng = new LatLng(latitude, longitude);
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(mapStatusUpdate);
            //
        }
    }


    @Override
    public View initView() {

        View view = super.initView();

        return view;
    }

    @Override
    public void initData() {

        userInfo = BmobUser.getCurrentUser(MyUser.class);

        if (userInfo == null) {
        } else {
        }

        item = new ArrayList<>();
        super.initData();
        tvTitle.setText("帮我");
        imageButton.setVisibility(View.INVISIBLE);

        View view = View.inflate(mActivity, R.layout.forhelp, null);

        for_kuaidi = (TextView) view.findViewById(R.id.item_kuaidi);
        for_kuaidi.setOnClickListener(new item_onclick());
        for_waimai = (TextView) view.findViewById(R.id.item_waimai);
        for_waimai.setOnClickListener(new item_onclick());
        for_things = (TextView) view.findViewById(R.id.item_things);
        for_things.setOnClickListener(new item_onclick());
        for_study = (TextView) view.findViewById(R.id.item_study);
        for_study.setOnClickListener(new item_onclick());
        for_qiandao = (TextView) view.findViewById(R.id.item_qiandao);
        for_qiandao.setOnClickListener(new item_onclick());
        for_others = (TextView) view.findViewById(R.id.item_others);
        for_others.setOnClickListener(new item_onclick());
        for_address = (TextView) view.findViewById(R.id.item_address);
        for_address.setOnClickListener(new item_onclick());
        for_settings = (TextView) view.findViewById(R.id.item_settings);
        for_settings.setOnClickListener(new item_onclick());


        flcontent.removeAllViews();
        flcontent.addView(view);


        mapView = view.findViewById(R.id.mapview);

        baiduMap = mapView.getMap();
        //开启图层定位
        baiduMap.setMyLocationEnabled(true);

        //创建定位客户端对象
        locationClient = new LocationClient(getApplicationContext()); //

        forgroudService();

        //创建定位选项对象，并设置定位参数
        option = new LocationClientOption();

        setLocationClientoption();

        //应用定位选项
        locationClient.setLocOption(option);
        //启动定位
        locationClient.start();

        locationClient.registerLocationListener(new MyLocationListener());


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
                if (msg.what == 1) {
                    MyToast.makeText(mActivity,
                            "哎，网络又偷懒了，请稍后再试", Toast.LENGTH_SHORT).show();
                } else {
                    // MyToast.makeText(mActivity,
                    //       "保存成功", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    class item_onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_kuaidi:
                    StartActivity(0);
                    break;
                case R.id.item_waimai:
                    StartActivity(1);
                    break;
                case R.id.item_things:
                    StartActivity(2);
                    break;
                case R.id.item_study:
                    StartActivity(3);
                    break;
                case R.id.item_qiandao:
                    StartActivity(4);
                    break;
                case R.id.item_others:
                    StartActivity(5);
                    break;
                case R.id.item_address:
                    Intent intent = new Intent(mActivity, ReceiveAddressList.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.item_settings:
                    break;


            }
        }
    }

    public void StartActivity(int i) {
        if (userInfo == null) {
            MyToast.makeText(mActivity, "还未登录呢！", Toast.LENGTH_SHORT).show();
        } else if (userInfo.getSchool() == null) {
            MyToast.makeText(mActivity, "未填写学校，完善后才可以发布需求哦", Toast.LENGTH_LONG).show();
        } else if (userInfo.getSchool().length() == 0) {
            MyToast.makeText(mActivity, "未填写学校，完善后才可以发布需求哦", Toast.LENGTH_LONG).show();
        } else {
            it.putExtra("location", i);
            mActivity.startActivity(it);
        }
    }


    private void forgroudService() {
        //  开启前台定位服务
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        //获取一个Notification构造器
        Intent nfIntent = new Intent(getApplicationContext(), App.class);

        builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, nfIntent, 0))
                .setContentTitle("正在进行后台定位")//设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher)//设置状态栏内的小图标
                .setContentText("后台定位通知")//设置上下文内容
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());//设置该通知发生的时间
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            notification = builder.build();
        }
        notification.defaults = notification.DEFAULT_SOUND;
        locationClient.enableLocInForeground(1001, notification);//调起前台定位
    }

    //设置定位参数
    private void setLocationClientoption() {

        //设置定位时候扫面间隔时间
        option.setScanSpan(10000);
        //开启GPS定位
        option.setOpenGps(true);
        //设置包含地址信息
        option.setIsNeedLocationPoiList(true);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        //设置坐标类型
        option.setCoorType("gcj02");
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
    }

    //动态权限
    private void judgePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝

            // sd卡权限
            String[] SdCardPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(mActivity, SdCardPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(mActivity, SdCardPermission, 100);
            }

            //手机状态权限
            String[] readPhoneStatePermission = {Manifest.permission.READ_PHONE_STATE};
            if (ContextCompat.checkSelfPermission(mActivity, readPhoneStatePermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(mActivity, readPhoneStatePermission, 200);
            }

            //定位权限
            String[] locationPermission = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (ContextCompat.checkSelfPermission(mActivity, locationPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(mActivity, locationPermission, 300);
            }

            String[] ACCESS_COARSE_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION};
            if (ContextCompat.checkSelfPermission(mActivity, ACCESS_COARSE_LOCATION[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(mActivity, ACCESS_COARSE_LOCATION, 400);
            }


            String[] READ_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(mActivity, READ_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(mActivity, READ_EXTERNAL_STORAGE, 500);
            }

            String[] WRITE_EXTERNAL_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(mActivity, WRITE_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(mActivity, WRITE_EXTERNAL_STORAGE, 600);
            }

        } else {
            //doSdCardResult();
        }
        //LocationClient.reStart();
    }

}
