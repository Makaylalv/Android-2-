package com.john.software.helpeachother.code.Pagers;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.john.software.helpeachother.PtrDefaultHandler;
import com.john.software.helpeachother.PtrDefaultHandler2;
import com.john.software.helpeachother.PtrFrameLayout;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.Activity.HelpDetialActivity;
import com.john.software.helpeachother.code.Activity.HelpFenLei;
import com.john.software.helpeachother.code.Activity.PersonalCenterActivity;
import com.john.software.helpeachother.code.Activity.ReceiveAddressList;
import com.john.software.helpeachother.code.LoadingDialog.LoadingDialog;
import com.john.software.helpeachother.code.Util.CountTimerUtils.TimeUtil;
import com.john.software.helpeachother.code.Util.MyToast;
import com.john.software.helpeachother.code.Util.SpUtils.FirstCome;
import com.john.software.helpeachother.code.Util.ViewChangeUtils.TopHelperViewPager;
import com.john.software.helpeachother.header.MaterialHeader;
import com.john.software.helpeachother.util.PtrLocalDisplay;
import com.john.software.helpeachother.viewPagerIndicatorLibrary.CirclePageIndicator;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/*
 * 项目名:   PullLoadMore
 * 包名:     com.john.software.pullloadmore.code.Pagers
 * 文件名:   HelpPager
 * 创建者:   software.John
 * 创建时间: 2019/5/10 14:18
 * 描述:      TODO
 */
public class HelpPager extends BasePager {
    private ArrayList<String> listViews;
    private ArrayList<ImageView> Imagepagerlists;
    private int[] pagerlists;
    private Handler mhandler;
    private int po;
    private PtrFrameLayout mPtrFrame;
    private List<Information> list;
    private CirclePageIndicator mIndicator;
    private BitmapUtils mBitmapUtils = new BitmapUtils(mActivity);;
    private ListView listView;
    private TextView item_kuaidi,item_waimai,item_things,item_study,
            item_qiandao,item_others,item_address,item_settings,
            item_types;
    private char symbol=165;//人民币符号
    private TopHelperViewPager viewPager;
    private Intent it;
    private String s;
    private Handler handler;
    private MyUser userInfo;
    private View Footview;
    private int[] ints;
    private int pos;
    private Date datea;
    private LoadingDialog loadingDialog;
    public HelpPager(Activity activity) {
        super(activity);
    }

    public void init() {
        View view = View.inflate(mActivity, R.layout.help_delivery, null);
        listView = (ListView) view.findViewById(R.id.help_delivery_listview);
        ViewUtils.inject(this, view);
        View headerview_viewpager = View.inflate(mActivity, R.layout.item_listview_viewpager_header, null);
        mIndicator= (CirclePageIndicator) headerview_viewpager.findViewById(R.id.indicator);
        ViewUtils.inject(this, headerview_viewpager);
        View headerview_choose=View.inflate(mActivity,R.layout.item_listview_headerview_choose,null);
        ViewUtils.inject(this, headerview_choose);
        Footview=View.inflate(mActivity,R.layout.helppager_foot_layout,null);
        item_kuaidi= (TextView) headerview_choose.findViewById(R.id.item_kuaidi);
        item_kuaidi.setOnClickListener(new item_onclick());
        item_waimai= (TextView) headerview_choose.findViewById(R.id.item_waimai);
        item_waimai.setOnClickListener(new item_onclick());
        item_things=(TextView) headerview_choose.findViewById(R.id.item_things);
        item_things.setOnClickListener(new item_onclick());
        item_study=(TextView) headerview_choose.findViewById(R.id.item_study);
        item_study.setOnClickListener(new item_onclick());
        item_qiandao=(TextView) headerview_choose.findViewById(R.id.item_qiandao);
        item_qiandao.setOnClickListener(new item_onclick());
        item_others=(TextView) headerview_choose.findViewById(R.id.item_others);
        item_others.setOnClickListener(new item_onclick());
        item_address=(TextView) headerview_choose.findViewById(R.id.item_address);
        item_address.setOnClickListener(new item_onclick());
        item_settings=(TextView) headerview_choose.findViewById(R.id.item_settings);
        item_settings.setOnClickListener(new item_onclick());
        //给listview加入headerview
        listView.addHeaderView(headerview_viewpager);
        listView.addHeaderView(headerview_choose);
        listView.addFooterView(Footview);
        mPtrFrame = (PtrFrameLayout) view.findViewById(R.id.ptr);
        viewPager = (TopHelperViewPager) headerview_viewpager.findViewById(R.id.vp_delivery_tab);
        //刷新 经典 风格的头部实现
        //final PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mActivity);
        //header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);
        //Material Design下拉刷新风格的头部
        final MaterialHeader header = new MaterialHeader(mActivity);
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 20);
        //经典上拉加载更多
//        final PtrClassicDefaultFooter foot = new PtrClassicDefaultFooter(mActivity);
//        foot.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);
        mPtrFrame.setHeaderView(header);
        //mPtrFrame.setFooterView(foot);
        //mPtrFrame.setPinContent(true);//刷新时，保持内容不动，仅头部下移,默认,false
        mPtrFrame.disableWhenHorizontalMove(true);//如果是ViewPager，设置为true，会解决ViewPager滑动冲突问题。
        mPtrFrame.addPtrUIHandler(header);
        it=new Intent(mActivity, HelpFenLei.class);
        // mPtrFrame.addPtrUIHandler(foot);
        flcontent.removeAllViews();
        flcontent.addView(view);
        ints=new int[]{R.drawable.kuaidi_,
                R.drawable.waimai_,
                R.drawable.maidongxi_,
                R.drawable.xuexi_ ,
                R.drawable.qiandao_,
                R.drawable.bg_help_detial,};
    }
    private void GetDataFromNet() {
        loadingDialog=new LoadingDialog();
        loadingDialog.startLoadingDialog(mActivity,"加载中...");
        new Thread(){
            @Override
            public void run() {
                BmobQuery<Information> query = new BmobQuery<Information>();
                //查询playerName叫“比目”的数据
                query.addWhereEqualTo("school",userInfo.getSchool() );
                query.addWhereEqualTo("finish",false );
                query.addWhereEqualTo("ok",false );
//返回50条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(8);
                query.order("-createdAt");
//执行查询方法
                query.findObjects(new FindListener<Information>() {
                    @Override
                    public void done(List<Information> object, BmobException e) {
                        if(e==null) {
                            if (object == null) {
                                Message message=new Message();
                                message.what=2;
                                handler.sendMessage(message);
                            } else if(object.size() == 0){
                                Message message=new Message();
                                message.what=2;
                                handler.sendMessage(message);
                            }else {
                                // toast("查询成功：共"+object.size()+"条数据。");
                                for (Information information:object) {
                                    list = object;
                                    handler.sendEmptyMessage(0);
                                    //获得playerName的信息
                                    //gameScore.getPlayerName();
                                    //获得数据的objectId信息
                                    //gameScore.getObjectId();
                                    //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                                    // gameScore.getCreatedAt();
                                }
                            }
                        }else{
                            Message message=new Message();
                            message.what=1;
                            handler.sendMessage(message);
                            //Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        }.start();
    }

    @Override
    public void initData() {
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if(userInfo!=null){
            if(userInfo.getSchool()==null){
                tvTitle.setText("未填写学校");}
            else if(userInfo.getSchool().length()==0){
                tvTitle.setText("未填写学校");
            }else {
                tvTitle.setText(userInfo.getSchool());
            }}
        else {tvTitle.setText("未登录");}
        init();

        mPtrFrame.setPtrHandler(new PtrDefaultHandler2(){
            //上拉加载更多
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                        //mPtrFrame.autoRefresh();//自动刷新
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            //设置下拉刷新数据
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                System.out.println("MainActivity.onRefreshBegin");
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(userInfo==null){
                            MyToast.makeText(mActivity,"还未登录呢！",Toast.LENGTH_SHORT).show();
                            mPtrFrame.refreshComplete();
                        }else if(userInfo.getSchool()==null){
                            mPtrFrame.refreshComplete();
                            MyToast.makeText(mActivity,"未填写学校，请填写后再刷新",Toast.LENGTH_LONG).show();
                        }
                        else if(userInfo.getSchool().length()==0){
                            mPtrFrame.refreshComplete();
                            MyToast.makeText(mActivity,"未填写学校，请填写后再刷新",Toast.LENGTH_LONG).show();
                        }
                        else {
                            BmobQuery<Information> query = new BmobQuery<Information>();
                            //查询playerName叫“比目”的数据
                            query.addWhereEqualTo("school",userInfo.getSchool() );
                            query.addWhereEqualTo("finish",false );
                            query.addWhereEqualTo("ok",false );
                            query.order("-createdAt");
                            //MyToast.makeText(mActivity,userInfo.getSchool(),Toast.LENGTH_SHORT).show();
                            //返回50条数据，如果不加上这条语句，默认返回10条数据
                            query.setLimit(8);
                            //执行查询方法
                            query.findObjects(new FindListener<Information>() {
                                @Override
                                public void done(List<Information> object, BmobException e) {
                                    if(e==null) {
                                        if (object == null) {
                                            Message message=new Message();
                                            message.what=2;
                                            handler.sendMessage(message);
                                            list=null;
                                        } else if(object.size() == 0){
                                            Message message=new Message();
                                            message.what=2;
                                            list = null;
                                            handler.sendMessage(message);
                                        }else {
                                            // toast("查询成功：共"+object.size()+"条数据。");
                                            for (Information information:object) {
                                                list = object;
                                                handler.sendEmptyMessage(0);
                                                //获得playerName的信息
                                                //gameScore.getPlayerName();
                                                //获得数据的objectId信息
                                                //gameScore.getObjectId();
                                                //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                                                // gameScore.getCreatedAt();
                                            }
                                        }
                                    }else{
                                        Message message=new Message();
                                        message.what=1;
                                        handler.sendMessage(message);
                                        //Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });
                        }
                        //mPtrFrame.autoRefresh();//自动刷新
                    }
                }, 500);
            }
        });
        //ViewPager初始化数据
        Imagepagerlists=new ArrayList<>();
        pagerlists= new int[]{R.drawable.viewpager1,R.drawable.viewpager2
                ,R.drawable.viewpager3,R.drawable.viewpager5};
        for(int i=0;i<pagerlists.length;i++)
        {
            ImageView imageView=new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(pagerlists[i]);
            Imagepagerlists.add(imageView);
        }
        viewPager.setAdapter(new DeliveryAdapter());
        if(mhandler==null){
            mhandler= new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    int count=viewPager.getCurrentItem();
                    count++;
                    if(count>Imagepagerlists.size()-1){
                        count=0;
                    }
                    viewPager.setCurrentItem(count,true);
                    //  viewPager.setCurrentItem(count);
                    mhandler.sendEmptyMessageDelayed(0,4000);
                }
            };
            mhandler.sendEmptyMessageDelayed(0,4000);
        }
        mIndicator.setViewPager(viewPager);
        mIndicator.setSnap(true);// 快照方式展示

        // 事件要设置给Indicator
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // 更新头条新闻标题
            }
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // 更新第一个头条新闻标题
        mIndicator.onPageSelected(0);// 默认让第一个选中(解决页面销毁后重新初始化时,Indicator仍然保留上次圆点位置的bug)
        // ListView初始化数据
        listView.setAdapter(new myListViewAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==po|position==1) {
                    //阻断底部与顶部单击响应
                }else {
                    if(userInfo==null){
                        MyToast.makeText(mActivity,"还未登录呢！",Toast.LENGTH_SHORT).show();
                        mPtrFrame.refreshComplete();
                    }else if(userInfo.getSchool()==null){
                        mPtrFrame.refreshComplete();
                        MyToast.makeText(mActivity,"未填写学校，完善后才可以帮助他人哦",Toast.LENGTH_LONG).show();
                    }
                    else if(userInfo.getSchool().length()==0){
                        mPtrFrame.refreshComplete();
                        MyToast.makeText(mActivity,"未填写学校，完善后才可以帮助他人哦",Toast.LENGTH_LONG).show();
                    }
                    else {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            datea= sdf.parse(list.get(position-2).getCreatedAt());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Intent it = new Intent(mActivity, HelpDetialActivity.class);
                        it.putExtra("name",list.get(position-2).getName());
                        it.putExtra("types",list.get(position-2).getTypes());
                        it.putExtra("tel",list.get(position-2).getTel());
                        it.putExtra("things",list.get(position-2).getThings());
                        it.putExtra("take_address",list.get(position-2).getAddress_take());
                        it.putExtra("send_address",list.get(position-2).getAddress_send());
                        it.putExtra("time_send",list.get(position-2).getTime_send());
                        it.putExtra("des",list.get(position-2).getSign());
                        it.putExtra("time",TimeUtil.getTimeFormatText(datea));
                        it.putExtra("password",list.get(position-2).getPassword());
                        it.putExtra("object_id",list.get(position-2).getObjectId());
                        mActivity.startActivity(it);
//                       Intent it = new Intent(mActivity, HelpDetialActivity.class);
//                       mActivity.startActivity(it);
                    }
                }
            }
        });
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if(userInfo==null) {
        }else {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
                    if (msg.what == 1) {
                        po=2;
                        MyToast.makeText(mActivity,
                                "哎，网络又偷懒了，下拉刷新试试", Toast.LENGTH_SHORT).show();
                        mPtrFrame.refreshComplete();
                        loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                    } else if(msg.what==2){
                        po=2;
                        listView.setAdapter(new myListViewAdapter());
                        mPtrFrame.refreshComplete();
                        //listView.setFooterDividersEnabled(false);
                        //listView.removeFooterView(Footview);
                        loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                        MyToast.makeText(mActivity,"该学校暂无同学发布信息，快做第一名",Toast.LENGTH_LONG).show();
                    }else {
                        po=list.size()+2;
                        //listView.setFooterDividersEnabled(true);
                        //listView.addFooterView(Footview);
                        listView.setAdapter(new myListViewAdapter());
                        mPtrFrame.refreshComplete();
                        loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                    }
                }
            };
            if(FirstCome.first){
                FirstCome.first = false;
                GetDataFromNet();

            }
        }
    }
    //ViewPager适配器
    class DeliveryAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pagerlists.length;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view =Imagepagerlists.get(position);
            container.addView(view);
            return view;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    //ListView适配器
    class myListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if(list==null){
                return 0;
            }else if(list.size()>9){
                return 8;
            }else {
                return list.size();
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
                convertView = View.inflate(mActivity, R.layout.item_help_delivery_listview, null);
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
            if(list==null){

            }else {
                holder.itemwriter.setText(list.get(position).getName());
                holder.itemtvDesc.setText("送到:"+list.get(position).getAddress_send());
                switch (list.get(position).getTypes()){
                    case "帮取快递":pos =0;break;
                    case "帮取外卖":pos =1;break;
                    case "帮买东西":pos =2;break;
                    case "学习资料":pos =3;break;
                    case "上课签到":pos =4;break;
                    case "其他":pos =5;break;
                }
                holder.itemivIcon.setImageResource(ints[pos]);
                holder.itemschool.setText(list.get(position).getSchool());
                holder.itemtypes.setText(list.get(position).getTypes());
                Date date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    date = sdf.parse(list.get(position).getCreatedAt());
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
    static class ViewHolder {
        public ImageView itemivIcon;
        public TextView itemwriter;
        public TextView updata;
        public TextView itemtvDesc;
        public TextView itemschool;
        public TextView itemtypes;
    }
    class item_onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_kuaidi:StartActivity(0);break;
                case R.id.item_waimai:StartActivity(1);break;
                case R.id.item_things:StartActivity(2); break;
                case R.id.item_study:StartActivity(3);break;
                case R.id.item_qiandao:StartActivity(4); break;
                case R.id.item_others:StartActivity(5);break;
                case R.id.item_address:Intent intent=new Intent(mActivity, ReceiveAddressList.class);
                    mActivity.startActivity(intent);break;
                case R.id.item_settings:
                    Intent it= new Intent(mActivity, PersonalCenterActivity.class);
                    it.putExtra("num",1);
                    FirstCome.leftenter=true;
                    //MyToast.makeText(mActivity,"暂未开通，敬请期待",Toast.LENGTH_SHORT).show();
                    mActivity.startActivity(it); break;
            }
        }
    }
    public void  StartActivity(int i){
        it.putExtra("location",i);
        mActivity.startActivity(it);
    }

}
