package com.example.zjq.mobileplayer.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zjq.mobileplayer.R;
import com.example.zjq.mobileplayer.bean.MediaItem;
import com.example.zjq.mobileplayer.utils.Utils;

import java.util.ArrayList;

public class  VideoPagerAdapter extends BaseAdapter {

    private final boolean isVideo;
    private Context context;
    //装数据集合
    private ArrayList<MediaItem> mediaItems;

    private Utils utils;

    public VideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems,boolean isVideo){
        this.context=context;
        this.mediaItems=mediaItems;
        this.isVideo=isVideo;
        utils=new Utils();
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=View.inflate( context, R.layout.item_video_pager,null );
        }

        ImageView iv_icon=convertView.findViewById( R.id.iv_icon );
        TextView tv_name=convertView.findViewById( R.id.tv_name );
        TextView tv_time=convertView.findViewById( R.id.tv_time );
        TextView tv_size=convertView.findViewById( R.id.tv_size );

        MediaItem mediaItem= mediaItems.get( position );

        tv_name.setText( mediaItem.getName() );
        tv_size.setText(  Formatter.formatFileSize( context,mediaItem.getSize()));
        tv_time.setText( utils.stringForTime( (int) mediaItem.getDuration() ) );


        if (!isVideo){
            //音频

            iv_icon.setImageResource( R.drawable.music_default_bg );
        }
        return convertView;
    }
}
