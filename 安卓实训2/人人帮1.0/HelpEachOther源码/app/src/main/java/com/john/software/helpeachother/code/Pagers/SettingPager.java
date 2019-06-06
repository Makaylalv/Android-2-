package com.john.software.helpeachother.code.Pagers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.MainActivity;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.Activity.About_Activity;
import com.john.software.helpeachother.code.Activity.AdTogether;
import com.john.software.helpeachother.code.Activity.ContactMe;
import com.john.software.helpeachother.code.Activity.FeedBack;
import com.john.software.helpeachother.code.Activity.HelpFenLei;
import com.john.software.helpeachother.code.Activity.Laws;
import com.john.software.helpeachother.code.Activity.ReceiveAddressList;
import com.john.software.helpeachother.code.Activity.SystemMessage;
import com.john.software.helpeachother.code.Util.MyToast;

import cn.bmob.v3.BmobUser;

/*
 * 项目名:   PullLoadMore
 * 包名:     com.john.software.pullloadmore.code.Pagers
 * 文件名:   SettingPager
 * 创建者:   software.John
 * 创建时间: 2019/5/10 14:20
 * 描述:      TODO
 */
public class SettingPager extends BasePager {
    private Activity mActivity;
    private Button message;
    private LinearLayout ll_money,ll_i_help,ll_help_i;
    private CardView cardView;
    public boolean comeonline = false;
    private ImageView iv_setting_person;
    private CardView exit;
    private MyUser userInfo;
    public TextView address, add_AD, question, laws, feed_back, about, settings, come_online, counter_help, counter_forhelp, sign;

    public SettingPager(Activity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;

    }

    @Override
    public View initView() {

        return super.initView();
    }

    @Override
    public void initData() {
        //让toolbar消失
        relativeLayout.setVisibility(View.GONE);
        init();
    }

    private void init() {
        View view = View.inflate(mActivity, R.layout.person_center_pager, null);
        address = (TextView) view.findViewById(R.id.address);
        about = (TextView) view.findViewById(R.id.about);
        add_AD = (TextView) view.findViewById(R.id.add_AD);
        //question=(TextView) view.findViewById(R.id.question);
        laws = (TextView) view.findViewById(R.id.laws);
        feed_back = (TextView) view.findViewById(R.id.feed_back);
        settings = (TextView) view.findViewById(R.id.settings);
        cardView = (CardView) view.findViewById(R.id.online_cardview);
        come_online = (TextView) view.findViewById(R.id.come_online);
        exit = (CardView) view.findViewById(R.id.online_cardview);
        iv_setting_person = (ImageView) view.findViewById(R.id.iv_setting_person);
        sign = (TextView) view.findViewById(R.id.tv_sign);
        ll_money= (LinearLayout) view.findViewById(R.id.my_credit);
        ll_i_help= (LinearLayout) view.findViewById(R.id.count_my_help);
        ll_help_i= (LinearLayout) view.findViewById(R.id.count_for_help);
        message = (Button) view.findViewById(R.id.message);
        //检测登陆
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if (userInfo != null) {
            comeonline = true;
            //如果登陆了，则显示个性签名！
            sign.setVisibility(View.VISIBLE);
            exit.setVisibility(View.VISIBLE);
            if (userInfo.getMyname() == null) {
                come_online.setText(userInfo.getUsername());
                if (userInfo.getDes() == null) {
                    sign.setText("这家伙很懒，什么都没留下");
                } else {
                    sign.setText(userInfo.getDes());
                }
            } else {
                //
                if (userInfo.getDes() == null) {
                    sign.setText("这家伙很懒，什么都没留下");
                } else {
                    if (userInfo.getDes().equals("未设置")) {
                        sign.setText("这家伙很懒，什么都没留下");
                    } else {
                        sign.setText(userInfo.getDes());
                    }
                }
                come_online.setText(userInfo.getMyname());
            }
        } else {
            comeonline = false;
            sign.setVisibility(View.INVISIBLE);
            exit.setVisibility(View.GONE);
            come_online.setText("登陆/注册");
        }
        flcontent.removeAllViews();
        flcontent.addView(view);
        personalListener();
    }

    private void personalListener() {
        //登陆或注册
        come_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                MyToast.makeText(mActivity, "请在互帮界面左滑，单击昵称更改信息", Toast.LENGTH_LONG).show();
                //CheckComeOnline();
            }
        });
        iv_setting_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.makeText(mActivity, "请在互帮界面左滑，单击昵称更改信息", Toast.LENGTH_LONG).show();
                //CheckComeOnline();
            }
        });
        //地址管理
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ReceiveAddressList.class);
                mActivity.startActivity(intent);
            }
        });
        //广告加盟
        add_AD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AdTogether.class);
                mActivity.startActivity(intent);
            }
        });
        //服务条款
        laws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, Laws.class);
                mActivity.startActivity(intent);
            }
        });
        //建议反馈
        feed_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mActivity, FeedBack.class);
                mActivity.startActivity(it);
            }
        });
        //关于
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mActivity.getApplicationContext(), About_Activity.class);
                mActivity.startActivity(it);
            }
        });
        //联系我
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mActivity.getApplicationContext(), ContactMe.class);
                mActivity.startActivity(it);
            }
        });
        //信息
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SystemMessage.class);
                mActivity.startActivity(intent);
            }
        });
        //退出
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialog();
            }
        });
        //钱包
        ll_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.makeText(mActivity,"暂未开通",Toast.LENGTH_SHORT).show();
            }
        });
        //我帮助的
        ll_i_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,HelpFenLei.class);
                intent.putExtra("location",7);
                mActivity.startActivity(intent);
            }
        });
        //帮助我的
        ll_help_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,HelpFenLei.class);
                intent.putExtra("location",6);
                mActivity.startActivity(intent);
            }
        });
    }
    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final android.support.v7.app.AlertDialog.Builder normalDialog =
                new android.support.v7.app.AlertDialog.Builder(mActivity);
        normalDialog.setIcon(R.drawable.exit);
        normalDialog.setTitle("离开");
        normalDialog.setMessage("确定要退出登陆吗?");
        normalDialog.setCancelable(false);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //SpUtils.putBoolean(mActivity,"changeID",true);
                        BmobUser.logOut();   //清除缓存用户对象
                        // BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
                        Intent intent =new Intent(mActivity,MainActivity.class);
                        mActivity.startActivity(intent);
                        mActivity.finish();
                        MyToast.makeText(mActivity,"退出成功",Toast.LENGTH_SHORT).show();
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