package com.john.software.helpeachother.code.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Person;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.Bean.School;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.LoadingDialog.LoadingDialog;
import com.john.software.helpeachother.code.Util.MyToast;
import com.john.software.helpeachother.code.Util.MyToastForBlue;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class PersonalCenterActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private TextView school, tel, sex, address, sign, name, tv_show;
    private ImageView iv_person;
    private ImageButton button_edit;
    private LinearLayout ll_school, ll_tel, ll_sex, ll_address, ll_sign;
    private boolean q = true;
    private EditText edit_text, edit_text1;
    private int yourChoice;
    private Handler handler;
    private LoadingDialog loadingDialog;
    private String[] schoollist;
    private Mycityy mycity;
    private String id;
    private MyUser userInfo;
    private CityChooseDialog1 cityChooseDialog;
    private ListView show_city;

    private School schools;
    private ArrayAdapter Myadapter;


    //00
    //private String[] searcoh = {"hhhhh", "cccccc", "wwwwww", "ddddd", "aaaaa"};
    private SearchView main_searchview;
    private ListView main_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.layout_personal_centerll );
        StatusBarCompat.setStatusBarColor( this, Color.parseColor( "#FF1A92E7" ), false );


        linearLayout = (LinearLayout) findViewById( R.id.personalL );
        school = findViewById( R.id.tv_school );
        school = (TextView) findViewById( R.id.tv_school );
        tel = (TextView) findViewById( R.id.tv_tel );
        sex = (TextView) findViewById( R.id.tv_sex );
        address = (TextView) findViewById( R.id.tv_address );
        sign = (TextView) findViewById( R.id.tv_sign );
        name = (TextView) findViewById( R.id.tv_name );
        iv_person = (ImageView) linearLayout.findViewById( R.id.iv_person );
        button_edit = (ImageButton) linearLayout.findViewById( R.id.ib_editt );
        ll_school = (LinearLayout) findViewById( R.id.ll_school );
        ll_tel = (LinearLayout) findViewById( R.id.ll_tel );
        ll_sex = (LinearLayout) findViewById( R.id.ll_sex );
        ll_address = (LinearLayout) findViewById( R.id.ll_address );
        ll_sign = (LinearLayout) findViewById( R.id.ll_sign );

        mycity = new Mycityy();

        userInfo = BmobUser.getCurrentUser( MyUser.class );

        schools=new School();
        Myadapter=new ArrayAdapter( PersonalCenterActivity.this , android.R.layout.simple_list_item_1,schools.getSchool());

        if (userInfo != null) {
            Intent ity = getIntent();
            int i = ity.getIntExtra( "num", 0 );
            if (i == 1) {
                id = userInfo.getUsername();
                button_edit.setVisibility( View.VISIBLE );
            } else {
                id = ity.getStringExtra( "id" );
                button_edit.setVisibility( View.GONE );
            }
            //获取网络数据
            GetDataFromNet();


            //点击监听配置
            OpenOnlisten();


            //设置控件单击响应
            SetClick( false );
            //监听编辑按钮变化
            button_edit.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (q) {
                        schoollist = null;
                        schoollist = schools.getSchool();

                        SetClick( true );
                        MyToastForBlue.makeText( PersonalCenterActivity.this, "已进入编辑页面，单击即可编辑", Toast.LENGTH_LONG ).show();
                        button_edit.setBackgroundResource( R.drawable.finish );
                        q = !q;
                    } else

                    {
                        SetClick( false );
                        button_edit.setBackgroundResource( R.drawable.edit );
                        //发送数据至网络以更新
                        OpenNetworkConnect();
                        q = !q;
                    }
                }
            });
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
                    if (msg.what == 1) {
                        MyToastForBlue.makeText( PersonalCenterActivity.this,
                                "哎，网络又偷懒了，请稍后再试", Toast.LENGTH_SHORT ).show();
                        loadingDialog.closeLoadingDialog( loadingDialog.loadingDialog );// 关闭ProgressDialog
                    } else {
                        loadingDialog.closeLoadingDialog( loadingDialog.loadingDialog );// 关闭ProgressDialog
                    }
                }
            };
        }else

        {
            MyToastForBlue.makeText( PersonalCenterActivity.this, "还未登陆呦", Toast.LENGTH_SHORT ).show();
        }

    }


    //开启网络连接准备上传数据
    public void OpenNetworkConnect() {
        //开启progressDialog
        loadingDialog = new LoadingDialog();
        loadingDialog.startLoadingDialog( PersonalCenterActivity.this, "加载中..." );
        new Thread( new Runnable() {
            @Override
            public void run() {
                PutIntnet();
            }
        } ).start();

    }

    public void back(View view) {
        finish();
    }

    //上传网络数据
    private void PutIntnet() {
        MyUser newUser = new MyUser();
        newUser.setMyname( name.getText().toString() );
        newUser.setSchool( school.getText().toString() );
        newUser.setSex( sex.getText().toString() );
        newUser.setDes( sign.getText().toString() );
        newUser.setMyaddress( address.getText().toString() );
        newUser.setMytel( tel.getText().toString() );
        BmobUser bmobUser = BmobUser.getCurrentUser( MyUser.class );
        newUser.update( bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    handler.sendEmptyMessage( 0 );
                    MyToastForBlue.makeText( PersonalCenterActivity.this, "信息更改成功", Toast.LENGTH_LONG );
                } else {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage( message );
                }
            }
        } );
    }

    //开启与关闭点击
    private void SetClick(boolean b) {
        if (b) {
            ll_school.setClickable( true );
            iv_person.setClickable( true );
            name.setClickable( true );
            ll_tel.setClickable( true );
            ll_sex.setClickable( true );
            ll_sign.setClickable( true );
            ll_address.setClickable( true );
        } else {
            ll_school.setClickable( false );
            iv_person.setClickable( false );
            name.setClickable( false );
            ll_tel.setClickable( false );
            ll_sex.setClickable( false );
            ll_sign.setClickable( false );
            ll_address.setClickable( false );
        }
    }

    //自定义通用提示框
    private void showCustomizeDialog(String des, final TextView textView) {
        TextView text = new TextView( PersonalCenterActivity.this );
        text.setText( des );
        text.setTextSize( 16 );
        text.setPadding( 0, 0, 0, 15 );
        text.setGravity( Gravity.CENTER );
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder( PersonalCenterActivity.this );
        final View dialogView = LayoutInflater.from( PersonalCenterActivity.this ).inflate( R.layout.dialog_customize, null );
        edit_text = dialogView.findViewById( R.id.edit_text );
        edit_text.setText( textView.getText() );
        edit_text.setSelection( textView.length() );//将光标移至文字末尾
        customizeDialog.setCustomTitle( text );
        customizeDialog.setView( dialogView );
        customizeDialog.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //获取EditView中输入内容
                if (edit_text.getText().length() != 0 | textView.getText().equals( edit_text.getText() )) {
                    textView.setText( edit_text.getText() );
                } else {
                    MyToastForBlue.makeText( PersonalCenterActivity.this, "信息未更改", Toast.LENGTH_LONG ).show();
                }
            }
        } );
        customizeDialog.show();
    }

    //性别选择框
    private void showSexDialog() {
        TextView text = new TextView( PersonalCenterActivity.this );
        text.setTextSize( 16 );
        text.setText( "选择性别：（要认真哦）" );
        text.setPadding( 0, 40, 0, 15 );
        text.setGravity( Gravity.CENTER );
        final String[] items = {"男", "女", "保密"};
        yourChoice = 0;
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder( PersonalCenterActivity.this );
        singleChoiceDialog.setCustomTitle( text );
        //第二个参数是默认选项 此处设置为0
        singleChoiceDialog.setSingleChoiceItems( items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                yourChoice = which;
            }
        } );
        singleChoiceDialog.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sex.setText( items[yourChoice] );
            }
        } );
        singleChoiceDialog.show();
    }

    //开启点击监听
    private void OpenOnlisten() {
        iv_person.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToastForBlue.makeText( PersonalCenterActivity.this, "暂未支持修改头像", Toast.LENGTH_LONG ).show();
            }
        } );
        name.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomizeDialog( "请输入您的昵称", name );

            }
        } );

        ll_school.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cityChooseDialog=new CityChooseDialog1();
                cityChooseDialog.startLoadingDialog(PersonalCenterActivity.this);

            }

        } );
        ll_sex.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSexDialog();
                // MyToast.makeText(PersonalCenterActivity.this,"点击了",Toast.LENGTH_SHORT).show();
            }
        } );
        ll_address.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomizeDialog( "输入您的住址:", address );
            }
        } );
        ll_sign.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomizeDialog( "输入您的个性签名:", sign );
            }
        } );

    }

    //获取网络数据
    private void GetDataFromNet() {
        userInfo = BmobUser.getCurrentUser( MyUser.class );
        //开启progressDialog
        loadingDialog = new LoadingDialog();
        loadingDialog.startLoadingDialog( PersonalCenterActivity.this, "加载中..." );

        new Thread( new Runnable() {
            @Override
            public void run() {
                if (userInfo != null) {
                    BmobQuery<MyUser> query = new BmobQuery<>();
                    query.addWhereEqualTo( "username", id );
                    //先缓存再网络获取数据
                    query.findObjects( new FindListener<MyUser>() {
                        @Override
                        public void done(List<MyUser> object, BmobException e) {
                            if (e == null) {
                                MyUser myUser = new MyUser();
                                myUser = object.get( 0 );
                                if (myUser.getMyname() == null) {
                                    name.setText( "未设置昵称" );
                                } else {
                                    name.setText( myUser.getMyname() );
                                }
                                if (myUser.getSchool() == null) {
                                    school.setText( "未设置" );
                                } else {
                                    school.setText( myUser.getSchool() );
                                }
                                if (myUser.getSex() == null) {
                                    sex.setText( "未设置" );
                                } else {
                                    sex.setText( myUser.getSex() );
                                }
                                if (myUser.getMyaddress() == null) {
                                    address.setText( "未设置" );
                                } else {
                                    address.setText( myUser.getMyaddress() );
                                }
                                if (myUser.getDes() == null) {
                                    sign.setText( "未设置" );
                                } else {
                                    sign.setText( myUser.getDes() );
                                }
                                if (myUser.getMytel() == null) {
                                    tel.setText( myUser.getUsername() );
                                } else {
                                    tel.setText( myUser.getMytel() );
                                }
                                handler.sendEmptyMessage( 0 );
                            } else {
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage( message );
                            }
                        }
                    } );
                } else {
                    handler.sendEmptyMessage( 0 );
                }
            }
        } ).start();
    }

    class Mycityy extends BaseAdapter  {



        @Override
        public int getCount() {
            return schoollist.length;
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

            Log.e( "来这了", "" );

            View view = View.inflate( PersonalCenterActivity.this, R.layout.item_city_list, null );
            tv_show = view.findViewById( R.id.tv_item );
            //o==3县区级
            if (position == 0) {
                tv_show.setText( schoollist[position] );
                tv_show.setTextColor( Color.parseColor( "#ff5f1a" ) );
            } else {
                tv_show.setText( schoollist[position] );
            }
            return view;
        }






    }

    private void showCustomizeforTelDialog(String des, final TextView textView) {
        TextView text = new TextView( PersonalCenterActivity.this );
        text.setText( des );
        text.setTextSize( 16 );
        text.setPadding( 0, 40, 0, 15 );
        text.setGravity( Gravity.CENTER );
        android.support.v7.app.AlertDialog.Builder customizeDialog =
                new android.support.v7.app.AlertDialog.Builder( PersonalCenterActivity.this );
        final View dialogView = LayoutInflater.from( PersonalCenterActivity.this )
                .inflate( R.layout.dialog_customize_fortel, null );
        edit_text1 =
                (EditText) dialogView.findViewById( R.id.edit_text_1 );
        edit_text1.setText( textView.getText() );
        edit_text1.setSelection( textView.length() );//将光标移至文字末尾
        customizeDialog.setCustomTitle( text );
        customizeDialog.setView( dialogView );
        customizeDialog.setPositiveButton( "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        if (edit_text1.getText().length() != 0 | edit_text1.getText().equals( textView.getText() )) {
                            textView.setText( edit_text1.getText() );
                        } else {
                            MyToast.makeText( PersonalCenterActivity.this, "信息未更改", Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );
        customizeDialog.show();
    }

    //学校选择提示框
    public class CityChooseDialog1 {
        public Dialog loadingDialog;

        public Dialog startLoadingDialog(Context context) {

            LayoutInflater inflater = LayoutInflater.from( context );
            View v = inflater.inflate( R.layout.address_citychoose, null );// 得到加载view
            LinearLayout layout = v.findViewById( R.id.jjj );// 加载布局

            show_city = v.findViewById( R.id.lv_show_city );

            loadingDialog = new Dialog( context );// 创建自定义样式dialog
            loadingDialog.setCancelable( true ); // 是否可以按“返回键”消失
            loadingDialog.setCanceledOnTouchOutside( false ); // 点击加载框以外的区域

            loadingDialog.setContentView( layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT ) );// 设置布局

            /*show_city.setAdapter( mycity );*/

            show_city.setAdapter( Myadapter );
            show_city.setTextFilterEnabled( true );

            //00
            main_searchview = v.findViewById( R.id.main_searchview );

            main_searchview.setOnQueryTextListener( new SearchView.OnQueryTextListener() // 设置搜索文本监听
            {
                @Override
                public boolean onQueryTextSubmit(String query) {//搜索时触发事件
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {//搜索时根据文本框动态改变搜索内容

                    if (!TextUtils.isEmpty( newText )) {

                        // main_listview.setFilterText( newText ); 有黑框
                        //无黑框
                        Myadapter.getFilter().filter( newText );



                    } else {
                        show_city.clearTextFilter();
                        show_city.clearChoices();
                        Myadapter.getFilter().filter( "" );

                    }
                    return true;
                }
            } );

            show_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String s=(String)parent.getItemAtPosition( position );

                    if("请选择学校（按字母顺序排列）".equals(s)){

                        MyToastForBlue.makeText(PersonalCenterActivity.this,"请选择您的学校",Toast.LENGTH_SHORT).show();

                    }else {

                        school.setText(s);
                        cityChooseDialog.closeLoadingDialog(cityChooseDialog.loadingDialog);

                    }
                }
            });

            /**
             *将显示Dialog的方法封装在这里面
             */
            Window window = loadingDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setGravity( Gravity.CENTER );
            window.setAttributes( lp );
            loadingDialog.show();

            return loadingDialog;
        }

        public void closeLoadingDialog(Dialog mDialogUtils) {
            if (mDialogUtils != null && mDialogUtils.isShowing()) {
                mDialogUtils.dismiss();
            }
        }


    }

}
