package com.john.software.helpeachother.code.Record_ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.john.software.helpeachother.code.Activity.PersonalCenterActivity;
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
 * 文件名:   HelpingMeRecordViewPager
 * 创建者:   software.John
 * 创建时间: 2019/5/28 9:14
 * 描述:      TODO
 */
public class HelpingMeRecordViewPager extends BaseRecordViewPager {
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
    public HelpingMeRecordViewPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        return super.initView();
    }

    @Override
    public void initData() {
        View view =View.inflate(mActivity, R.layout.record_havedone_pager,null);
        mPtrFrame = (PtrFrameLayout) view.findViewById(R.id.ptr0);
        lv_record= (ListView) view.findViewById(R.id.lv_record_helping_me);
        userInfo= BmobUser.getCurrentUser(MyUser.class);
        list = new ArrayList<Information>();
        FreashSetting();
        fl_record_pager.removeAllViews();
        fl_record_pager.addView(view);
        lv_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(mActivity, PersonalCenterActivity.class);
                intent.putExtra("id",list.get(position).getHelper());
                mActivity.startActivity(intent);
            }
        });
        if(userInfo!=null){
            if(userInfo.getSchool()==null){}
            else if(userInfo.getSchool().length()==0){
            }else {
                queryData(0, STATE_REFRESH);
            }}
        else{}
        mPtrFrame.refreshComplete();
    }
    private void FreashSetting() {
        final MaterialHeader header = new MaterialHeader(mActivity);
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 20);
        //经典上拉加载更多
        final PtrClassicDefaultFooter foot = new PtrClassicDefaultFooter(mActivity);
        foot.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.setFooterView(foot);
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
                //System.out.println("MainActivity.onRefreshBegin");
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
        query.addWhereEqualTo("tel",userInfo.getUsername());
        query.addWhereEqualTo("finish",true);
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
                        if (t) {
                            MyToast.makeText(mActivity, "没有数据", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    MyToast.makeText(mActivity,"网络又偷懒了，请检查网络连接",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    static class ViewHolder {
        // public TextView itemwriter;
        public TextView itemtvDesc;
        public TextView itemtypes;
        public TextView time_send;
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
            if (convertView == null){
                convertView = View.inflate(mActivity, R.layout.item_helping_me, null);
                holder = new ViewHolder();
                holder.itemtypes= (TextView) convertView.findViewById(R.id.help_delivery_ta);
                //holder.itemwriter = (TextView) convertView.findViewById(R.id.help_delivery_write);
                //对方电话
                holder.itemtvDesc = (TextView) convertView.findViewById(R.id.help_delivery_thin);
                holder.time_send= (TextView) convertView.findViewById(R.id.help_delivery_data);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            if(list==null){

            }else {
                //holder.itemwriter.setText(list.get(position).getName());
                holder.itemtvDesc.setText(list.get(position).getHelper());
                holder.itemtypes.setText(list.get(position).getTypes());
                holder.time_send.setText(list.get(position).getTime_send());
            }
            return convertView;
        }
    }
}
