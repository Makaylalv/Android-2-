package com.example.zjq.mobileplayer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zjq.mobileplayer.IMusicPlayerService;
import com.example.zjq.mobileplayer.R;
import com.example.zjq.mobileplayer.bean.MediaItem;
import com.example.zjq.mobileplayer.service.MusicPlayerService;
import com.example.zjq.mobileplayer.utils.LyricUtils;
import com.example.zjq.mobileplayer.utils.Utils;
import com.example.zjq.mobileplayer.view.ShowLyricView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

public class AudioPlayerActivity extends Activity implements View.OnClickListener {


    private static final int TIAODONG =3 ;
    private int position;

    //服务的代理类，通过它可以调用服务的方法
    private IMusicPlayerService service;

    /**
     * 进度更新
     */
    private static final int PROGRESS = 1;
    /**
     * 显示歌词
     */
    private static final int SHOW_LYRIC = 2;

    /**
     * true:从状态栏进入的，不需要重新播放
     * false:从播放列表进入的
     */
    private boolean notification;
    private ImageView ivIcon;
    private TextView tvArtist;
    private TextView tvName;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnLyrc;
    private ShowLyricView showLyricView;

    private Utils utils;
   // private MyReceiver receiver;

   /* //跳动
    private BaseVisualizerView baseVisualizerView;
    private Visualizer mVisualizer;
*/


    //接口
    private ServiceConnection con = new ServiceConnection() {


        /**
         * 当连接成功的时候回调这个方法
         * @param name
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {

            service = IMusicPlayerService.Stub.asInterface( iBinder );


            if (service != null) {
                            try {

                                if (!notification) {//从列表

                                    service.openAudio( position );

                                } else {
                                    //从状态栏

                                    //点击状态栏进入页面后重新发送通知
                                    try {
                                        if (service!=null)
                                            service.Notification();
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }

                        //开始歌词同步
                        showLyric();
                       //显示视图
                        showViewData();

                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 当断开连接的时候回调这个方法
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

            //退出activity时不会回调
            try {
                if (service != null) {
                    service.stop();
                    service = null;

                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 直接把页面进入pause状态（HOME键）后,再点击状态栏进入页面后，状态栏通知消失，因为pause后再打开不执行create，而是start
     * 所以在start方法中
     * 再次发送通知
     */
    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (service!=null)
            service.Notification();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );

            switch (msg.what) {

                case SHOW_LYRIC: {
                    try {
                        //1.得到当前的进度

                        int currentPosition = service.getCurrentPosition();


                        //2.把进度传入ShowLyricView控件，并且计算该高亮那一句
                        showLyricView.setshowNextLyric(currentPosition);


                        //3.实时的发消息
                        handler.removeMessages( SHOW_LYRIC );
                        handler.sendEmptyMessage( SHOW_LYRIC );

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                }

                case PROGRESS:


                    try {
                        //1.得到当前进度
                        int currentPosition = service.getCurrentPosition();

                        //2.设置seekBar,setProgress(进度)

                        seekbarAudio.setProgress( currentPosition );
                        //3.时间进度更新
                        tvTime.setText( utils.stringForTime( currentPosition ) + "/" + utils.stringForTime( service.getDuration() ) );
                        //4.每秒运行一次
                        handler.removeMessages( PROGRESS );
                        handler.sendEmptyMessageDelayed( PROGRESS, 1000 );

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }


                    break;

                /*case TIAODONG:
                    setupVisualizerFxAndUi();
                    handler.removeMessages( TIAODONG );
                    break;*/
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );
            //透明导航栏
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION );
        }



        initData();
        findViews();
        getData();
        bindAndStartService();


    }

    private void initData() {

        utils = new Utils();

       // receiver = new MyReceiver();

        //1.EventBus注册
        EventBus.getDefault().register( this );


        //用EventBus代替
       /* //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction( MusicPlayerService.OPENAUDIO );
        registerReceiver( receiver, intentFilter );*/
    }


    /*class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            showData( null );
        }
    }*/


    /**
     * //EventBus
     * sticky = false  是否粘性
     */
    //3.订阅方法
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false, priority = 0)
    public void showData(MediaItem mediaItem) {

        //开始歌词同步
        showLyric();
        showViewData();
        checkPlaymode();
        //setupVisualizerFxAndUi();

       /* handler.sendEmptyMessage( TIAODONG );*/

       // swichTop();


    }




  /*  //顶部跳动模式选择
    private void swichTop() {
        baseVisualizerView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseVisualizerView.setVisibility( View.GONE );
                ivIcon.setVisibility( View.VISIBLE );
            }
        } );

        ivIcon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseVisualizerView.setVisibility( View.VISIBLE );
                ivIcon.setVisibility( View.GONE );
            }
        } );
    }*/


   /* *//**
     * 生成一个VisualizerView对象，使音频频谱的波段能够反映到 VisualizerView上
     *//*
    private void setupVisualizerFxAndUi()
    {

        try {
            int audioSessionid = service.getAudioSessionId();
            System.out.println("audioSessionid=="+audioSessionid);
            mVisualizer = new Visualizer(audioSessionid);
            // 参数内必须是2的位数
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            // 设置允许波形表示，并且捕获它
            baseVisualizerView.setVisualizer(mVisualizer);
            mVisualizer.setEnabled(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
*/



    private void showLyric() {
        //解析歌词
        LyricUtils lyricUtils=new LyricUtils();

        try {
            //传歌词文件
            String path=service.getAudioPath();
            path=path.substring( 0,path.lastIndexOf( "." ) );
            File file=new File( path+".lrc" );
            if (!file.exists()){
                file=new File( path+".txt" );
            }
            lyricUtils.readLyricFile( file );//解析歌词

            showLyricView.setLyrics( lyricUtils.getLyrics() );

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (lyricUtils.isExistsLyric()){
            handler.sendEmptyMessage( SHOW_LYRIC );

        }



    }


    private void showViewData() {
        try {
            tvArtist.setText( service.getArtist() );
            tvName.setText( service.getName() );

            //设置进度条最大值
            seekbarAudio.setMax( service.getDuration() );

            //发消息
            handler.sendEmptyMessage( PROGRESS );


        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void bindAndStartService() {
        Intent intent = new Intent( this, MusicPlayerService.class );

        intent.setAction( "com.zjq.mobileplayer_OPENAUDIO" );
        bindService( intent, con, Context.BIND_AUTO_CREATE );

        //这个方法使 不至于实例化多个服务
        startService( intent );
    }

    //得到数据
    private void getData() {
        notification = getIntent().getBooleanExtra( "notification", false );

        if (!notification) {
            position = getIntent().getIntExtra( "position", 0 );

        }
    }




    private void findViews() {
        setContentView( R.layout.activity_audioplayer );

        //baseVisualizerView=findViewById( R.id.baseVisualizerView );

        ivIcon = findViewById( R.id.iv_icon );
        ivIcon.setBackgroundResource( R.drawable.animation_list );
        AnimationDrawable drawable = (AnimationDrawable) ivIcon.getBackground();
        drawable.start();

        tvArtist = (TextView) findViewById( R.id.tv_artist );
        tvName = (TextView) findViewById( R.id.tv_name );
        tvTime = (TextView) findViewById( R.id.tv_time );
        seekbarAudio = (SeekBar) findViewById( R.id.seekbar_audio );
        btnAudioPlaymode = (Button) findViewById( R.id.btn_audio_playmode );
        btnAudioPre = (Button) findViewById( R.id.btn_audio_pre );
        btnAudioStartPause = (Button) findViewById( R.id.btn_audio_start_pause );
        btnAudioNext = (Button) findViewById( R.id.btn_audio_next );
        btnLyrc = (Button) findViewById( R.id.btn_lyrc );
        showLyricView = (ShowLyricView) findViewById(R.id.showLyricView);




        // baseVisualizerView = (BaseVisualizerView) findViewById(R.id.baseVisualizerView);

        btnAudioPlaymode.setOnClickListener( this );
        btnAudioPre.setOnClickListener( this );
        btnAudioStartPause.setOnClickListener( this );
        btnAudioNext.setOnClickListener( this );
        btnLyrc.setOnClickListener( this );

        //设置视频的拖动
        seekbarAudio.setOnSeekBarChangeListener( new MyOnSeekBarChangeListener() );
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (fromUser) {
                //拖动进度
                try {
                    service.seekTo( progress );
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnAudioPlaymode) {
            // Handle clicks for btnAudioPlaymode
            setPlaymode();
        } else if (v == btnAudioPre) {
            // Handle clicks for btnAudioPre
            if (service != null) {
                try {
                    service.pre();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (v == btnAudioStartPause) {

            if (service != null) {
                try {
                    if (service.isPlaying()) {


                        //暂停
                        service.pause();
                        //按钮-播放
                        btnAudioStartPause.setBackgroundResource( R.drawable.btn_audio_start_selecter );
                        //播放中，可以暂停
                    } else {

                        service.start();
                        btnAudioStartPause.setBackgroundResource( R.drawable.btn_audio_pause_selecter );

                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (v == btnAudioNext) {
            // Handle clicks for btnAudioNext
            if (service != null) {
                try {
                    service.next();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (v == btnLyrc) {
            // Handle clicks for btnLyrc
        }
    }

    private void showPlaymode() {

        try {
            int playmode = service.getPlayMode();

            if (playmode == MusicPlayerService.REPEAT_NORMAL) {

                btnAudioPlaymode.setBackgroundResource( R.drawable.btn_audio_playmode_normal_selector );
                Toast.makeText( AudioPlayerActivity.this, "顺序播放", Toast.LENGTH_SHORT ).show();

            } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {

                btnAudioPlaymode.setBackgroundResource( R.drawable.btn_audio_playmode_single_selector );
                Toast.makeText( AudioPlayerActivity.this, "单曲循环", Toast.LENGTH_SHORT ).show();

            } else if (playmode == MusicPlayerService.REPEAT_ALL) {

                btnAudioPlaymode.setBackgroundResource( R.drawable.btn_audio_playmode_all_selector );
                Toast.makeText( AudioPlayerActivity.this, "全部循环", Toast.LENGTH_SHORT ).show();

            } else {
                btnAudioPlaymode.setBackgroundResource( R.drawable.btn_audio_playmode_normal_selector );
                Toast.makeText( AudioPlayerActivity.this, "顺序播放", Toast.LENGTH_SHORT ).show();
            }


        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    private void setPlaymode() {

        try {

            int playmode = service.getPlayMode();

            if (playmode == MusicPlayerService.REPEAT_NORMAL) {
                playmode = MusicPlayerService.REPEAT_SINGLE;
            } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {
                playmode = MusicPlayerService.REPEAT_ALL;
            } else if (playmode == MusicPlayerService.REPEAT_ALL) {
                playmode = MusicPlayerService.REPEAT_NORMAL;
            } else {
                playmode = MusicPlayerService.REPEAT_NORMAL;

            }

            //保存
            service.setPlayMode( playmode );
            //设置图片,显示模式
            showPlaymode();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * 校验状态
     */
    private void checkPlaymode() {
        try {
            int playmode = service.getPlayMode();

            if (playmode == MusicPlayerService.REPEAT_NORMAL) {
                btnAudioPlaymode.setBackgroundResource( R.drawable.btn_audio_playmode_normal_selector );
            } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {
                btnAudioPlaymode.setBackgroundResource( R.drawable.btn_audio_playmode_single_selector );
            } else if (playmode == MusicPlayerService.REPEAT_ALL) {
                btnAudioPlaymode.setBackgroundResource( R.drawable.btn_audio_playmode_all_selector );
            } else {
                btnAudioPlaymode.setBackgroundResource( R.drawable.btn_audio_playmode_normal_selector );
            }

            //校验播放和暂停的按钮
            if (service.isPlaying()) {
                btnAudioStartPause.setBackgroundResource( R.drawable.btn_audio_start_selecter );
            } else {
                btnAudioStartPause.setBackgroundResource( R.drawable.btn_audio_pause_selecter );
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages( null );

        //2.EventBus取消注册
        EventBus.getDefault().unregister( this );


        //用EventBus代替
       /* //取消注册广播
        if (receiver != null) {
            unregisterReceiver( receiver );
            receiver = null;
        }*/

        //解绑服务
        if (con != null) {
            unbindService( con );
            con = null;
        }

        super.onDestroy();

    }


    @Override
    protected void onPause() {
        super.onPause();



/*
        if (mVisualizer!=null){
            mVisualizer.release();
        }*/
    }


}
