package com.john.software.helpeachother.code.Record_ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.john.software.helpeachother.Bean.Information;
import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.PtrClassicDefaultFooter;
import com.john.software.helpeachother.PtrDefaultHandler;
import com.john.software.helpeachother.PtrDefaultHandler2;
import com.john.software.helpeachother.PtrFrameLayout;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.Activity.HelpDetialActivity;
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

/*
 * 项目名:   PullLoadMore
 * 包名:     com.john.software.pullloadmore.code.Record_ViewPager
 * 文件名:   HelpingThemRecordViewPager
 * 创建者:   software.John
 * 创建时间: 2019/5/28 8:35
 * 描述:      TODO
 */
public class HelpingThemRecordViewPager extends BaseRecordViewPager {
    private PtrFrameLayout mPtrFrame;
    private ListView lv_record;
    private MyUser userInfo;
    private TextView detial_tv_title;
    private Handler handler;
    private List<Information> list;
    private Date date,datea;
    private int o,p,i;
    private int[] ints;
    private char symbol=165;//人民币符号
    String lastTime = null;
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private int curPage = 0; // 当前页的编号，从0开始
    int  positiona;
    private boolean t=false;
    public HelpingThemRecordViewPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        return super.initView();
    }
    @Override
    public void initData() {
        View view =View.inflate( mActivity, R.layout.record_helping_pager,null);
        mPtrFrame = (PtrFrameLayout) view.findViewById(R.id.ptr);
        // tv_no_message= (TextView) view.findViewById(R.id.tv_show_record);
        //tv_no_message.setText("下拉查看");
        lv_record= (ListView) view.findViewById(R.id.lv_record_helping);
        userInfo= BmobUser.getCurrentUser(MyUser.class);
        ints=new int[]{R.drawable.kuaidi_,
                R.drawable.waimai_,
                R.drawable.maidongxi_,
                R.drawable.xuexi_,
                R.drawable.qiandao_,
                R.drawable.bg_help_detial};
        list = new ArrayList<Information>();
        FreashSetting();
        fl_record_pager.removeAllViews();
        fl_record_pager.addView(view);
        lv_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    datea= sdf.parse(list.get(position).getCreatedAt());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent it = new Intent(mActivity, HelpDetialActivity.class);
                it.putExtra("p",1);
                it.putExtra("name",list.get(position).getName());
                it.putExtra("types",list.get(position).getTypes());
                it.putExtra("tel",list.get(position).getTel());
                it.putExtra("things",list.get(position).getThings());
                it.putExtra("take_address",list.get(position).getAddress_take());
                it.putExtra("send_address",list.get(position).getAddress_send());
                it.putExtra("time_send",list.get(position).getTime_send());
                it.putExtra("des",list.get(position).getSign());
                it.putExtra("time", TimeUtil.getTimeFormatText(datea));
                it.putExtra("password",list.get(position).getPassword());
                it.putExtra("object_id",list.get(position).getObjectId());
                mActivity.startActivity(it);
            }
        });
        if(userInfo!=null){
            if(userInfo.getSchool()==null){}
            else if(userInfo.getSchool().length()==0){
            }else {

                queryData(0, STATE_REFRESH);
            }}
        else{}
    }
    private void FreashSetting() {
        final MaterialHeader header = new MaterialHeader(mActivity);
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 20);
        //经典上拉加载更多
        final PtrClassicDefaultFooter foot = new PtrClassicDefaultFooter(mActivity);
        foot.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.setFooterView(foot);
        //mPtrFrame.setPinContent(true);//刷新时，保持内容不动，仅头部下移,默认,false
        mPtrFrame.disableWhenHorizontalMove(true);//如果是ViewPager，设置为true，会解决ViewPager滑动冲突问题。
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.addPtrUIHandler(foot);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2(){
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(userInfo!=null){
                            if(userInfo.getSchool()==null){}
                            else if(userInfo.getSchool().length()==0){
                            }else {if(t){
                                queryData(curPage, STATE_MORE);
                            }
                            }}
                        else{}
                        // handler.sendEmptyMessage(0);
                        mPtrFrame.refreshComplete();
                        //mPtrFrame.autoRefresh();//自动刷新
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            //需要加载数据时触发
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                System.out.println("MainActivity.onRefreshBegin");
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(userInfo!=null){
                            if(userInfo.getSchool()==null){}
                            else if(userInfo.getSchool().length()==0){
                            }else {
                                t=true;
                                queryData(0, STATE_REFRESH);
                            }}
                        else{}
                        mPtrFrame.refreshComplete();
                        //mPtrFrame.autoRefresh();//自动刷新
                    }
                }, 1000);
            }
        });
    }
    private void queryData(int page, final int actionType) {
        BmobQuery<Information> query = new BmobQuery<>();
        // 按时间降序查询
        query.addWhereEqualTo("helper",userInfo.getUsername());
        query.addWhereEqualTo("finish",true );
        query.addWhereEqualTo("ok",false);
        query.order("-updatedAt");
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
            // 只查询小于等于最后一个item发表时间的数据
            query.addWhereLessThanOrEqualTo("updatedAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            query.setSkip(page * count);

        }  else {
            // 下拉刷新
            page = 0;
            query.setSkip(page);
            //lastTime = list.get(list.size() - 1).getCreatedAt();
        }
        // 设置每页数据个数
        query.setLimit(15);
        // 查找数据
        query.findObjects(new FindListener<Information>() {
            @Override
            public void done(List<Information> listi, BmobException e) {
                if(e==null){
                    //MyToast.makeText(HelpFenLei.this,String.valueOf(list.size()),Toast.LENGTH_SHORT).show();
                    if (listi.size() >0) {
                        //MyToast.makeText(mActivity,String .valueOf(listi.size()),Toast.LENGTH_SHORT).show();
                        if (actionType == STATE_REFRESH) {
                            // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                            curPage = 0;
                            list.clear();
                            o=0;
                            // 获取最后时间
                        }else {o=-110;}
                        // 将本次查询的数据添加到bankCards中
                        for (Information td : listi) {
                            list.add(td);
                        }
                        positiona = lv_record.getFirstVisiblePosition();
                        lv_record.setAdapter(new myListViewAdapter());
//                        View v = listView.getChildAt(0);
//                        int top = (v == null) ? 0 : v.getTop();
                        lv_record.setSelectionFromTop(positiona,o);
                        lastTime = list.get(list.size() - 1).getUpdatedAt();
//                       listView.setAdapter(new myListViewAdapter());
//                          positiona = listView.getFirstVisiblePosition();
                        // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                        curPage++;//页数加一
                        //tv_no_message.setVisibility(View.GONE);
                    } else if (actionType == STATE_MORE) {
                        MyToast.makeText(mActivity,"没有更多数据了", Toast.LENGTH_SHORT).show();
                        //showToast("没有更多数据了");
                    } else if (actionType == STATE_REFRESH) {
                        //tv_no_message.setText("暂无数据");
                        if(t){
                            list.clear();
                            lv_record.setAdapter(new myListViewAdapter());
                            MyToast.makeText(mActivity,"没有数据",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    MyToast.makeText(mActivity,"网络又偷懒了，请检查网络连接",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    static class ViewHolder {
        public ImageView itemivIcon;
        public TextView itemwriter;
        public TextView updata;
        public TextView itemtvDesc;
        public TextView itemtypes;
    }
    //listview适配器
    class myListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if(list==null){
                return 0;
            }else {
                return  list.size();
            }
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

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_helping, null);
                holder = new ViewHolder();
                holder.itemtypes= (TextView) convertView.findViewById(R.id.help_delivery_tab);
                holder.itemivIcon = (ImageView) convertView.findViewById(R.id.help_delivery_image);
                holder.itemwriter = (TextView) convertView.findViewById(R.id.help_delivery_writer);
                holder.itemtvDesc = (TextView) convertView.findViewById(R.id.help_delivery_things);
                holder.updata = (TextView) convertView.findViewById(R.id.help_delivery_data);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            if(list==null){

            }else {
                switch (list.get(position).getTypes()){
                    case "帮取快递":i=0;break;
                    case "帮取外卖":i=1;break;
                    case "帮买东西":i=2;break;
                    case "学习资料":i=3;break;
                    case "上课签到":i=4;break;
                    case "其他":i=5;break;
                }
                holder.itemwriter.setText(list.get(position).getName());
                holder.itemtvDesc.setText("送到:"+list.get(position).getAddress_send());
                holder.itemtypes.setText(list.get(position).getTypes());
                holder.updata.setText(list.get(position).getTel());
                holder.itemivIcon.setImageResource(ints[i]);
            }
            return convertView;
        }
    }

}

