package com.john.software.helpeachother.code.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.john.software.helpeachother.Bean.Information;
import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.LoadingDialog.LoadingDialog;
import com.john.software.helpeachother.code.Util.MyToast;
import com.john.software.helpeachother.code.Util.SpUtils.SpUtils;

import org.feezu.liuli.timeselector.TimeSelector;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class EditForHelp extends AppCompatActivity {
    private EditText mthing, mtakeaddress, mbeizhu, forhelp_in_password;
    private TextView time_selector, textViehhhw, mtel, mname, msendaddress;
    private Button send;
    private CardView cv_send;
    private TimeSelector timeSelector;
    private LinearLayout ll_take_address, ll_name, ll_myinfo;
    private RadioGroup rg_time;
    private int year, month, date, hour, minute;
    private String startdate, things, address_take, time_send, sign, name, tel, address_send, types, password;
    private TextView title, tv_name, textViewsd;
    private ImageButton back;
    private Intent intent;
    public LoadingDialog loadingDialog;
    private int i;
    private Handler handler;
    private MyUser userInfo;
    private Integer integer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_for_help);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1296db"), false);
        rg_time = (RadioGroup) findViewById(R.id.rg_time);
        cv_send = (CardView) findViewById(R.id.cv_sendmess);
        mtel = (TextView) findViewById(R.id.tv_forhelp_address_tel);
        mtel.setText(SpUtils.getString(EditForHelp.this, "tel", ""));
        mthing = (EditText) findViewById(R.id.forhelp_in_thing_name);
        msendaddress = (TextView) findViewById(R.id.tv_forhelp_address_des);
        msendaddress.setText(SpUtils.getString(EditForHelp.this, "address", "未添加地址，点击添加！"));
        mtakeaddress = (EditText) findViewById(R.id.forhelp_in_take_address);
        mbeizhu = (EditText) findViewById(R.id.forhelp_in_beizhu);
        mname = (TextView) findViewById(R.id.tv_forhelp_address_name);
        tv_name = (TextView) findViewById(R.id.tv_name);
        mname.setText(SpUtils.getString(EditForHelp.this, "name", ""));
        ll_myinfo = (LinearLayout) findViewById(R.id.ll_myinfo);
        textViewsd = (TextView) findViewById(R.id.textViewsd);
        ll_take_address = (LinearLayout) findViewById(R.id.ll_take_address);
        ll_name = (LinearLayout) findViewById(R.id.ll_name);
        textViehhhw = (TextView) findViewById(R.id.textViehhhw);
        back = (ImageButton) findViewById(R.id.detial_btn_back);
        title = (TextView) findViewById(R.id.detial_tv_title);
        forhelp_in_password = (EditText) findViewById(R.id.forhelp_in_password);
        intent = getIntent();
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if (userInfo == null) {
            //未登录
            MyToast.makeText(EditForHelp.this, "还未登录，请登录后发布", Toast.LENGTH_SHORT).show();
        } else {
            //登陆后且完善了个人信息
            i = intent.getIntExtra("location", 8);
            switch (i) {
                case 0:
                    title.setText("取快递");
                    types = "帮取快递";
                    break;
                case 1:
                    title.setText("取外卖");
                    types = "帮取外卖";
                    break;
                case 2:
                    title.setText("买东西");
                    types = "帮买东西";
                    textViehhhw.setText("购买地址");
                    break;
                case 3:
                    title.setText("学习资料");
                    types = "学习资料";
                    tv_name.setText("资料名称");
                    ll_take_address.setVisibility(View.GONE);
                    break;
                case 4:
                    title.setText("帮Ta签到");
                    types = "上课签到";
                    mtakeaddress.setHint("请输入签到地点");
                    ll_name.setVisibility(View.GONE);
                    textViehhhw.setText("签到地址");
                    textViewsd.setText("签到时间");
                    break;
                case 5:
                    title.setText("其他");
                    types = "其他";
                    break;
            }
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            time_selector=findViewById(R.id.time_selector);
            //接收时间按钮监听
            ReceiveOnClickListener();
            //点击设置时间按钮监听
            SettimeOnClickListener();
            //选择地址
            ll_myinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(EditForHelp.this,ReceiveAddressList.class);
                    intent.putExtra("from",1);
                    startActivityForResult(intent,9);
                }
            });
            //点击提交到网络
            cv_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    things=mthing.getText().toString();
                    address_take=mtakeaddress.getText().toString();
                    if(time_selector.getText().toString().equals("点击设置时间")){
                        time_send="尽快";
                    }else{
                        time_send=time_selector.getText().toString();
                    }
                    name=mname.getText().toString();
                    password=forhelp_in_password.getText().toString();
                    tel=mtel.getText().toString();
                    address_send=msendaddress.getText().toString();
                    sign=mbeizhu.getText().toString();
                    switch (i){
                        case 0://取快递
                            if(things.length()!=0&address_take.length()!=0&time_send.length()!=0&name.length()!=0&
                                    tel.length()!=0&address_send.length()!=0&password.length()!=0){
                                showNormalDialog();
                            }else{
                                MyToast.makeText(EditForHelp.this,"部分内容未填写",Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 1: //取外卖
                            if (things.length() != 0 & address_take.length() != 0 & time_send.length() != 0
                                    & name.length() != 0 &
                                    tel.length() != 0 & address_send.length() != 0&password.length()!=0) {
                                showNormalDialog();

                            } else {
                                MyToast.makeText(EditForHelp.this, "部分内容未填写", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2: //买东西
                            if (things.length() != 0 & address_take.length() != 0 & time_send.length() != 0
                                    & name.length() != 0 &
                                    tel.length() != 0 & address_send.length() != 0&password.length()!=0) {


                                showNormalDialog();
                            } else {
                                MyToast.makeText(EditForHelp.this, "部分内容未填写", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 3: //学习资料
                            if (things.length() != 0 & time_send.length() != 0
                                    & name.length() != 0 &
                                    tel.length() != 0 & address_send.length() != 0&password.length()!=0) {

                                showNormalDialog();
                            } else {
                                MyToast.makeText(EditForHelp.this, "部分内容未填写", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 4: //上课签到
                            if (address_take.length() != 0 & time_send.length() != 0
                                    & name.length() != 0 &
                                    tel.length() != 0 & address_send.length() != 0&password.length()!=0) {

                                showNormalDialog();
                            } else {
                                MyToast.makeText(EditForHelp.this, "部分内容未填写", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 5: //其他
                            if (things.length() != 0 & address_take.length() != 0 & time_send.length() != 0
                                    & name.length() != 0 &
                                    tel.length() != 0 & address_send.length() != 0&password.length()!=0) {

                                showNormalDialog();
                            } else {
                                MyToast.makeText(EditForHelp.this, "部分内容未填写", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                }
            });
            handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what==1){
                        MyToast.makeText(EditForHelp.this,"发布失败，网络又偷懒了，稍后再试",Toast.LENGTH_LONG).show();
                        loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);
                    }else{
                        MyToast.makeText(EditForHelp.this,"发布成功",Toast.LENGTH_LONG).show();
                        finish();
                        loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);
                    }
                }
            };
        }
    }
        //点击设置时间按钮监听
    private void SettimeOnClickListener() {
        time_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeSelectorShow();
            }
        });
    }

    //接收时间按钮监听
    private void ReceiveOnClickListener() {
        rg_time.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.jinkuai:
                        time_selector.setText("点击设置时间");
                        time_selector.setVisibility(View.INVISIBLE);break;
                    case R.id.lingyue:
                        time_selector.setVisibility(View.VISIBLE);break;
                }
            }
        });
    }

    //自定义获取时间方法
    private void getTime() {
        Time t=new Time("GMT+8");
        t.setToNow(); // 取得系统时间。
        year = t.year;
        month = t.month;
        date = t.monthDay;
        hour = t.hour;
        minute = t.minute;
        startdate= String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+
                String.valueOf(date+1)+" "+String.valueOf(hour-16)+":"+String.valueOf(minute);
    }
    //时间选择器显示
    private void TimeSelectorShow() {
        getTime();
        timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                // Toast.makeText(mActivity.getApplicationContext(), time, Toast.LENGTH_LONG).show();
                time_selector.setText(time);
            }
        }, startdate, "2100-12-31 23:59");
        //timeSelector.setIsLoop(false);//循环显示
        timeSelector.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mtel.setText(SpUtils.getString(EditForHelp.this,"tel",""));
        msendaddress.setText(SpUtils.getString(EditForHelp.this,"address","未添加地址，点击添加！"));
        mname.setText(SpUtils.getString(EditForHelp.this,"name",""));
    }
    //开启子线程，上传数据
    public void PutOnNet(){
        loadingDialog=new LoadingDialog();
        loadingDialog.startLoadingDialog(EditForHelp.this,"发布中...");
        new Thread(){
            @Override
            public void run() {
                Information information =new Information();
                information.setTypes(types);
                information.setName(name);
                information.setTel(tel);
                information.setSchool(userInfo.getSchool());
                information.setAddress_send(address_send);
                information.setAddress_take(address_take);
                information.setThings(things);
                information.setSign(sign);
                information.setPassword(password);
                information.setFinish(false);
                information.setTime_send(time_send);
                information.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            handler.sendEmptyMessage(0);
                        }else {
                            Message message=new Message();
                            message.what=1;
                            handler.sendMessage(message);
                        }
                    }
                });
            }
        }.start();

    }
    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(EditForHelp.this);
        normalDialog.setIcon(R.drawable.ok_);
        normalDialog.setTitle("发布");
        normalDialog.setMessage("您确认发布该信息");
        normalDialog.setCancelable(false);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PutOnNet();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do-nothing
                    }
                });
        // 显示
        normalDialog.show();
    }
}

