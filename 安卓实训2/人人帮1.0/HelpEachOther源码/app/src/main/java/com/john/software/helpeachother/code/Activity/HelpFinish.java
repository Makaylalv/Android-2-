package com.john.software.helpeachother.code.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.john.software.helpeachother.Bean.Information;
import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.LoadingDialog.LoadingDialog;
import com.john.software.helpeachother.code.Util.MyToast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class HelpFinish extends AppCompatActivity {

    private String id;
    private String pas;
    private Intent intent;
    private TextView textView,detial_tv_title;
    private LoadingDialog loadingDialog;
    private Handler handler;
    private MyUser myUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_finish);
        textView= (TextView) findViewById(R.id.tv_show_pas);
        intent=getIntent();
        id=intent.getStringExtra("id");
        pas=intent.getStringExtra("pas");
        Button back= (Button)findViewById(R.id.back);
        myUser= BmobUser.getCurrentUser(MyUser.class);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //开启progressDialog
        if(intent.getIntExtra("iii",0)==1){
            textView.setText(pas);
        }else {
            loadingDialog = new LoadingDialog();
            loadingDialog.startLoadingDialog(HelpFinish.this, "加载中...");
            new Thread(){
                @Override
                public void run() {
                    DoItOnNet();
                }
            }.start();
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
                if(msg.what==1){
                    textView.setText("错误");
                    MyToast.makeText(HelpFinish.this,
                            "网络又偷懒了！稍后再试", Toast.LENGTH_SHORT).show();
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                } else {
                    MyToast.makeText(HelpFinish.this,"帮助成功，您真是活雷锋", Toast.LENGTH_SHORT).show();
                    textView.setText(pas);
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                }
            }
        };
    }
    private void DoItOnNet() {
        Information information = new Information();
        information.setFinish(true);
        information.setHelper(myUser.getUsername());
        information.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    handler.sendEmptyMessage(0);
                    //handler.sendMessage(message);
                }else{
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                }
            }
        });
    }
}
