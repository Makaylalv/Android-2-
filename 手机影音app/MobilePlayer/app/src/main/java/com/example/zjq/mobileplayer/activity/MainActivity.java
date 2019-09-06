package com.example.zjq.mobileplayer.activity;


import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zjq.mobileplayer.R;
import com.example.zjq.mobileplayer.base.BasePager;
import com.example.zjq.mobileplayer.fragment.ReplaceFragment;
import com.example.zjq.mobileplayer.pager.AudioPager;
import com.example.zjq.mobileplayer.pager.NetAudioPager;
import com.example.zjq.mobileplayer.pager.NetVideoPager;
import com.example.zjq.mobileplayer.pager.VideoPager;

import java.util.ArrayList;




public class MainActivity extends FragmentActivity {
    private FrameLayout fl_main_content;
    private RadioGroup rg_bottom_tag;

    private ArrayList<BasePager> basePagers;

    //选中的位置
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        fl_main_content = findViewById( R.id.fl_main_content );
        rg_bottom_tag = findViewById( R.id.rg_bottom_tag );

        basePagers = new ArrayList<>();
        basePagers.add( new VideoPager( this ) );
        basePagers.add( new AudioPager( this ) );
        basePagers.add( new NetVideoPager( this ) );
        basePagers.add( new NetAudioPager( this ) );

        //设置RadioGroup监听
        rg_bottom_tag.setOnCheckedChangeListener( new MyOnCheckedChangeListener() );
        rg_bottom_tag.check( R.id.rb_video );


    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                default:
                    position = 0;
                    break;

                case R.id.rb_audio:
                    position = 1;
                    break;

                case R.id.rb_net_video:
                    position=2;
                    break;

                case R.id.rb_net_audio:
                    position=3;
                    break;

            }

            setFragment();

        }
    }




    private void setFragment() {
        //把頁面添加到Fragment中

        //1.得到FragmentManger
        FragmentManager manager=getSupportFragmentManager();

        //2.开启事务
        FragmentTransaction ft= manager.beginTransaction();
        //3.替换
        ft.replace( R.id.fl_main_content, new ReplaceFragment(getBasePager()));


        //4.提交事务
        ft.commit();
    }

    private BasePager getBasePager(){

        BasePager basePager=basePagers.get( position );

        if (basePager!=null&&!basePager.isInitData){
            basePager.initDate();
            basePager.isInitData=true;
        }

        return basePager;
    }


    //是否退出
    private boolean isExit=false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (position!=0){//不是第一个页面
                position=0;
                rg_bottom_tag.check( R.id.rb_video );
                return true;
            }else if (!isExit){
                isExit=true;
                Toast.makeText( MainActivity.this,"在按一次退出",Toast.LENGTH_SHORT ).show();
                new Handler(  ).postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        isExit=false;
                    }
                } ,200);

            }


        }

        return super.onKeyDown( keyCode, event );
    }
}
