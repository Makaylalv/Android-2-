package com.john.software.helpeachother.code.Activity;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.LoadingDialog.LoadingDialog;
import com.john.software.helpeachother.code.Util.CountTimerUtils.CountTimerUtils;
import com.john.software.helpeachother.code.Util.MyToast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class Registration extends AppCompatActivity {
    private EditText telephone, password, yanzheng;
    private ImageView show_password, back;
    private TextView yanzhengma,tv_w,tv_la;
    private Button btn_reg;
    private String tel, pas, num;
    private CountTimerUtils mCountDownTimerUtils;
    private int a = 0;
    private LoadingDialog loadingDialog;
    private Handler handler;
    private int aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        telephone = (EditText) findViewById(R.id.et_registration_telephone);
        password = (EditText) findViewById(R.id.et_registration_password);
        back = (ImageView) findViewById(R.id.iv_back1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        yanzhengma = findViewById(R.id.tv_registration_yanzhengma);
        btn_reg= (Button) findViewById(R.id.btn_reg);
        tv_w= (TextView) findViewById(R.id.tv_w);
        tv_la= (TextView) findViewById(R.id.tv_lawsa);
        yanzheng = (EditText) findViewById(R.id.et_registration_yanzhengma);
        show_password = (ImageView) findViewById(R.id.iv_registration_show_password);

        Intent intent=getIntent();
        aa=intent.getIntExtra("forgetpas",0);
        if(aa==1){
            btn_reg.setText("重置密码");
            password.setHint("请输入您的密码");
            tv_w.setText("重置密码");
            tv_la.setVisibility(View.INVISIBLE);

        }else{
            btn_reg.setText("注册");
        }

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
        //验证码
        yanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(telephone.getText().toString().length()==11){
                    mCountDownTimerUtils=new CountTimerUtils(yanzhengma,6000,1000);
                    mCountDownTimerUtils.start();

                    //请求网络发送验证码
                    BmobSMS.requestSMSCode(telephone.getText().toString(), "HH", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer integer, BmobException ex) {
                            if(ex==null){
                                //验证码发送成功
                                MyToast.makeText(Registration.this,"发送成功", Toast.LENGTH_LONG).show();
                            }else{
                                MyToast.makeText(Registration.this,"发送失败",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    MyToast.makeText(Registration.this,"请输入正确的密码！",Toast.LENGTH_LONG).show();
                }
            }
        });

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);
                    MyToast.makeText(Registration.this,"重置失败，请稍后再试",Toast.LENGTH_LONG).show();
                }else if(msg.what==2){
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);
                    MyToast.makeText(Registration.this,"改手机号已被注册，请直接登陆",Toast.LENGTH_LONG).show();
                }else{
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);
                }
            }
        };
    }

    //注册点击响应
    public void registratio(View view){
        num=yanzheng.getText().toString();
        tel=telephone.getText().toString();
        pas=password.getText().toString();
        //信息错误
        if(tel.length()<11 || pas.length()<6 || num.length()<6){
            MyToast.makeText(this,"信息错误，请重新填写",Toast.LENGTH_LONG).show();
        }
        //发送登陆
        else{
            //开启等待
            loadingDialog=new LoadingDialog();
            loadingDialog.startLoadingDialog(Registration.this,"加载中...");
            new Thread(){
                @Override
                public void run() {
                    if(a==1){
                        //重置密码
                        chongzhi();
                    }else{
                        //注册
                        zhuce();
                    }
                }
            }.start();
        }
    }
    public void chongzhi(){
        BmobUser.resetPasswordBySMSCode(num, pas, new UpdateListener() {
            @Override
            public void done(BmobException ex) {
                if(ex==null){
                    handler.sendEmptyMessage(0);
                    MyToast.makeText(Registration.this,"重置成功",Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                }
            }
        });
    }
    public void zhuce(){
        MyUser user=new MyUser();
        user.setMobilePhoneNumber(tel);//设置手机号码（必填）
        user.setUsername(tel);//设置用户名，如果没有传用户名 则默认为手机号码
        user.setPassword(pas);//设置用户密码
        user.signOrLogin(num, new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e==null){
                    handler.sendEmptyMessage(0);
                    MyToast.makeText(Registration.this,"注册成功", Toast.LENGTH_LONG).show();
                    finish();
                    //退出
                }
            }
        });
    }
}
