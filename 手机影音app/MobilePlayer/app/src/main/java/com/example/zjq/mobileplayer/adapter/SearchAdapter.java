package com.example.zjq.mobileplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zjq.mobileplayer.R;
import com.example.zjq.mobileplayer.bean.MediaItem;
import com.example.zjq.mobileplayer.bean.SearchBean;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SearchAdapter extends BaseAdapter {

    private Context context;
    //装数据集合
    private List<SearchBean.ItemData> mediaItems;



    public SearchAdapter(Context context, List<SearchBean.ItemData> mediaItems){
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

        SearchBean.ItemData ItemData= mediaItems.get( position );

        tv_name.setText( ItemData.getItemTitle() );
        tv_desc.setText( ItemData.getKeywords() );

        //xUtils
        x.image().bind( iv_icon,ItemData.getItemImage().getImgUrl1() );
        return convertView;
    }
}
