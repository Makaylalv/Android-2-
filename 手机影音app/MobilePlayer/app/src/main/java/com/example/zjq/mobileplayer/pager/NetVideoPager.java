package com.example.zjq.mobileplayer.pager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zjq.mobileplayer.R;
import com.example.zjq.mobileplayer.activity.SystemVideoPlayer;
import com.example.zjq.mobileplayer.adapter.NetVideoPagerAdapter;
import com.example.zjq.mobileplayer.adapter.VideoPagerAdapter;
import com.example.zjq.mobileplayer.base.BasePager;
import com.example.zjq.mobileplayer.bean.MediaItem;
import com.example.zjq.mobileplayer.utils.CacheUtils;
import com.example.zjq.mobileplayer.utils.Constants;
import com.example.zjq.mobileplayer.utils.LogUtil;
import com.example.zjq.mobileplayer.utils.Utils;
import com.example.zjq.mobileplayer.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NetVideoPager extends BasePager {


    @ViewInject(R.id.listview)
    private XListView mListView;

    @ViewInject(R.id.tv_nonet)
    private TextView mTv_nonet;

    @ViewInject(R.id.pb_loading)
    private ProgressBar mProgressBar;

    private ArrayList<MediaItem> mediaItems;

    private NetVideoPagerAdapter netVideoPagerAdapter;

    private Utils utils;

    private boolean isLoadMore;


    public NetVideoPager(Context context) {
        super( context );
    }

    @Override
    public View initView() {
        utils=new Utils();

        View view = View.inflate( context, R.layout.netvideo_pager, null );

        //xUtils实例化布局 第一个参数不是上下文，第二个参数是实例化的布局
        x.view().inject( this, view );

        //设置listview Item的点击事件
        mListView.setOnItemClickListener( new NetVideoPager.MyOnItemClickListener() );

        //下拉刷新
        mListView.setPullLoadEnable( true );
        mListView.setXListViewListener( new MyIXListViewListener() );



        return view;
    }

    class MyIXListViewListener implements XListView.IXListViewListener {


        @Override
        public void onRefresh() {

            getDataFromNet();

        }

        @Override
        public void onLoadMore() {
            getMoreDataFromNet();

        }
    }

    private void getMoreDataFromNet() {

        RequestParams params = new RequestParams( Constants.NET_URL );
        x.http().get( params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                LogUtil.e( "加载成功==" + result );

                isLoadMore=true;
                //解析数据 主线程 很快
                processData( result );
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e( "加载失败==" + ex.getMessage() );

                isLoadMore=false;
            }

            @Override
            public void onCancelled(CancelledException cex) {

                LogUtil.e( "取消加载==" + cex.getMessage() );

                isLoadMore=false;
            }

            @Override
            public void onFinished() {
                LogUtil.e( "加载完成==" );

                isLoadMore=false;
            }
        } );

    }


    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("更新时间:"+utils.getSystemTime());
    }


    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //3.传列表，传递列表数据-对象的话，需要序列化
            Intent intent = new Intent( context, SystemVideoPlayer.class );
            Bundle bundle = new Bundle();
            bundle.putSerializable( "videolist", mediaItems );
            intent.putExtras( bundle );
            intent.putExtra( "position", position-1 );
            context.startActivity( intent );
        }
    }


    @Override
    public void initDate() {
        super.initDate();
     /*   LogUtil.e( "网络视频页面数据被初始化了" );
        textView.setText( "网络视频页面" );*/

        String saveJson=CacheUtils.getString( context,Constants.NET_URL );
        if (!TextUtils.isEmpty( saveJson )){
            processData( saveJson );

        }

        getDataFromNet();


    }

    private void getDataFromNet() {
        //视频内容
        RequestParams params = new RequestParams( Constants.NET_URL );
        x.http().get( params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                LogUtil.e( "联网成功==" + result );

                //缓存数据
                CacheUtils.putString( context,Constants.NET_URL,result );


                //解析数据 主线程 很快
                processData( result );
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e( "联网失败==" + ex.getMessage() );

                showData();

            }

            @Override
            public void onCancelled(CancelledException cex) {

                LogUtil.e( "onCancelled==" + cex.getMessage() );

            }

            @Override
            public void onFinished() {
                LogUtil.e( "联网完成==" );

            }
        } );
    }

    private void processData(Object json) {

        if (!isLoadMore){
            //下拉刷新
            mediaItems = parseJson( json );
            showData();


        }else {
            //上滑加载更多，要把得到更多的数据添加到原来的集合中

            isLoadMore=false;

            mediaItems.addAll( parseJson( json ) );

            //刷新适配器
            netVideoPagerAdapter.notifyDataSetChanged();

            onLoad();
        }

    }

    private void showData() {
        //设置适配器
        if (mediaItems != null && mediaItems.size() > 0) {
            //有数据
            //设置适配器

            netVideoPagerAdapter = new NetVideoPagerAdapter( context, mediaItems );

            mListView.setAdapter( netVideoPagerAdapter );

            onLoad();

            //把文本和progressBar隐藏
            mTv_nonet.setVisibility( View.GONE );
        } else {

            //没有数据，文本显示
            mTv_nonet.setVisibility( View.VISIBLE );

        }
        mProgressBar.setVisibility( View.GONE );
    }

    //解析json数据

    /**
     * 1.用系统接口解析json数据
     * 2.使用第三方解析工具（GSON）
     *
     * @param json
     * @return
     */
    private ArrayList<MediaItem> parseJson(Object json) {

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        try {


            JSONObject jsonObject = new JSONObject( (String) json );
            //用getJsonArray的话，key不存在会奔溃，optJsonArray 不会
            JSONArray jsonArray = jsonObject.optJSONArray( "trailers" );
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectItem = (JSONObject) jsonArray.get( i );

                    if (jsonObjectItem != null) {

                        MediaItem mediaItem = new MediaItem();

                        String movieName = jsonObjectItem.optString( "movieName" );
                        mediaItem.setName( movieName );

                        String videoTitle = jsonObjectItem.optString( "videoTitle" );
                        mediaItem.setDesc( videoTitle );

                        String imageUrl = jsonObjectItem.optString( "coverImg" );
                        mediaItem.setImageUrl( imageUrl );

                        String highUrl = jsonObjectItem.optString( "hightUrl" );
                        mediaItem.setData( highUrl );

                        //添加到集合中
                        mediaItems.add( mediaItem );
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mediaItems;
    }
}
