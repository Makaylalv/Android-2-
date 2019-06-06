package com.john.software.helpeachother;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.john.software.helpeachother.Bean.Address;
import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.Sliding.SlidingMenu;
import com.john.software.helpeachother.Sliding.app.SlidingFragmentActivity;
import com.john.software.helpeachother.code.Activity.Registration_or_Landing;
import com.john.software.helpeachother.code.Fragment.LeftMenuFragMent;
import com.john.software.helpeachother.code.Fragment.MainFragment;
import com.john.software.helpeachother.code.MyLinealayout;
import com.john.software.helpeachother.code.Util.MyToast;

import cn.bmob.v3.BmobUser;

public class MainActivity extends SlidingFragmentActivity {
   private long firstTime=0;
   private SlidingMenu slidingMenu;
   private int a=0;
   Address address;
   private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBehindContentView(R.layout.left_menu_layout);
        setContentView(R.layout.activity_main);
       // Bmob.initialize(this, "b3646f4197dc70a1bb71fccf86025339");
       MyUser myUser=BmobUser.getCurrentUser(MyUser.class);
       if(myUser!=null){
           Log.e("MainActivity","user!=null");
       }else{
           handler=new Handler();
           MyToast.makeText(this,"未登录，即将打开登陆界面！", Toast.LENGTH_LONG).show();
           new Thread(){
               @Override
               public void run() {
                   try{
                       Thread.sleep(2000);
                       handler.sendEmptyMessage(0);
                   }catch (InterruptedException e){
                       e.printStackTrace();
                   }
               }
           }.start();
       }
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //handler接收到消息就会执行此方法
                Intent intent=new Intent(MainActivity.this,Registration_or_Landing.class);
                startActivity(intent);
                finish();
            }
        };
       //解决虚拟菜单键底部导航栏被遮盖的问题
        MyLinealayout.assistActivity(this);
        //测量屏幕宽度
        WindowManager wm=this.getWindowManager();
        int with=wm.getDefaultDisplay().getWidth();
        slidingMenu=getSlidingMenu();
        //设置预留剩余屏幕宽度
        slidingMenu.setBehindOffset(with/3);
        //主屏幕全屏触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                a=0;
            }
        });
        //侧滑菜单监听
        slidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                a=1;
            }
        });
        initFragMent();

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(a==0){
            if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_UP){
                long secondeTime=System.currentTimeMillis();
                if(secondeTime-firstTime>1500){
                    MyToast.makeText(MainActivity.this,"再按一次推出喽！",Toast.LENGTH_LONG);
                    firstTime=secondeTime;
                    return true;
                }else{
                    System.exit(0);
                }
            }
            return super.onKeyUp(keyCode,event);
        }
       else slidingMenu.toggle();
        return false;
    }
    private void initFragMent(){
        FragmentManager fragment=getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction=fragment.beginTransaction();
        transaction.replace(R.id.left_menu_layout,new LeftMenuFragMent(),"LEFT_FRAGMENT");
        transaction.replace(R.id.activity_main,new MainFragment(),"MAIN_FRAGMENT");
        //提交事务
        transaction.commit();
    }
}

