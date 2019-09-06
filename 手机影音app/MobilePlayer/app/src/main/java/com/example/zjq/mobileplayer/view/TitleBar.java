package com.example.zjq.mobileplayer.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.zjq.mobileplayer.R;
import com.example.zjq.mobileplayer.activity.SearchActivity;

public class TitleBar extends LinearLayout implements View.OnClickListener {

    private View tv_search;
    private View rl_game;
    private View iv_record;
    private Context context;

    //代码中创建时用
    public TitleBar(Context context) {
        this( context,null );
    }

    //布局文件用
    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this( context, attrs,0 );
    }

    //设置样式时,最终调用这个方法
    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );

        this.context=context;
    }


    //当布局文件加载完成的时候，回调这个方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //得到孩子的实例(第0个是logo)
        tv_search=getChildAt( 1 );
        rl_game=getChildAt( 2 );
        iv_record=getChildAt( 3 );

        //设置点击事件
        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener( this );
        iv_record.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tv_search:

                //Toast.makeText( context,"搜索",Toast.LENGTH_SHORT ).show();

                Intent intent=new Intent( context,SearchActivity.class );
                context.startActivity( intent );

                break;
            case R.id.rl_game:
                Toast.makeText( context,"游戏",Toast.LENGTH_SHORT ).show();

                break;
            case R.id.iv_record:
                Toast.makeText( context,"播放历史",Toast.LENGTH_SHORT ).show();

                break;
        }
    }
}
