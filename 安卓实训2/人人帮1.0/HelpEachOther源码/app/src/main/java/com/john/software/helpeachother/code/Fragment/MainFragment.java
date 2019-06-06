package com.john.software.helpeachother.code.Fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.githang.statusbar.StatusBarCompat;
import com.john.software.helpeachother.MainActivity;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.Pagers.BasePager;
import com.john.software.helpeachother.code.Pagers.ForhelpPager;
import com.john.software.helpeachother.code.Pagers.HelpPager;
import com.john.software.helpeachother.code.Pagers.RecordPager;
import com.john.software.helpeachother.code.Pagers.SettingPager;
import com.john.software.helpeachother.code.Util.SpUtils.FirstCome;
import com.john.software.helpeachother.code.Util.ViewChangeUtils.NoScrollViewPager;

import java.util.ArrayList;

/*
 * 项目名:   PullLoadMore
 * 包名:     com.john.software.pullloadmore.code.Fragment
 * 文件名:   MainFragment
 * 创建者:   software.John
 * 创建时间: 2019/5/9 8:44
 * 描述:      TODO
 */
public class MainFragment extends BaseFragMent {
    private NoScrollViewPager mViewpager;
    private RadioGroup rggroup;
    private ArrayList<BasePager> mPagers;
    public String mes; //setting登陆文字
    private myViewPager my;

    @Override
    public View initView() {
        //默认使ViewPager进入help页面并开启侧滑功能
        setSlidingMenu(true);

        View view = View.inflate(mActivity, R.layout.main_fragment, null);

        mViewpager = (NoScrollViewPager) view.findViewById(R.id.vp_fragment);
        rggroup = (RadioGroup) view.findViewById(R.id.rg_group);
        StatusBarCompat.setStatusBarColor(mActivity, Color.parseColor("#FF1A92E7"),false);
        rggroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_help:

                        mViewpager.setCurrentItem(0, false);

                        setSlidingMenu(true);

                        StatusBarCompat.setStatusBarColor(mActivity, Color.parseColor("#FF1A92E7"), false);

                        break;
                    case R.id.rb_forhelp:
                        mViewpager.setCurrentItem(1, false);
                        setSlidingMenu(false);
                        StatusBarCompat.setStatusBarColor(mActivity, Color.parseColor("#FF1A92E7"), false);
                        break;
                    case R.id.rb_record:
                        mViewpager.setCurrentItem(2, false);
                        setSlidingMenu(false);
                       StatusBarCompat.setStatusBarColor(mActivity, Color.parseColor("#FF1A92E7"), false);

                        break;
                    case R.id.rb_setting:
                        mViewpager.setCurrentItem(3, false);
                        setSlidingMenu(false);
                        //系统状态栏颜色
                        StatusBarCompat.setStatusBarColor(mActivity, Color.parseColor("#FF1A92E7"), false);
                        break;
                }

            }
        });
        return view;
    }

    private void setSlidingMenu(boolean b) {
        MainActivity mainActivity = (MainActivity) mActivity;
        mainActivity.getSlidingMenu().setSlidingEnabled(b);
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mPagers.add(new HelpPager(mActivity));
            mPagers.add(new ForhelpPager(mActivity));
            mPagers.add(new RecordPager(mActivity));
            mPagers.add(new SettingPager(mActivity));
        }
        my = new myViewPager();
        mViewpager.setAdapter(my);
    }

    //    public HelpPager getHelpPager() {
//        HelpPager helppager= (HelpPager) mPagers.get(0);
//        return helppager;
//    }
    class myViewPager extends PagerAdapter {


        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagers.get(position);
            pager.initData();
            container.addView(pager.rootview);






            return pager.rootview;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
//        if(userInfo != null){
//            if(userInfo.getSchool()==null){
//
//            }else {
//
//            }
//            // 允许用户使用应用
//        }else {
//            // MyToast.makeText(mActivity,"未登陆",Toast.LENGTH_SHORT).show();
//
//        }
//    }
    @Override
    public void onResume() {
        super.onResume();
        if(FirstCome.first){}else {
            if(FirstCome.leftenter){
                mViewpager.setAdapter(my);
                FirstCome.leftenter=false;
            }
        }
    }
}

