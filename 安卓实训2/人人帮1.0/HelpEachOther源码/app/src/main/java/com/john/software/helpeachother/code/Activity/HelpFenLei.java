package com.john.software.helpeachother.code.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.john.software.helpeachother.Bean.Information;
import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.PtrClassicDefaultFooter;
import com.john.software.helpeachother.PtrDefaultHandler;
import com.john.software.helpeachother.PtrDefaultHandler2;
import com.john.software.helpeachother.PtrFrameLayout;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.LoadingDialog.LoadingDialog;
import com.john.software.helpeachother.code.Util.CountTimerUtils.TimeUtil;
import com.john.software.helpeachother.code.Util.MyToast;
import com.john.software.helpeachother.header.MaterialHeader;
import com.john.software.helpeachother.util.PtrLocalDisplay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class HelpFenLei extends AppCompatActivity {

    private TextView title;
    private ImageButton back;
    private Intent intent;
    private FrameLayout fl_context;
    private LoadingDialog loadingDialog;
    private int[] ints;
    private int i;
    private PtrFrameLayout mPtrFrame;
    private ListView listView;
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private int curPage = 0; // 当前页的编号，从0开始
    private MyUser userInfo;
    String lastTime = null;
    private int o, w;
    int positiona;
    private Handler handler;
    private Date date, datea;
    private myListViewAdapter myListViewAdapter;
    private List<Information> bankCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_fen_lei);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1296db"), false);
        back = (ImageButton) findViewById(R.id.detial_btn_back);
        title = (TextView) findViewById(R.id.detial_tv_title);
        fl_context = (FrameLayout) findViewById(R.id.fl_help_listview);
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        myListViewAdapter = new myListViewAdapter();
        intent = getIntent();
        i = intent.getIntExtra("location", 8);
        ints = new int[]{R.drawable.kuaidi_,
                R.drawable.waimai_,
                R.drawable.maidongxi_,
                R.drawable.xuexi_,
                R.drawable.qiandao_,
                R.drawable.bg_help_detial};
        switch (i) {
            case 0:
                title.setText("帮取快递");
                break;
            case 1:
                title.setText("帮取外卖");
                break;
            case 2:
                title.setText("帮买东西");
                break;
            case 3:
                title.setText("学习资料");
                break;
            case 4:
                title.setText("上课签到");
                break;
            case 5:
                title.setText("其他");
                break;
            case 6:
                title.setText("我发布的");
                break;
            case 7:
                title.setText("我帮助的");
                break;
        }
        bankCards = new ArrayList<Information>();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        View view = View.inflate(this, R.layout.help_fenlei_listview, null);
        listView = (ListView) view.findViewById(R.id.help_fenlei_listview);
        //listView.setAdapter(new myListViewAdapter());
        mPtrFrame = (PtrFrameLayout) view.findViewById(R.id.ptro);
        final MaterialHeader header = new MaterialHeader(HelpFenLei.this);
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 20);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        final PtrClassicDefaultFooter foot = new PtrClassicDefaultFooter(HelpFenLei.this);
        foot.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);
        mPtrFrame.setFooterView(foot);
        mPtrFrame.addPtrUIHandler(foot);
        fl_context.removeAllViews();
        fl_context.addView(view);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            //上拉加载更多
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        queryData(curPage, STATE_MORE);
                        // handler.sendEmptyMessage(0);
                        mPtrFrame.refreshComplete();
                        //listView.setSelection(bankCards.size()-5);
                        //mPtrFrame.refreshComplete();
                        //mPtrFrame.refreshComplete();
//                        listView.setAdapter(new myListViewAdapter());
//                        listView.setSelection(bankCards.size()-1);
                        //mPtrFrame.autoRefresh();//自动刷新
                    }
                }, 1000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            //设置下拉刷新数据
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        queryData(0, STATE_REFRESH);
                        mPtrFrame.refreshComplete();
                        //listView.setAdapter(new myListViewAdapter());
                        // listView.setSelection(0);
                        //mPtrFrame.autoRefresh();//自动刷新\
                    }
                }, 1000);

            }
        });
        //listview点击监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (i == 6) {
                    if (!bankCards.get(position).getFinish()) {
                        //取消订单
                        showNormalDialog(position);
                    } else if (bankCards.get(position).getOk()) {
                        MyToast.makeText(HelpFenLei.this, "该订单已完成", Toast.LENGTH_SHORT).show();
                    } else {
                        MyToast.makeText(HelpFenLei.this, "帮手已经行动，如需取消请与Ta联系解决", Toast.LENGTH_SHORT).show();
                    }
                } else if (i == 7) {
                    MyToast.makeText(HelpFenLei.this, "请到'记录'中查看", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        datea = sdf.parse(bankCards.get(position).getCreatedAt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Intent it = new Intent(HelpFenLei.this, HelpDetialActivity.class);
                    it.putExtra("name", bankCards.get(position).getName());
                    it.putExtra("types", bankCards.get(position).getTypes());
                    it.putExtra("tel", bankCards.get(position).getTel());
                    it.putExtra("things", bankCards.get(position).getThings());
                    it.putExtra("take_address", bankCards.get(position).getAddress_take());
                    it.putExtra("send_address", bankCards.get(position).getAddress_send());
                    it.putExtra("time_send", bankCards.get(position).getTime_send());
                    it.putExtra("des", bankCards.get(position).getSign());
                    it.putExtra("time", TimeUtil.getTimeFormatText(datea));
                    it.putExtra("password", bankCards.get(position).getPassword());
                    it.putExtra("object_id", bankCards.get(position).getObjectId());
                    startActivity(it);
                }
            }
        });
        if (userInfo == null) {
            MyToast.makeText(HelpFenLei.this, "未登录，暂时无法查询", Toast.LENGTH_SHORT).show();
        } else {
            if (userInfo.getSchool() == null) {
                MyToast.makeText(HelpFenLei.this, "未填写学校，暂时无法查询", Toast.LENGTH_SHORT).show();
            } else if (userInfo.getSchool().equals("未设置")) {
                MyToast.makeText(HelpFenLei.this, "未填写学校，暂时无法查询", Toast.LENGTH_SHORT).show();
            } else {
                GetDAtaOnNet();
            }
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
                if (msg.what == 1) {
                    MyToast.makeText(HelpFenLei.this,
                            "网络又偷懒了", Toast.LENGTH_SHORT).show();
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                } else {
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                    listView.setAdapter(new myListViewAdapter());
                    //setTopmessage();
                }
            }
        };
    }

    private void GetDAtaOnNet() {
        //开启progressDialog
        loadingDialog = new LoadingDialog();
        loadingDialog.startLoadingDialog(HelpFenLei.this, "加载中...");
        new Thread() {
            @Override
            public void run() {
                queryData(0, STATE_REFRESH);
            }
        }.start();
    }

    static class ViewHolder {
        public ImageView itemivIcon;
        public TextView itemwriter;
        public TextView updata;
        public TextView itemtvDesc;
        public TextView itemschool;
        public TextView itemtypes;
    }
    //真的获取网络数据
    private void queryData(int page, final int actionType) {

        BmobQuery<Information> query = new BmobQuery<>();
        if(i==6){
            query.addWhereEqualTo("tel",userInfo.getUsername() );
            // 按时间降序查询
            query.order("-updatedAt");
        }else if(i==7){
            query.addWhereEqualTo("helper",userInfo.getUsername() );
            query.order("-updatedAt");
        }else {
            query.addWhereEqualTo("school",userInfo.getSchool() );
            query.addWhereEqualTo("types", title.getText().toString());
            query.addWhereEqualTo("finish",false );
            query.addWhereEqualTo("ok",false );
            // 按时间降序查询
            query.order("-createdAt");
        }
        int count = 0;
        //加载更多
        if (actionType == STATE_MORE) {
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
                Log.i("时间", date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(i==6|i==7){
                // 只查询小于等于最后一个item发表时间的数据
                query.addWhereLessThanOrEqualTo("updatedAt", new BmobDate(date));

            }
            else {
                // 只查询小于等于最后一个item发表时间的数据
                query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));

            }
            // 跳过之前页数并去掉重复数据
            query.setSkip(page * count);

        }  else {
            // 下拉刷新
            page = 0;
            query.setSkip(page);
            //lastTime = bankCards.get(bankCards.size() - 1).getCreatedAt();
        }
        // 设置每页数据个数
        query.setLimit(15);
        // 查找数据
        query.findObjects(new FindListener<Information>() {

            @Override
            public void done(List<Information> list, BmobException e) {
                if(e==null){
                    //MyToast.makeText(HelpFenLei.this,String.valueOf(list.size()),Toast.LENGTH_SHORT).show();
                    if (list.size() >0) {

                        if (actionType == STATE_REFRESH) {
                            // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                            curPage = 0;
                            bankCards.clear();
                            o=0;
                            // 获取最后时间
                        }else {o=-110;}
                        // 将本次查询的数据添加到bankCards中
                        for (Information td : list) {
                            bankCards.add(td);
                        }
                        positiona = listView.getFirstVisiblePosition();
                        //MyToast.makeText(HelpFenLei.this,String.valueOf(positiona),Toast.LENGTH_SHORT).show();
                        listView.setAdapter(new myListViewAdapter());
//                        View v = listView.getChildAt(0);
//                        int top = (v == null) ? 0 : v.getTop();
                        listView.setSelectionFromTop(positiona,o);
                        if(i==6|i==7){
                            lastTime = bankCards.get(bankCards.size() - 1).getUpdatedAt();
                        }else {
                            lastTime = bankCards.get(bankCards.size() - 1).getCreatedAt();
                        }

                        loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog

//                       listView.setAdapter(new myListViewAdapter());
//                          positiona = listView.getFirstVisiblePosition();
                        // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                        curPage++;//页数加一
                        // p=1;
                    } else if (actionType == STATE_MORE) {
                        loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                        MyToast.makeText(HelpFenLei.this,"没有更多数据了",Toast.LENGTH_SHORT).show();
                        //showToast("没有更多数据了");
                    } else if (actionType == STATE_REFRESH) {
                        loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                        MyToast.makeText(HelpFenLei.this,"没有数据",Toast.LENGTH_SHORT).show();
                        //showToast("没有数据");
                    }
                    //关闭刷新
                    //mPullToRefreshView.onRefreshComplete();

                }else {
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                    MyToast.makeText(HelpFenLei.this,"网络又偷懒了，请检查网络连接",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //listview适配器
    class myListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if(bankCards==null){
                return 0;
            }else {
                return  bankCards.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return bankCards.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(HelpFenLei.this, R.layout.item_help_delivery_listview, null);
                holder = new ViewHolder();
                holder.itemtypes= (TextView) convertView.findViewById(R.id.help_delivery_tab);
                holder.itemivIcon = (ImageView) convertView.findViewById(R.id.help_delivery_image);
                holder.itemwriter = (TextView) convertView.findViewById(R.id.help_delivery_writer);
                holder.itemtvDesc = (TextView) convertView.findViewById(R.id.help_delivery_things);
                holder.updata = (TextView) convertView.findViewById(R.id.help_delivery_data);
                holder.itemschool= (TextView) convertView.findViewById(R.id.help_delivery_school);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            if(bankCards==null){
                //MyToast.makeText(HelpFenLei.this,"jj",Toast.LENGTH_SHORT).show();
            }else {
                switch (bankCards.get(position).getTypes()){
                    case "帮取快递":w =0;break;
                    case "帮取外卖":w =1;break;
                    case "帮买东西":w =2;break;
                    case "学习资料":w =3;break;
                    case "上课签到":w =4;break;
                    case "其他":w =5;break;
                }
                holder.itemwriter.setText(bankCards.get(position).getName());
                holder.itemtvDesc.setText("送到:"+bankCards.get(position).getAddress_send());
                holder.itemivIcon.setImageResource(ints[w]);
                if(i==6){
                    if(!bankCards.get(position).getFinish()){
                        holder.itemschool.setText("还未有帮手接单");
                        //holder.itemschool.setTextColor(Color.parseColor("#ff5f1a"));
                    }else if(bankCards.get(position).getOk()){
                        holder.itemschool.setText("已完成互帮");
                        // holder.itemschool.setTextColor(Color.parseColor("#000"));
                    }else {
                        holder.itemschool.setText("帮手正在帮您");
                        //holder.itemschool.setTextColor(Color.parseColor("#f00"));
                    }
                }else if(i==7){
                    if(!bankCards.get(position).getFinish()){
                        holder.itemschool.setText("还未有帮手接单");
                        // holder.itemschool.setTextColor(Color.parseColor("#ff5f1a"));
                    }else if(bankCards.get(position).getOk()){
                        holder.itemschool.setText("帮助完成");
                        // holder.itemschool.setTextColor(Color.parseColor("#000"));
                    }else {
                        holder.itemschool.setText("正在帮Ta");
                        // holder.itemschool.setTextColor(Color.parseColor("#f00"));
                    }

                }else  {
                    holder.itemschool.setText(bankCards.get(position).getSchool());
                }
                holder.itemtypes.setText(bankCards.get(position).getTypes());
                date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    date = sdf.parse(bankCards.get(position).getCreatedAt());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.updata.setText(TimeUtil.getTimeFormatText(date));
            }
            //holder.updata.setText(listViews.get(position));
            // mBitmapUtils.display(holder.itemivIcon, String.valueOf(Imagepagerlists.get(0)));
            return convertView;

        }
    }
    //长按删除提示框
    private void showNormalDialog(final int p){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(HelpFenLei.this);
        normalDialog.setIcon(R.drawable.delete);
        normalDialog.setTitle("删除");
        normalDialog.setMessage("确定要删除该条信息吗?(钱款无法退回)");
        normalDialog.setCancelable(false);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //开启progressDialog
                        loadingDialog=new LoadingDialog();
                        loadingDialog.startLoadingDialog(HelpFenLei.this,"加载中...");
                        new Thread(){
                            @Override
                            public void run() {
                                Information information = new Information();
                                String id = bankCards.get(p).getObjectId();
                                information.setObjectId(id);
                                information.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            handler.sendEmptyMessage(0);
                                            bankCards.remove(p);

                                            MyToast.makeText(HelpFenLei.this,
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

}

