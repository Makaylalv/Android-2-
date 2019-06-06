package com.john.software.helpeachother.code.Activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.Util.MyToast;

public class ContactMe extends AppCompatActivity {

    private TextView title;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FF1A92E7"),false);
        title=findViewById(R.id.detial_tv_title);
        title.setText("429小队的联系方式");
        back=findViewById(R.id.detial_btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void copyqq(View view){
        ClipboardManager copy=(ClipboardManager)ContactMe.this.getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setText("543984974");
        MyToast.makeText(this,"复制成功", Toast.LENGTH_LONG).show();
    }
    public void copyweibo(View view){
        ClipboardManager copy=(ClipboardManager)ContactMe.this.getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setText("429小队");
        MyToast.makeText(this,"复制成功",Toast.LENGTH_LONG).show();
    }
    public void copywechat(View view){
        ClipboardManager copy=(ClipboardManager)ContactMe.this.getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setText("人人帮");
        MyToast.makeText(this,"复制成功",Toast.LENGTH_LONG).show();
    }
    public void copyemail(View view){
        ClipboardManager copy=(ClipboardManager)ContactMe.this.getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setText("1521051354@qq.com");
        MyToast.makeText(this,"复制成功",Toast.LENGTH_LONG).show();
    }
}
