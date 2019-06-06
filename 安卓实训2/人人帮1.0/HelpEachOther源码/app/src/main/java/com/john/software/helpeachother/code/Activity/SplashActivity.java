package com.john.software.helpeachother.code.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.john.software.helpeachother.MainActivity;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.Util.SpUtils.SpUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);}



        initUI();
    }

    private void initUI() {
        RelativeLayout splash= (RelativeLayout) findViewById(R.id.activity_splash);
        RotateAnimation rota=new RotateAnimation(0,360,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rota.setDuration(800);
        rota.setFillAfter(true);
        ScaleAnimation scale=new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scale.setDuration(600);
        scale.setFillAfter(true);
        AlphaAnimation alpha=new AlphaAnimation(0,1);
        alpha.setDuration(1000);
        alpha.setFillAfter(true);
        AnimationSet anima=new AnimationSet(true);
        anima.addAnimation(scale);
        anima.addAnimation(alpha);
        splash.setAnimation(anima);
        anima.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Boolean sp= SpUtils.getBoolean(getApplicationContext(),"FIRST_ENTER",true);
                if(sp){
                    //第一次进入
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finally {
                                enter_newuser();
                            }
                        }
                    }.start();

                }else{
                    //不是第一次进入
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(600);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finally{
                                enter_home();

                            }
                        }

                    }.start();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void enter_newuser() {
        SpUtils.putBoolean(getApplicationContext(),"FIRST_ENTER",false);
        Intent it =new Intent(getApplicationContext(),GuideActivity.class);
        startActivity(it);
        finish();
    }

    private void enter_home() {
        Intent it =new Intent(getApplicationContext(),MainActivity.class);
        startActivity(it);
        finish();
    }


}
