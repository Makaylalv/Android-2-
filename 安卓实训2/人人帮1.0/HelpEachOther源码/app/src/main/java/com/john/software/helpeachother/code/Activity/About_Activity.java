package com.john.software.helpeachother.code.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.Util.MyToast;

public class About_Activity extends AppCompatActivity {

    private TextView version_code,update,getVersion_code_1,title;
    private ImageView icon,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FF1A92E7"),false);
        version_code= (TextView) findViewById(R.id.version_code);
        getVersion_code_1= (TextView) findViewById(R.id.version_code_1);
        icon= (ImageView) findViewById(R.id.icon);
        back= (ImageView) findViewById(R.id.detial_btn_back);
        title= (TextView) findViewById(R.id.detial_tv_title);
        update= (TextView) findViewById(R.id.update);
        init();
    }
    private void init() {
        title.setText("版本信息");
        //返回点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //版本更新点击事件
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.makeText(About_Activity.this,"已经是最新版本了！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
