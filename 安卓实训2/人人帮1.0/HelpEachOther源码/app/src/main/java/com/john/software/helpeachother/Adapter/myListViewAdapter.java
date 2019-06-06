package com.john.software.helpeachother.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.john.software.helpeachother.Bean.People;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.ViewHolder.ViewHolder;

import java.util.List;

import static cn.bmob.v3.Bmob.getApplicationContext;

/*
 * 项目名:   PullLoadMore
 * 包名:     com.john.software.pullloadmore.Adapter
 * 文件名:   myListViewAdapter
 * 创建者:   software.John
 * 创建时间: 2019/5/8 20:49
 * 描述:      TODO
 */
public class myListViewAdapter extends BaseAdapter {
    List<People> peopleList;
    public myListViewAdapter(List<People>list){
        peopleList=list;
    }
    @Override
    public int getCount() {
        if(peopleList==null) {
            return 0;
        }else {
            return peopleList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder hodler;
        if(convertView==null) {
            convertView = View.inflate(getApplicationContext(), R.layout.peoplelist, null);
            hodler = new ViewHolder();
            hodler.school = convertView.findViewById(R.id.tv_school);
            hodler.name = convertView.findViewById(R.id.tv_name);
            hodler.sex = convertView.findViewById(R.id.tv_sex);
            convertView.setTag(hodler);
        }else{
            hodler=(ViewHolder)convertView.getTag();
        }
        if(peopleList==null){

        }else {
            hodler.school.setText(peopleList.get(position).getSchool());
            hodler.sex.setText(peopleList.get(position).getSex());
            hodler.name.setText(peopleList.get(position).getName());
        }



        return convertView;
    }
}
