package com.john.software.helpeachother.code.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.MainActivity;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.LoadingDialog.LoadingDialog;
import com.john.software.helpeachother.code.Util.MyToast;
import com.john.software.helpeachother.code.Util.SpUtils.FirstCome;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Registration_or_Landing extends AppCompatActivity {
    private EditText telephone,password;
    private ImageView show_password,back;
    private TextView forget_password;
    private String tel,pas;
    private int a=0;
    private LoadingDialog loadingDialog;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_or_landing);

        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#f1f0f0"),false);
        telephone=findViewById(R.id.et_landing_telephone);
        password=findViewById(R.id.et_landing_password);
        back=findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        show_password=findViewById(R.id.iv_landing_show_password);
        forget_password=findViewById(R.id.forget_password);
        show_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a==0){
                    a=1;
                   show_password.setImageResource(R.drawable.landing_no_show);
                   password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    a=0;
                    show_password.setImageResource(R.drawable.landing_show);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        //忘记密码
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(),Registration.class);
                it.putExtra("forgetpas",1);
                startActivity(it);
            }
        });
        handler=new Handler(){
            @Override
            public void handleMessage(final Message msg) {
                if(msg.what==1){
                    MyToast.makeText(Registration_or_Landing.this,"账号或密码错误", Toast.LENGTH_LONG).show();
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);//关闭ProgressDialog

                }else if(msg.what==3){
                    new Thread(){
                        @Override
                        public void run() {
                            try{
                                sleep(2000);
                                Message message=new Message();
                                message.what=2;
                                handler.sendMessage(message);
                            }catch (InterruptedException er){
                                er.printStackTrace();
                            }
                        }
                    }.start();
                }if(msg.what==2){
                    Intent intent=new Intent(Registration_or_Landing.this, MainActivity.class);
                    startActivity(intent);
                    FirstCome.first=true;
                }else{
                    Intent intent1=getIntent();
                    int i=intent1.getIntExtra("settings",0);
                    if(i==1){
                        Intent intent=new Intent(Registration_or_Landing.this,MainActivity.class);
                        startActivity(intent);
                    }else{

                    }
                    setResult(RESULT_OK,intent1);//handler接收到消息就会i执行此方法
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);
                }
            }
        };
    }
    //登陆
    public void landing(View view){
        tel=telephone.getText().toString();
        pas=password.getText().toString();
        //信息错误
        if(tel.length()==11&&pas.length()!=0){
            //开启等待
            loadingDialog=new LoadingDialog();
            loadingDialog.startLoadingDialog(Registration_or_Landing.this,"加载中...");
            new Thread(){
                @Override
                public void run() {
                    BmobUser bu2=new BmobUser();
                    bu2.setUsername(tel);
                    bu2.setPassword(pas);
                    bu2.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if(e==null){
                                handler.sendEmptyMessage(0);
                                MyToast.makeText(Registration_or_Landing.this,"登陆成功，即将重新打开",Toast.LENGTH_LONG).show();
                                Message message=new Message();
                                message.what=3;
                                handler.sendMessage(message);
                                finish();
                                //通过BmobUser user=BmobUser.getCurrentUser()获取登陆成功后的本地用户信息
                                //如果是自定义用户对象MyUser,可通过MyUser user=BmobUser.getCurrentUser(MyUser.class);获取自定义用户信息

                            }else{
                                Message message=new Message();
                                message.what=1;
                                handler.sendMessage(message);
                            }
                        }
                    });
                }
            }.start();
        }
        else{
            MyToast.makeText(Registration_or_Landing.this,"账号或密码格式错误",Toast.LENGTH_LONG).show();
        }
    }
    //注册
    public void registration(View view){
        Intent it=new Intent(getApplicationContext(),Registration.class);
        startActivity(it);
    }
}
