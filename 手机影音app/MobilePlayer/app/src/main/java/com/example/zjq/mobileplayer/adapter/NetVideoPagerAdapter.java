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

import org.xutils.x;

import java.util.ArrayList;

/**
 * 网络适配器
 */
public class NetVideoPagerAdapter extends BaseAdapter {

    private Context context;
    //装数据集合
    private ArrayList<MediaItem> mediaItems;



    public NetVideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems){
        this.context=context;
        this.mediaItems=mediaItems;
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
            convertView=View.inflate( context, R.layout.item_netvideo_pager,null );
        }

        ImageView iv_icon=convertView.findViewById( R.id.iv_icon );
        TextView tv_name=convertView.findViewById( R.id.tv_name );
        TextView tv_desc=convertView.findViewById( R.id.tv_desc );

        MediaItem mediaItem= mediaItems.get( position );

        tv_name.setText( mediaItem.getName() );
        tv_desc.setText( mediaItem.getDesc() );

        //xUtils
        x.image().bind( iv_icon,mediaItem.getImageUrl() );
        return convertView;
    }
}
