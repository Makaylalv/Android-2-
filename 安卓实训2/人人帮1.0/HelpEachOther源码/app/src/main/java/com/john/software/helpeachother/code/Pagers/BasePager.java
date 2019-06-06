package com.john.software.helpeachother.code.Pagers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.john.software.helpeachother.MainActivity;
import com.john.software.helpeachother.R;

import static cn.bmob.v3.Bmob.getApplicationContext;


/**
 *
 * Created by ZGQ  on 2017/11/14.
 */

public class BasePager  {

    public RelativeLayout relativeLayout;
    public Activity mActivity;
    public TextView tvTitle;
    public TextView tvTitlecenter;
    public ImageButton imageButton;
    public FrameLayout flcontent;
    public View rootview;


    public BasePager(Activity mActivity){

        this.mActivity=mActivity;

        rootview=initView();

    }

    public View initView(){


        View view=View.inflate(mActivity, R.layout.base_layout,null);
        tvTitle= (TextView) view.findViewById(R.id.tv_title);
        tvTitlecenter= (TextView) view.findViewById(R.id.center);
        imageButton=(ImageButton) view.findViewById(R.id.btn_menu);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainUI= (MainActivity) mActivity;
                mainUI.getSlidingMenu().toggle();
            }
        });


        relativeLayout= (RelativeLayout) view.findViewById(R.id.title_bar);
        flcontent=view.findViewById(R.id.fl_content);






        return  view;
    }
    public  void initData(){


    }


}
