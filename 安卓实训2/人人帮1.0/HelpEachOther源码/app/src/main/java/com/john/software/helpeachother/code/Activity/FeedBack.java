package com.john.software.helpeachother.code.Activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.john.software.helpeachother.Bean.FeedBackData;
import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.LoadingDialog.LoadingDialog;
import com.john.software.helpeachother.code.Util.MyToast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class FeedBack extends AppCompatActivity {
    private ImageButton back;
    private TextView title;
    private Button btn_tiiao;
    private RadioGroup radioGroup;
    private EditText editText;
    private int i;
    private String type, text;
    private Handler handler;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FF1A92E7"), false);
        title = findViewById(R.id.detial_tv_title);
        btn_tiiao = findViewById(R.id.btnFeedBack);
        btn_tiiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tijiao();
            }
        });
        title.setText("意见反馈");
        radioGroup = findViewById(R.id.rg_feedback);
        i = radioGroup.getCheckedRadioButtonId();
        editText = findViewById(R.id.et_feedback_text);
        back = findViewById(R.id.detial_btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);
                    MyToast.makeText(FeedBack.this, "登录后才可以反馈哦", Toast.LENGTH_LONG).show();
                } else {
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);
                }
            }
        };
    }

    public void tijiao() {
        i = radioGroup.getCheckedRadioButtonId();
        if (i == R.id.rb_advice) {
            type = "产品建议";
        } else
            type = "程序错误";
        text = editText.getText().toString();
        if (text.length() == 0) {
            MyToast.makeText(FeedBack.this, "请写下宝贵的意见", Toast.LENGTH_LONG).show();
        } else {
            //传送给网络
            loadingDialog = new LoadingDialog();
            loadingDialog.startLoadingDialog(FeedBack.this, "正在上传.....");
            new Thread() {
                @Override
                public void run() {
                    //上传反馈数据
                    PutInNet();
                }
            }.start();
        }

    }

    //上传反馈数据
    public void PutInNet() {
        FeedBackData feedBackData = new FeedBackData();
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        if (userInfo != null) {
            if (userInfo.getMyname() == null) {
                feedBackData.setUsername(userInfo.getUsername());
            } else {
                feedBackData.setUsername(userInfo.getMyname());
            }
            feedBackData.setTel_feedback(userInfo.getUsername());
            feedBackData.setDes(text);
            feedBackData.setType(type);
            feedBackData.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        handler.sendEmptyMessage(0);
                        MyToast.makeText(FeedBack.this, "提交成功，感谢您的反馈", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        handler.sendEmptyMessage(0);
                        MyToast.makeText(FeedBack.this, "反馈失败，请稍后再试", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    }
}
