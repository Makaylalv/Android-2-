package com.john.software.helpeachother.code.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.john.software.helpeachother.MainActivity;
import com.john.software.helpeachother.R;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {
    ViewPager mviewpager;
    Button mbtn;
    private ArrayList<ImageView> mImageViewlist;
    private int[]mImageIds=new int[]{R.mipmap.one,R.mipmap.two,R.mipmap.three};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);


        /*   android:fitsSystemWindows="true"*/

        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);}


        mbtn=findViewById(R.id.btn_start);
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click","yes");
                Intent it=new Intent(GuideActivity.this, MainActivity.class);
                startActivity(it);
                finish();
            }
        });
        mviewpager=findViewById(R.id.vp_guide);
        initData();
        mviewpager.setAdapter(new GudiePager());
        mviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.i("tag",String.valueOf(i));
                if(i==2){
                    mbtn.setVisibility(View.VISIBLE);
                }else{
                    mbtn.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {


            }
        });
    }
    private void initData(){
        mImageViewlist=new ArrayList<>();
        for(int i=0;i<mImageIds.length;i++){
            ImageView view=new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);
            mImageViewlist.add(view);
        }
    }
    class GudiePager extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageViewlist.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }
        //初始化item

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView view=mImageViewlist.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }
}
