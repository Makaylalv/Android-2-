package com.john.software.helpeachother.code.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.john.software.helpeachother.R;

public class SystemMessage extends AppCompatActivity {

    private TextView textView;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FF1A92E7"),false);
        textView= (TextView) findViewById(R.id.detial_tv_title);
        textView.setText("系统消息");
        back= (ImageButton) findViewById(R.id.detial_btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
