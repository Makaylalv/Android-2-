package com.john.software.helpeachother;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.john.software.helpeachother.Adapter.myListViewAdapter;
import com.john.software.helpeachother.Bean.People;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Main2Activity extends AppCompatActivity {
    List<People>peopleList;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bmob.initialize(this,"b3646f4197dc70a1bb71fccf86025339");
        listView=findViewById(R.id.listview);
        GetDataFormNeet();

    }
    private void GetDataFormNeet(){
        new Thread(){
            public void run(){
                BmobQuery<People> query=new BmobQuery<>();
                query.addWhereEqualTo("school","河北师范");
                query.setLimit(8);
                query.order("-createAt");

                query.findObjects(new FindListener<People>() {
                    @Override
                    public void done(List<People> list, BmobException e) {
                        if(e==null){
                            Log.e("Find","success");
                            peopleList=list;
                            listView.setAdapter(new myListViewAdapter(peopleList));

                        }else{
                            Log.e("Find",e.toString());
                        }
                    }
                });
            }
        }.start();
    }

}
