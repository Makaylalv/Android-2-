package com.john.software.helpeachother.code.Pagers;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.Record_ViewPager.BaseRecordViewPager;
import com.john.software.helpeachother.code.Record_ViewPager.HelpingMeRecordViewPager;
import com.john.software.helpeachother.code.Record_ViewPager.HelpingThemRecordViewPager;

import java.util.ArrayList;

/*
 * 项目名:   PullLoadMore
 * 包名:     com.john.software.pullloadmore.code.Pagers
 * 文件名:   RecordPager
 * 创建者:   software.John
 * 创建时间: 2019/5/10 14:20
 * 描述:      TODO
 */
public class RecordPager extends BasePager {
    private ViewPager viewPager;
    private View view1 ,view ;
    private ArrayList<BaseRecordViewPager> mBaseRecordPager;
    private RadioGroup radioGroup;
    private RadioButton helping,have_finish;
    public RecordPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        return super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        //让toolbar消失
        tvTitle.setText("帮助记录");

        relativeLayout.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.INVISIBLE);
        view=View.inflate(mActivity, R.layout.record_layout,null);
        mFindviewbiID();
        viewPager= (ViewPager) view.findViewById(R.id.vp_record);
        initViewPager();
        viewPager.setAdapter(new RecordAdapter());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        helping.setChecked(true);
                        break;
                    case 1:
                        have_finish.setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rg_helping:
                        viewPager.setCurrentItem(0,true);
                        break;
                    case R.id.rg_have_finish:
                        viewPager.setCurrentItem(1,true);
                        break;
                }
            }
        });
        flcontent.removeAllViews();
        flcontent.addView(view);
    }
    //初始化ID
    private void mFindviewbiID() {
        radioGroup= (RadioGroup) view.findViewById(R.id.rg_record);
        helping= (RadioButton) view.findViewById(R.id.rg_helping);
        have_finish= (RadioButton) view.findViewById(R.id.rg_have_finish);
        //no_finish= (RadioButton) view.findViewById(R.id.rg_no_finish);
        //sixin= (RadioButton) view.findViewById(R.id.rg_sixin);
    }
    //初始化ViewPager
    public void initViewPager(){
        mBaseRecordPager=new ArrayList<>();
        mBaseRecordPager.add(new HelpingThemRecordViewPager(mActivity));
        mBaseRecordPager.add(new HelpingMeRecordViewPager(mActivity));
    }
    //viewpager适配器
    class RecordAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mBaseRecordPager.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            view1=mBaseRecordPager.get(position).rootView;
            mBaseRecordPager.get(position).initData();
            container.addView(view1);
            return view1;
        }
    }
}