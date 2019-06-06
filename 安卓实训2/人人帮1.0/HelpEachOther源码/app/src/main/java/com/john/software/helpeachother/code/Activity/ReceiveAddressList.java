package com.john.software.helpeachother.code.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.john.software.helpeachother.Bean.Address;
import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.LoadingDialog.LoadingDialog;
import com.john.software.helpeachother.code.Util.MyToast;
import com.john.software.helpeachother.code.Util.SpUtils.SpUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ReceiveAddressList extends AppCompatActivity {
    public ListView listView;
    private ImageButton back;
    private TextView title,guide;
    private List<Address> addresslist;
    public MyAddressList addressAdapter;
    private LoadingDialog loadingDialog;
    private Handler handler;
    private MyUser userInfo;
    private int from;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_address_list);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FF1A92E7"),false);
        guide= (TextView) findViewById(R.id.tv_address_guide);
        title= (TextView) findViewById(R.id.detial_tv_title);
        back= (ImageButton) findViewById(R.id.detial_btn_back);
        title.setText("收货地址");
        listView= (ListView) findViewById(R.id.lv_address_list);
        addressAdapter = new MyAddressList();
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if (userInfo != null) {
            //已登陆
            Intent it=getIntent();
            from=it.getIntExtra("from",0);
            //开启子线程，加载网络数据。
            OpenNetworkConnect();
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            //单击修改信息
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(from==0){
                        Intent intent=new Intent(ReceiveAddressList.this,AddNewAddress.class);
                        intent.putExtra("code","0");
                        intent.putExtra("sheng",addresslist.get(position).getSheng());
                        intent.putExtra("shi",addresslist.get(position).getShi());
                        intent.putExtra("school",addresslist.get(position).getSchool());
                        intent.putExtra("name",addresslist.get(position).getPersonname());
                        intent.putExtra("tel",addresslist.get(position).getTel());
                        intent.putExtra("des",addresslist.get(position).getAddressdes());
                        intent.putExtra("Id",addresslist.get(position).getObjectId());
                        intent.putExtra("username",userInfo.getUsername());
                        startActivityForResult(intent,0);
                    }else {
                        SpUtils.putString(ReceiveAddressList.this,"address",addresslist.get(position).getAddressdes()+"-"+
                                addresslist.get(position).getSchool()+"-" +addresslist.get(position).getSheng()+addresslist.get(position).getShi());
                        SpUtils.putString(ReceiveAddressList.this,"name",addresslist.get(position).getPersonname());
                        SpUtils.putString(ReceiveAddressList.this,"tel",addresslist.get(position).getTel());
                        Intent intent=new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }
            });
            //长按删除信息
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    showNormalDialog(position);
                    return true;
                }
            });
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
                    if(msg.what==1){
                        MyToast.makeText(ReceiveAddressList.this,
                                "暂无数据，不如新建一个吧", Toast.LENGTH_SHORT).show();
                        loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                    }else {
                        loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                        listView.setAdapter(addressAdapter);
                        setTopmessage();
                    }
                }
            };
        }else {
            //未登陆
            MyToast.makeText(ReceiveAddressList.this,"未登陆，请登陆后查询", Toast.LENGTH_SHORT).show();
        }
    }
    //设置listview顶部信息
    public void setTopmessage(){
        if (addresslist.size()!=0) {
            if(from==1){
                guide.setText("单击选择");
            }else {
                guide.setText("单击修改,长按删除");
            }
        }else{
            guide.setText("还没有收货地址，快新增一个吧");
        }
    }
    //开启子线程网络数据加载
    public void OpenNetworkConnect(){
        //开启progressDialog
        loadingDialog=new LoadingDialog();
        loadingDialog.startLoadingDialog(ReceiveAddressList.this,"加载中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String user=userInfo.getUsername();
                BmobQuery<Address> query = new BmobQuery<Address>();
                //查询playerName叫“比目”的数据
                query.addWhereEqualTo("username",user);
                //返回50条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(10);
                //执行查询方法
                query.findObjects(new FindListener<Address>() {
                    @Override
                    public void done(List<Address> object, BmobException e) {
                        if(e==null){
                            SpUtils.putString(ReceiveAddressList.this,"address",object.get(0).getAddressdes()+"-"+
                                    object.get(0).getSchool()+"-" +object.get(0).getSheng()+object.get(0).getShi());
                            SpUtils.putString(ReceiveAddressList.this,"name",object.get(0).getPersonname());
                            SpUtils.putString(ReceiveAddressList.this,"tel",object.get(0).getTel());
                            addresslist=object;
                            if(addresslist.size()==0){
                            }
                            handler.sendEmptyMessage(0);
                        }else{
                            Message message=new Message();
                            message.what=1;
                            handler.sendMessage(message);
                        }
                    }
                });
            }
        }).start();

    }
    //listview适配器
    public   class MyAddressList extends BaseAdapter {

        @Override
        public int getCount() {
            if (addresslist==null){
                return 0;
            }else {
                return addresslist.size();
            }
        }
        @Override
        public Object getItem(int position) {
            return addresslist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null) {
                convertView = View.inflate(ReceiveAddressList.this, R.layout.item_address_show_listview, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.tv_address_name);
                viewHolder.tel = (TextView) convertView.findViewById(R.id.tv_address_tel);
                viewHolder.address = (TextView) convertView.findViewById(R.id.tv_address_des);
                convertView.setTag(viewHolder);

            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText(addresslist.get(position).getPersonname());
            viewHolder.tel.setText(addresslist.get(position).getTel());
            viewHolder.address.setText(addresslist.get(position).getSheng()+" "+addresslist.get(position).getShi()
                    +" "+addresslist.get(position).getSchool()+" "+"("+addresslist.get(position).getAddressdes()+")");
            return convertView;
        }
    }
    //新增地址点击事件
    public void add_new_address(View view){
        Intent intent=new Intent(ReceiveAddressList.this,AddNewAddress.class);
        intent.putExtra("code","1");
        intent.putExtra("name","");
        intent.putExtra("username",userInfo.getUsername());
        intent.putExtra("tel","");
        intent.putExtra("des","");
        startActivityForResult(intent,0);
    }
    //长按删除提示框
    private void showNormalDialog(final int p){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(ReceiveAddressList.this);
        normalDialog.setIcon(R.drawable.delete);
        normalDialog.setTitle("删除");
        normalDialog.setMessage("确定要删除该地址信息吗?");
        normalDialog.setCancelable(false);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(){
                            @Override
                            public void run() {
                                Address addressa = new Address();
                                String id = addresslist.get(p).getObjectId();
                                addressa.setObjectId(id);
                                addressa.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            handler.sendEmptyMessage(0);
                                            addresslist.remove(p);
                                            MyToast.makeText(ReceiveAddressList.this,
                                                    "删除成功",Toast.LENGTH_SHORT).show();

                                        } else {
                                            Message message=new Message();
                                            message.what=1;
                                            handler.sendMessage(message);
                                        }
                                    }
                                });
                            }
                        }.start();
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
    static class ViewHolder {
        public TextView name;
        public TextView tel;
        public TextView address;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        OpenNetworkConnect();
    }
}

