package com.john.software.helpeachother.code.Fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.Activity.AboutMe;
import com.john.software.helpeachother.code.Activity.About_Activity;
import com.john.software.helpeachother.code.Activity.FeedBack;
import com.john.software.helpeachother.code.Activity.PersonalCenterActivity;
import com.john.software.helpeachother.code.Activity.ReceiveAddressList;
import com.john.software.helpeachother.code.Activity.Registration_or_Landing;
import com.john.software.helpeachother.code.Activity.SystemMessage;
import com.john.software.helpeachother.code.Util.MyToast;
import com.john.software.helpeachother.code.Util.SpUtils.FirstCome;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;

/*
 * 项目名:   PullLoadMore
 * 包名:     com.john.software.pullloadmore.code.Fragment
 * 文件名:   LeftMenuFragMent
 * 创建者:   software.John
 * 创建时间: 2019/5/10 10:14
 * 描述:      TODO
 */
public class LeftMenuFragMent extends BaseFragMent {
    public FrameLayout flcontent;
    private LinearLayout linearLayout;
    private ListView lv_left;
    private int[]item;
    private ImageView item_imageView;
    private String m;
    private ArrayList<String> item_listView;
    public TextView item_textView,item_lv_detial,tv_personname;
    private boolean login;
    @Override
    public View initView() {
        //侧边界面
        View view=View.inflate(mActivity, R.layout.left_menu_fragment,null);
        flcontent=view.findViewById(R.id.fl_content);
        linearLayout=view.findViewById(R.id.ll_left);
        lv_left=view.findViewById(R.id.lv_left_person_center);
        tv_personname=view.findViewById(R.id.tv_personname);
        return view;
    }

    @Override
    public void initData() {
        m="20M";
        //检测登陆
        MyUser userInfo= BmobUser.getCurrentUser(MyUser.class);
        if(userInfo!=null){
            if(userInfo.getMyname()==null){
                tv_personname.setText(userInfo.getUsername());
            }else{
                tv_personname.setText(userInfo.getMyname());
            }
            //允许用户使用应用
            tv_personname.setText("未登陆");
        }
        //设置left页面侧边图片资源
        item=new int[]{R.drawable.item_left_address,R.drawable.item_message,R.drawable.item_left_fankui,
        R.drawable.item_left_know,R.drawable.item_left_delete,R.drawable.left_content,R.drawable.exit};
        item_listView=new ArrayList<>();
        item_listView.add("收货地址");
        item_listView.add("我的消息");
        item_listView.add("意见反馈");
        item_listView.add("了解人人帮");
        item_listView.add("清除缓存");
        item_listView.add("关于");
        item_listView.add("退出");
        //实体化listview适配器 并开启适配
        class Item extends BaseAdapter {
            @Override
            public int getCount() {
                return item_listView.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view=View.inflate(mActivity,R.layout.item_left_menu_listview,null);
                item_textView= (TextView) view.findViewById(R.id.tv_item_left_menu_listview);
                item_imageView= (ImageView) view.findViewById(R.id.iv_left_item);
                item_lv_detial= (TextView) view.findViewById(R.id.item_lv_detial);
                if(position==4){item_lv_detial.setText(m);}
                if(position==6){item_lv_detial.setText("安全离开");}
                item_imageView.setImageResource(item[position]);
                item_textView.setText(item_listView.get(position));
                return view;
            }
        }
        final Item item=new Item();
        lv_left.setAdapter(item);
        lv_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(mActivity,position+"点击了！",Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:  Intent intent=new Intent(mActivity,ReceiveAddressList.class);
                        mActivity.startActivity(intent);
                        break;
                    case 1:  Intent iint=new Intent(mActivity, SystemMessage.class);
                        mActivity.startActivity(iint);
                        break;
                    case 2:  Intent it=new Intent(mActivity, FeedBack.class);
                        mActivity.startActivity(it);
                        break;
                    case 3:   Intent ift=new Intent(mActivity, AboutMe.class);
                        mActivity.startActivity(ift);
                        break;
                    case 4:  m="0M";
                        item.notifyDataSetChanged();
                        MyToast.makeText(mActivity,"清理完成！",Toast.LENGTH_SHORT).show();

                        break;
                    case 5:   Intent iwt= new Intent(mActivity.getApplicationContext(), About_Activity.class);
                        mActivity.startActivity(iwt);
                        break;
                    case 6:   Exit();
                        break;
                }
            }
        });
        //个人中心开启监听
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否登陆 如果登陆-》进入个人中心-------否则，进入登陆界面
               MyUser bmobUser=BmobUser.getCurrentUser(MyUser.class);
               if(bmobUser!=null){
                   //允许用户使用应用 进入个人中心
                   login=true;
                   Intent it=new Intent(mActivity,PersonalCenterActivity.class);
                   it.putExtra("num",1);
                   FirstCome.leftenter=true;
                   mActivity.startActivity(it);
               }else{
                   //登陆
                   Intent it=new Intent(mActivity, Registration_or_Landing.class);
                   mActivity.startActivityForResult(it,2);
                   mActivity.finish();
               }
            }
        });
    }


    private void Exit() {
        //退出
        MyToast.makeText(mActivity,"即将关闭,欢迎下次使用!",Toast.LENGTH_LONG).show();
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    System.exit(0);
                }
            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        MyUser userInfo=BmobUser.getCurrentUser(MyUser.class);
        if(userInfo!=null) {
            if (userInfo.getMyname() == null)
                tv_personname.setText(userInfo.getUsername());
            else if (userInfo.getMyname().equals("未设置昵称")) {
                tv_personname.setText(userInfo.getUsername());
            } else {
                tv_personname.setText(userInfo.getMyname());
            }
            //允许用户使用应用
        }else{
            tv_personname.setText("未登录");
        }
    }
}
