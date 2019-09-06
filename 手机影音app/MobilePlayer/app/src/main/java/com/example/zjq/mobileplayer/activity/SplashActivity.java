package com.example.zjq.mobileplayer.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.zjq.mobileplayer.R;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG =SplashActivity.class.getSimpleName() ;


    private String[] permissions = {

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash );
        getPermission();


    }

    private Handler handler=new Handler(  ){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );

            switch (msg.what){
                case 1:
                    handler.postDelayed( new Runnable() {
                        @Override
                        public void run() {
                            startManinAcitvity();
                        }
                    } ,1000);
                    break;
                case 0:
                    finish();
                    break;

            }

        }
    };



    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
            Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessage( 1 );
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions( this, "需要获取相关权限", 1, permissions);

        }

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        Toast.makeText(this, "相关权限获取成功", Toast.LENGTH_SHORT).show();
        handler.sendEmptyMessage( 1 );

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
        handler.sendEmptyMessage( 0 );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] strings, @NonNull int[] grantResults) {
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private void startManinAcitvity() {


        Intent intent=new Intent( this,MainActivity.class );
        startActivity( intent );


        //关闭当前页面
        finish();
    }


    //显示logo的1秒时 点击屏幕可快速进入
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e( TAG,"onTouchEvent==Action()"+event.getAction() );
        startManinAcitvity();
        return  super.onTouchEvent( event );
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages( null );//所有的消息被移除
        super.onDestroy();
    }
}
