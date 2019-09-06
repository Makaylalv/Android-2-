package com.example.zjq.mobileplayer.pager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zjq.mobileplayer.R;
import com.example.zjq.mobileplayer.activity.SystemVideoPlayer;
import com.example.zjq.mobileplayer.activity.VitamioVideoPlayer;
import com.example.zjq.mobileplayer.adapter.VideoPagerAdapter;
import com.example.zjq.mobileplayer.base.BasePager;
import com.example.zjq.mobileplayer.bean.MediaItem;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class VideoPager extends BasePager{


    private ListView listView;
    private  TextView tv_nomedia;
    private ProgressBar pb_loading;

    private ArrayList<MediaItem> mediaItems;
    VideoPagerAdapter videoPagerAdapter;



    public VideoPager(Context context){
        super(context);


    }

    private Handler handler=new Handler(  ){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );
            if(mediaItems!=null &&mediaItems.size()>0){
                //有数据
                //设置适配器

                videoPagerAdapter=new VideoPagerAdapter(context,mediaItems,true);
                listView.setAdapter(  videoPagerAdapter);
                //把文本和progressBar隐藏

                tv_nomedia.setVisibility( View.GONE );
            }else {

                //没有数据，文本显示
                tv_nomedia.setVisibility( View.VISIBLE );

            }
            pb_loading.setVisibility( View.GONE  );

        }
    };

    @Override
    public View initView() {

        View view=View.inflate( context, R.layout.video_pager,null );
        listView=view.findViewById( R.id.listview );

        //设置listview Item的点击事件
        listView.setOnItemClickListener( new MyOnItemClickListener() );

        tv_nomedia=view.findViewById( R.id.tv_nomedia );
        pb_loading=view.findViewById( R.id.pb_loading );

        return view;
    }





    class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


         /*   //1.调起所有播放器
            //6.0
            Intent intent = new Intent();
            intent.setDataAndType( Uri.parse( mediaItem.getData() ), "video/*" );
            context.startActivity(intent);*/

         //2.调自己的播放器（传一个）
            /*Intent intent = new Intent(context,SystemVideoPlayer.class);
            intent.setDataAndType( Uri.parse( mediaItem.getData() ), "video/*" );
            context.startActivity(intent);*/


            //3.传列表，传递列表数据-对象的话，需要序列化
            Intent intent = new Intent(context,VitamioVideoPlayer.class);
            Bundle bundle=new Bundle(  );
            bundle.putSerializable( "videolist",mediaItems );
            intent.putExtras( bundle );
            intent.putExtra( "position",position );
            context.startActivity(intent);
        }
    }

    @Override
    public void initDate() {
        super.initDate();


        //加载本地数据
        getDataFromLocal();


    }


    /**
     * 从本地sdcard得到数据
     * 1、遍历sdcard
     * 2.从内容提供者里面获取
     *3.如果是6.0，需要动态获取权限
     */

    private void getDataFromLocal() {

        mediaItems=new ArrayList<>(  );


        new Thread( ){
            @Override
            public void run() {
                super.run();



                //内容解析者
               ContentResolver resolver= context.getContentResolver();
               Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
               String[] objs={

                       MediaStore.Video.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                       MediaStore.Video.Media.DURATION,//视频总时长
                       MediaStore.Video.Media.SIZE,//视频文件大小
                       MediaStore.Video.Media.DATA,//视频的绝对地址
                       MediaStore.Video.Media.ARTIST,//歌曲的演唱者
               };

            Cursor cursor= resolver.query( uri,objs,null,null,null);
            if (cursor!=null){
                while (cursor.moveToNext()){

                    MediaItem mediaItem=new MediaItem();

                    mediaItems.add( mediaItem );//写在上面也可以

                    String name=cursor.getString( 0 );
                    mediaItem.setName( name );

                    Long duration=cursor.getLong( 1 );
                    mediaItem.setDuration( duration );

                    Long size=cursor.getLong( 2 );
                    mediaItem.setSize( size );

                    String data=cursor.getString( 3 );
                    mediaItem.setData( data );

                    String artist=cursor.getString( 4 );
                    mediaItem.setArtist( artist );

                }
                cursor.close();

            }

            //Handler发消息
                handler.sendEmptyMessage( 1 );

            }
        }.start();
    }



}
