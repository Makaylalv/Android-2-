package com.john.software.helpeachother.code.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.john.software.helpeachother.Bean.Information;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.LoadingDialog.LoadingDialog;
import com.john.software.helpeachother.code.Util.MyToast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class HelpDetialActivity extends AppCompatActivity {

    //public ImageButton detial_btn_back;
    private int[] ints;
    private int pos;
    private ImageButton back;
    public TextView writer_name,detial_money,detial_des,detial_tv_title,
            detial_time,tv_money_symbol,show_send_time,
            show_tel,show_things,show_types,show_take_things_address
            ,show_address,show_time,tv_show_pas;
    public Button call,ok,say,person;
    private RelativeLayout rl_detail;
    private ImageView ig_header_view;
    private int ii;
    private String things;
    private String address_take;
    private String time_send;
    private String sign;
    private String name;
    private String tel;
    private String address_send;
    private String types;
    private String time,password,id;
    private LoadingDialog loadingDialog;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_detial);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FF0070BF"),false);
        tv_money_symbol= (TextView) findViewById(R.id.tv_money_symbol);
        rl_detail= (RelativeLayout) findViewById(R.id.rl_detail);
        ig_header_view= (ImageView) findViewById(R.id.ig_header_view);
        writer_name= (TextView) findViewById(R.id.show_name);
        detial_money= (TextView) findViewById(R.id.show_money);
        detial_des= (TextView) findViewById(R.id.show_beizhu);
        show_tel= (TextView) findViewById(R.id.show_tel);
        show_send_time= (TextView) findViewById(R.id.show_send_time);
        show_types= (TextView) findViewById(R.id.show_types);
        show_things= (TextView) findViewById(R.id.show_things);
        show_take_things_address= (TextView) findViewById(R.id.show_take_things_address);
        show_address= (TextView) findViewById(R.id.show_address);
        show_time= (TextView) findViewById(R.id.show_time);
        person= (Button) findViewById(R.id.personal);
        call= (Button) findViewById(R.id.call);
        ok= (Button) findViewById(R.id.ok);
        say= (Button) findViewById(R.id.say);
        init();
        initview();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
                if(msg.what==1){
                    MyToast.makeText(HelpDetialActivity.this,
                            "网络又偷懒了！稍后再试", Toast.LENGTH_SHORT).show();
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                }else if(msg.what==2){
                    MyToast.makeText(HelpDetialActivity.this,
                            "手速慢了，被别人抢走了",Toast.LENGTH_SHORT).show();
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                } else if(msg.what==4){
                    //帮助者确认帮助
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                    MyToast.makeText(HelpDetialActivity.this,
                            "确认成功,返回手动刷新记录以更新",Toast.LENGTH_SHORT).show();
                    StatusBarCompat.setStatusBarColor(HelpDetialActivity.this, Color.parseColor("#ff5f1a"),false);
                    rl_detail.removeAllViews();
                    View view=View.inflate(HelpDetialActivity.this,R.layout.help_ok,null);
                    back= (ImageButton) view.findViewById(R.id.detial_btn_back);
                    detial_tv_title= (TextView) view.findViewById(R.id.detial_tv_title);
                    detial_tv_title.setText("完成");
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    rl_detail.addView(view);
                }else {
                    Intent intent =new Intent(HelpDetialActivity.this,HelpFinish.class);
                    intent.putExtra("pas",password);
                    intent.putExtra("id",id);
                    startActivity(intent);
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                    finish();
                }
            }
        };
    }
    private void initview() {
        Intent intent=getIntent();
        ii=intent.getIntExtra("p",0);
        if(ii==1){
            ok.setText("查看密码");
            ok.setTextSize(12);
            say.setText("完成帮助");
            say.setTextSize(12);
        }
        name=intent.getStringExtra("name");
        types=intent.getStringExtra("types");
        tel=intent.getStringExtra("tel");
        things=intent.getStringExtra("things");
        address_take=intent.getStringExtra("take_address");
        address_send=intent.getStringExtra("send_address");
        time_send=intent.getStringExtra("time_send");
        sign=intent.getStringExtra("des");
        time=intent.getStringExtra("time");
        password=intent.getStringExtra("password");
        id=intent.getStringExtra("object_id");
        writer_name.setText(name);
        show_send_time.setText(time);
        show_tel.setText(tel);
        if(things.length()==0){things="无";}
        show_things.setText(things);
        show_types.setText(types);
        if(address_take.length()==0){address_take="无";}
        show_take_things_address.setText(address_take);
        show_address.setText(address_send);
        show_time.setText(time_send);
        if(sign.length()==0){sign="无";}
        detial_des.setText(sign);
        switch (types){
            case "帮取快递":pos =0;break;
            case "帮取外卖":pos =1;break;
            case "帮买东西":pos =2;break;
            case "学习资料":pos =3;break;
            case "上课签到":pos =4;break;
            case "其他":pos =5;break;
        }
        ig_header_view.setImageResource(ints[pos]);
    }

    private void init() {
        ints=new int[]{R.drawable.kuaidi,
                R.drawable.waimai,
                R.drawable.maidongxi,
                R.drawable.xuexi,
                R.drawable.qiandao,
                R.drawable.bg_help_detial};

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it =new Intent();
                it.setAction(Intent.ACTION_VIEW);
                it.setData(Uri.parse("tel:"+show_tel.getText().toString()));
                startActivity(it);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ii==1){
                    Intent intent =new Intent(HelpDetialActivity.this,HelpFinish.class);
                    intent.putExtra("iii",1);
                    intent.putExtra("pas",password);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }else {
                    showNormalDialog();
                }
            }
        });
        say.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ii==1){
                    //帮助成功，三小时内钱款将转至您的账户
                    //发起网络连接，将ok设置为true
                    //开启progressDialog
                    showDialog();
                }else{
                    Uri uri2 = Uri.parse("smsto:"+show_tel.getText().toString());
                    Intent intentMessage = new Intent(Intent.ACTION_VIEW,uri2);
                    startActivity(intentMessage);
                }
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it= new Intent(getApplicationContext(), PersonalCenterActivity.class);
                it.putExtra("po",1);
                it.putExtra("id",tel);
                it.putExtra("num",2);
                startActivity(it);
            }
        });
    }
    //确认帮助提示框
    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setIcon(R.drawable.tishi);
        normalDialog.setTitle("确认");
        normalDialog.setCancelable(false);
        normalDialog.setMessage("您确定帮助他后将查看他的快递单号或密码等必要信息");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadingDialog=new LoadingDialog();
                        loadingDialog.startLoadingDialog(HelpDetialActivity.this,"提交中...");
                        new Thread(){
                            @Override
                            public void run() {
                                GetData();
                            }
                        }.start();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
    //完成帮助提示框
    private void showDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setIcon(R.drawable.tishi);
        normalDialog.setTitle("确认");
        normalDialog.setCancelable(false);
        normalDialog.setMessage("已经完成帮助？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //开启progressDialog
                        loadingDialog=new LoadingDialog();
                        loadingDialog.startLoadingDialog(HelpDetialActivity.this,"加载中...");
                        new Thread(){
                            @Override
                            public void run() {
                                DoItOnNet();
                            }
                        }.start();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
    //网络连接查看订单是否被他人抢走
    private void GetData() {
        BmobQuery<Information> query = new BmobQuery<Information>();
        query.getObject(id, new QueryListener<Information>() {
            @Override
            public void done(Information object, BmobException e) {
                if(e==null){
                    //获得playerName的信息
                    boolean finish= object.getFinish();
                    if(finish){
                        //已被别人抢走
                        Message message=new Message();
                        message.what=2;
                        handler.sendMessage(message);
                    }else {
                        Message message=new Message();
                        message.what=3;
                        handler.sendMessage(message);
                        //可以继续
                    }
                }else{
                    //网络连接失败
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                    // Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    //完成帮助确认
    private void DoItOnNet() {
        Information information = new Information();
        information.setOk(true);
        information.setFinish(true);
        information.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Message message=new Message();
                    message.what=4;
                    handler.sendMessage(message);
                }else{
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                }
            }
        });
    }
    }

