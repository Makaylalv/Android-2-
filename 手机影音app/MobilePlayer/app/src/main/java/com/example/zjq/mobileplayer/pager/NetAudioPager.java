package com.example.zjq.mobileplayer.pager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zjq.mobileplayer.R;
import com.example.zjq.mobileplayer.adapter.NetAudioPagerAdapter;
import com.example.zjq.mobileplayer.base.BasePager;
import com.example.zjq.mobileplayer.bean.NetAudioPagerData;
import com.example.zjq.mobileplayer.utils.CacheUtils;
import com.example.zjq.mobileplayer.utils.Constants;
import com.example.zjq.mobileplayer.utils.LogUtil;
import com.example.zjq.mobileplayer.utils.Utils;
import com.example.zjq.mobileplayer.view.XListView;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Collection;
import java.util.List;

public class NetAudioPager extends BasePager {

    @ViewInject(R.id.listview)
    private ListView mlistview;

    @ViewInject(R.id.tv_nonet)
    private TextView tv_nonet;

    @ViewInject( R.id.pb_loading )
    private ProgressBar pb_loading;

    private NetAudioPagerAdapter adapter;


    //页面数据
    private List<NetAudioPagerData.ListBean> datas;

    private Utils utils;

    public NetAudioPager(Context context) {
        super( context );
    }

    @Override
    public View initView() {
        utils=new Utils();

        View view = View.inflate( context, R.layout.netaudio_pager, null );
        x.view().inject( this, view );


        return view;
    }



    @Override
    public void initDate() {
        super.initDate();


        String saveJson= CacheUtils.getString( context,Constants.ALL_RES_URL );
        if (!TextUtils.isEmpty( saveJson )){

            //解析数据
            processData(saveJson);
        }

        //联网
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams( Constants.ALL_RES_URL );


        x.http().get( params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                LogUtil.e( "请求数据成功" );
                //保存数据
                CacheUtils.putString(context,Constants.ALL_RES_URL,result  );

                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e( "请求数据失败" + ex.getMessage() );

            }

            @Override
            public void onCancelled(CancelledException cex) {

                LogUtil.e( "onCancelled" );


            }

            @Override
            public void onFinished() {
                LogUtil.e( "请求数据完成" );

            }
        } );
    }

    /**
     * 解析数据和显示数据
     * 1、用 GsonFormat 生成bean对象；
     * 2、用户Gson解析数据
     * @param json
     */
    private void processData(String json) {

            NetAudioPagerData data=parsedJson(json);
            datas=data.getList();


        if (datas!=null&&datas.size()>0){
            //有数据
            tv_nonet.setVisibility( View.GONE );

            //设置适配器

            adapter=new NetAudioPagerAdapter( context,datas );
            mlistview.setAdapter( adapter );
        }else {

            //没有数据
            tv_nonet.setText( "没有对应的数据..." );
            tv_nonet.setVisibility( View.VISIBLE );

        }

        pb_loading.setVisibility( View.GONE );





    }





    /**
     * GSON解析数据
     * @param json
     * @return
     */
    private NetAudioPagerData parsedJson(String json){


        return new Gson().fromJson( json,NetAudioPagerData.class );
    }
}
