package com.john.software.helpeachother.code.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.john.software.helpeachother.R;

public class Laws extends AppCompatActivity {

    private TextView title;
    private ImageButton back;
    private RelativeLayout detial_title_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laws);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FF1A92E7"),false);
        back=findViewById(R.id.detial_btn_back);
        detial_title_bar=findViewById(R.id.detial_title_bar);
        title=findViewById(R.id.detial_tv_title);
        title.setText(("【服务条款】（重要）"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
