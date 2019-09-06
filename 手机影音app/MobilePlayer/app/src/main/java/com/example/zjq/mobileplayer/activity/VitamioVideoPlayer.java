package com.example.zjq.mobileplayer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zjq.mobileplayer.R;
import com.example.zjq.mobileplayer.bean.MediaItem;
import com.example.zjq.mobileplayer.utils.Utils;
import com.example.zjq.mobileplayer.view.VitamioVideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

/*系统播放器
 * */
public class VitamioVideoPlayer extends Activity implements View.OnClickListener {

    private boolean isUseSystem = true;
    //上一次的播放进度
    private int precurrentPostion;

    //视频进度
    private static final int PROGRESS = 1;
    //几秒后隐藏控制面板

    private static final int HIDE_MediaController = 2;

    //全屏
    private static final int FULL_SCREEN = 1;
    //默认大小
    private static final int DEFAULT_SCREEN = 2;

    //显示网速
    private static final int SHOW_SPEED = 3;


    private Uri uri;

    private VitamioVideoView videoview;
    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwichPlayer;
    private LinearLayout llBottom;
    private RelativeLayout media_controller;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSiwchScreen;
    private TextView tv_buffer_netspeed;
    private LinearLayout ll_buffer;
    private TextView tv_laoding_netspeed;
    private LinearLayout ll_loading;


    private Utils utils;

    //监听电量
    private MyReceiver receiver;

    //传入进来的视频列表
    private ArrayList<MediaItem> mediaItems;

    //要播放的列表中的具体位置
    private int position;

    /*1.定义手势识别器*/
    private GestureDetector detector;

    //是否显示控制面板
    private boolean isshowMediaController = false;
    private boolean isFullScreen = false;

    //屏幕的宽
    private int screenWidth = 0;

    //屏幕的高
    private int screenHeight = 0;

    //真实视频的宽
    private int videoWidth;
    //真实视频的高
    private int videoHight;

    //调节声音
    private AudioManager am;

    //当前音量
    private int currentVoice;

    //最大音量 0-15
    private int maxVoice;
    private boolean isMute = false;


    private float startY;
    //屏幕的高
    private float touchRang;

    //当前按下的音量
    private int mVol;

    //是否是网络url
    private boolean isNetUri;

    private void findViews() {


        Vitamio.isInitialized( this );
        setContentView( R.layout.activity_vitamio_video_player );

        llTop = (LinearLayout) findViewById( R.id.ll_top );
        tvName = (TextView) findViewById( R.id.tv_name );
        ivBattery = (ImageView) findViewById( R.id.iv_battery );
        tvSystemTime = (TextView) findViewById( R.id.tv_system_time );
        btnVoice = (Button) findViewById( R.id.btn_voice );
        seekbarVoice = (SeekBar) findViewById( R.id.seekbar_voice );
        btnSwichPlayer = (Button) findViewById( R.id.btn_swich_player );
        llBottom = (LinearLayout) findViewById( R.id.ll_bottom );
        tvCurrentTime = (TextView) findViewById( R.id.tv_current_time );
        seekbarVideo = (SeekBar) findViewById( R.id.seekbar_video );
        tvDuration = (TextView) findViewById( R.id.tv_duration );
        btnExit = (Button) findViewById( R.id.btn_exit );
        btnVideoPre = (Button) findViewById( R.id.btn_video_pre );
        btnVideoStartPause = (Button) findViewById( R.id.btn_video_start_pause );
        btnVideoNext = (Button) findViewById( R.id.btn_video_next );
        btnVideoSiwchScreen = (Button) findViewById( R.id.btn_video_siwch_screen );
        videoview = (VitamioVideoView) findViewById( R.id.videoview2 );
        media_controller = (RelativeLayout) findViewById( R.id.media_controller2 );

        tv_buffer_netspeed = findViewById( R.id.tv_buffer_netspeed );
        ll_buffer = findViewById( R.id.ll_buffer2 );

        tv_laoding_netspeed = (TextView) findViewById( R.id.tv_loading_netspeed );
        ll_loading = (LinearLayout) findViewById( R.id.ll_loading2 );

        btnVoice.setOnClickListener( this );
        btnSwichPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnVideoPre.setOnClickListener( this );
        btnVideoStartPause.setOnClickListener( this );
        btnVideoNext.setOnClickListener( this );
        btnVideoSiwchScreen.setOnClickListener( this );

        //最大音量和SeekBar关联
        seekbarVoice.setMax( maxVoice );
        //设置当前进度-当前音量
        seekbarVoice.setProgress( currentVoice );

        //开始更新网络速度
        handler.sendEmptyMessage( SHOW_SPEED );

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );

            switch (msg.what) {

                case SHOW_SPEED://显示网速

                        //1.得到网速
                        String netSpeed = utils.getNetSpeed( VitamioVideoPlayer.this );

                        //显示网络速度
                        tv_laoding_netspeed.setText( "玩命加载中" + netSpeed );
                        tv_buffer_netspeed.setText( "缓冲中..." + netSpeed );


                        //每俩秒更新一次
                        //移除消息
                        handler.removeMessages( SHOW_SPEED );
                        handler.sendEmptyMessageDelayed( SHOW_SPEED, 2000 );


                    break;
                case PROGRESS:

                    //1.得到当前的视频的播放进度

                    int currentPostion = (int) videoview.getCurrentPosition();
                    //2.setBar.setProgress()
                    seekbarVideo.setProgress( currentPostion );

                    //更新文本播放进度
                    tvCurrentTime.setText( utils.stringForTime( currentPostion ) );

                    //设置系统时间
                    tvSystemTime.setText( getSystemTime() );

                    //缓存进度的更新
                    if (isNetUri) {

                        //只有网络的视频才有缓存

                        //源码算法
                        int buffer = videoview.getBufferPercentage();
                        int totalBuffer = buffer * seekbarVideo.getMax();
                        int setSecondaryProgress = totalBuffer / 100;
                        seekbarVideo.setSecondaryProgress( setSecondaryProgress );

                    } else {
                        //本地的没有
                        seekbarVideo.setSecondaryProgress( 0 );
                    }

                    //监听卡
                    if (!isUseSystem && videoview.isPlaying()) {

                        if (videoview.isPlaying()) {
                            int buffer = currentPostion - precurrentPostion;

                            if (buffer < 800) {
                                //视频卡了
                                ll_buffer.setVisibility( View.VISIBLE );

                            } else {
                                //视频不卡
                                ll_buffer.setVisibility( View.GONE );
                            }
                        }else {
                            ll_buffer.setVisibility( View.GONE );

                        }

                    }


                    precurrentPostion = currentPostion;

                    //3.每秒更新一次
                    //移除消息
                    handler.removeMessages( PROGRESS );
                    handler.sendEmptyMessageDelayed( PROGRESS, 1000 );


                    break;

                case HIDE_MediaController:

                    hideMediaController();

                    break;

            }
        }
    };

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat( "HH:mm" );
        return format.format( new Date() );
    }

    private void showSwichPlayerDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder( this );
        builder.setTitle( "万能播放器提示" );
        builder.setMessage( "当您播放有花屏时，请尝试切换系统播放器" );

        builder.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startSystemPlayer();
            }
        } );

        builder.setNegativeButton( "取消" ,null);

        builder.show();
    }

    private void startSystemPlayer() {

        if (videoview!=null){
            videoview.stopPlayback();
        }

        Intent intent=new Intent( this,SystemVideoPlayer.class );

        if (mediaItems!=null&&mediaItems.size()>0){
            Bundle bundle=new Bundle(  );
            bundle.putSerializable( "videolist",mediaItems );
            intent.putExtras( bundle );
            intent.putExtra( "position",position );
        }else if (uri!=null){

            intent.setData( uri );
        }
        startActivity( intent );

        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == btnVoice) {
            isMute = !isMute;
            updataVoice( currentVoice, isMute );

        } else if (v == btnSwichPlayer) {
            showSwichPlayerDialog();

        } else if (v == btnExit) {
            finish();
        } else if (v == btnVideoPre) {
            playPreVideo();

        } else if (v == btnVideoStartPause) {
            startAndPause();

        } else if (v == btnVideoNext) {
            playNextVideo();
        } else if (v == btnVideoSiwchScreen) {
            setFullScreenAndDefault();
        }

        handler.removeMessages( HIDE_MediaController );
        handler.sendEmptyMessageDelayed( HIDE_MediaController, 4000 );

    }



    private void startAndPause() {
        if (videoview.isPlaying()) {
            //视频在播放，可以设置为暂停
            videoview.pause();
            btnVideoStartPause.setBackgroundResource( R.drawable.btn_video_start_selecter );

            //按钮状态可以设置为播放
        } else {

            videoview.start();
            btnVideoStartPause.setBackgroundResource( R.drawable.btn_video_pause_selecter );
            //视频播放
            //按钮状态设置为暂停
        }
    }

    //播放上一个视频
    private void playPreVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            //播放上一个
            position--;
            if (position >= 0) {

                ll_loading.setVisibility( View.VISIBLE );
                MediaItem mediaItem = mediaItems.get( position );
                tvName.setText( mediaItem.getName() );
                isNetUri = utils.isNetUri( mediaItem.getData() );
                videoview.setVideoPath( mediaItem.getData() );

                //设置按钮状态
                setButtonState();

            }
        } else if (uri != null) {
            //设置按钮状态，上一个和下一个按钮设置灰色，并且不可点击
            setButtonState();
        }
    }

    private void playNextVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            //播放下一个
            position++;
            if (position < mediaItems.size()) {


                ll_loading.setVisibility( View.VISIBLE );
                MediaItem mediaItem = mediaItems.get( position );
                tvName.setText( mediaItem.getName() );
                isNetUri = utils.isNetUri( mediaItem.getData() );
                videoview.setVideoPath( mediaItem.getData() );


                //设置按钮状态
                setButtonState();

            }
        } else if (uri != null) {
            //设置按钮状态，上一个和下一个按钮设置灰色，并且不可点击
            setButtonState();
        }
    }

    private void setButtonState() {
        if (mediaItems != null && mediaItems.size() > 0) {

            if (mediaItems.size() == 1) {
                //俩个按钮都设置灰色
                btnVideoPre.setBackgroundResource( R.drawable.btn_pre_gray );
                btnVideoPre.setEnabled( false );
                btnVideoNext.setBackgroundResource( R.drawable.btn_next_gray );
                btnVideoNext.setEnabled( false );
            } else if (mediaItems.size() == 2) {
                if (position == 0) {
                    btnVideoPre.setBackgroundResource( R.drawable.btn_video_pre_selecter );
                    btnVideoPre.setEnabled( true );
                    btnVideoPre.setBackgroundResource( R.drawable.btn_pre_gray );
                    btnVideoPre.setEnabled( false );
                } else if (position == mediaItems.size() - 1) {
                    btnVideoNext.setBackgroundResource( R.drawable.btn_next_gray );
                    btnVideoNext.setEnabled( false );

                    btnVideoPre.setBackgroundResource( R.drawable.btn_video_pre_selecter );
                    btnVideoPre.setEnabled( true );
                }
            } else {
                if (position == 0) {
                    btnVideoPre.setBackgroundResource( R.drawable.btn_pre_gray );
                    btnVideoPre.setEnabled( false );
                } else if (position == mediaItems.size() - 1) {
                    btnVideoNext.setBackgroundResource( R.drawable.btn_next_gray );
                    btnVideoNext.setEnabled( false );
                } else {
                    btnVideoPre.setBackgroundResource( R.drawable.btn_video_pre_selecter );
                    btnVideoPre.setEnabled( true );
                    btnVideoNext.setBackgroundResource( R.drawable.btn_video_next_selecter );
                    btnVideoNext.setEnabled( true );
                }
            }
        } else if (uri != null) {
            //俩个按钮都设置灰色
            btnVideoPre.setBackgroundResource( R.drawable.btn_pre_gray );
            btnVideoPre.setEnabled( false );
            btnVideoNext.setBackgroundResource( R.drawable.btn_next_gray );
            btnVideoNext.setEnabled( false );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        initData();


        findViews();

        setListener();


        getData();


        setData();


       /* //系统控制器 播放 暂停
        videoView.setMediaController( new MediaController( this ) );*/

        //自定义控制器

    }

    private void setData() {

        if (mediaItems != null && mediaItems.size() > 0) {

            MediaItem mediaItem = mediaItems.get( position );
            tvName.setText( mediaItem.getName() );//设置视频的名称

            isNetUri = utils.isNetUri( mediaItem.getData() );

            videoview.setVideoPath( mediaItem.getData() );
        } else if (uri != null) {
            tvName.setText( uri.toString() );
            isNetUri = utils.isNetUri( uri.toString() );

            videoview.setVideoURI( uri );
        } else {
            Toast.makeText( VitamioVideoPlayer.this, "你没有传递数据", Toast.LENGTH_SHORT ).show();
        }

        setButtonState();
    }

    private void getData() {

        //得到播放地址

        uri = getIntent().getData();

        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra( "videolist" );

        position = getIntent().getIntExtra( "position", 0 );


    }

    private void initData() {
        utils = new Utils();
        //注册电量广播
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction( Intent.ACTION_BATTERY_CHANGED );
        registerReceiver( receiver, intentFilter );

        //2.实例化手势识别器，并且重写单击双击长按
        detector = new GestureDetector( this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress( e );

                //暂停，播放
                startAndPause();

               // Toast.makeText( VitamioVideoPlayer.this, "我被长按了", Toast.LENGTH_SHORT ).show();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {

                setFullScreenAndDefault();
              //  Toast.makeText( VitamioVideoPlayer.this, "我被双击了", Toast.LENGTH_SHORT ).show();
                return super.onDoubleTap( e );
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                if (isshowMediaController) {
                    hideMediaController();

                    //把隐藏消息移除
                    handler.removeMessages( HIDE_MediaController );
                } else {
                    //显示
                    showMediaController();
                    //发消息，几秒后隐藏‘

                    //发送延迟消息
                    handler.sendEmptyMessageDelayed( HIDE_MediaController, 4000 );
                }

                //Toast.makeText( VitamioVideoPlayer.this, "我被单击了", Toast.LENGTH_SHORT ).show();

                return super.onSingleTapConfirmed( e );
            }
        } );


        //得到屏幕的宽和屏幕的高
        //1.过时方法
       /* screenWidth=getWindowManager().getDefaultDisplay().getWidth();
        screenHeight=getWindowManager().getDefaultDisplay().getHeight();*/

        //2.新方法
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( displayMetrics );
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;


        //得到音量
        am = (AudioManager) getSystemService( AUDIO_SERVICE );
        currentVoice = am.getStreamVolume( AudioManager.STREAM_MUSIC );
        maxVoice = am.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
    }

    private void setFullScreenAndDefault() {
        if (isFullScreen) {
            //默认
            setVideoType( DEFAULT_SCREEN );
        } else {
            setVideoType( FULL_SCREEN );
            //全屏
        }
    }

    private void setVideoType(int defaultScreen) {

        switch (defaultScreen) {
            case FULL_SCREEN://全屏
                //1.设置视频画面的大小
                videoview.setViedoSize( screenWidth, screenHeight );
                //2.设置按钮的状态
                btnVideoSiwchScreen.setBackgroundResource( R.drawable.btn_voice_siwch_screen_default_selecter );
                isFullScreen = true;

                break;
            case DEFAULT_SCREEN:

                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHight;

                int width = screenWidth;
                int height = screenHeight;

                if (mVideoWidth * height < width * mVideoHeight) {
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    height = width * mVideoHeight / mVideoWidth;
                }

                videoview.setViedoSize( width, height );
                btnVideoSiwchScreen.setBackgroundResource( R.drawable.btn_voice_siwch_screen_full_selecter );
                isFullScreen = false;

                break;
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra( "level", 0 );//0-100
            setBattery( level );
        }
    }

    private void setBattery(int level) {
        if (level <= 0) {
            ivBattery.setImageResource( R.drawable.ic_battery_0 );
        } else if (level <= 10) {
            ivBattery.setImageResource( R.drawable.ic_battery_10 );
        } else if (level <= 20) {
            ivBattery.setImageResource( R.drawable.ic_battery_20 );
        } else if (level <= 40) {
            ivBattery.setImageResource( R.drawable.ic_battery_40 );

        } else if (level <= 60) {
            ivBattery.setImageResource( R.drawable.ic_battery_60 );

        } else if (level <= 80) {
            ivBattery.setImageResource( R.drawable.ic_battery_80 );

        } else if (level <= 100) {
            ivBattery.setImageResource( R.drawable.ic_battery_100 );

        } else {
            ivBattery.setImageResource( R.drawable.ic_battery_100 );

        }
    }

    private void setListener() {

        //准备好的监听
        videoview.setOnPreparedListener( new MyOnPreparedListener() );

        //播放出错的监听
        videoview.setOnErrorListener( new MyOnErrorListener() );

        //播放完成的监听
        videoview.setOnCompletionListener( new MyOnCompletionListener() );

        //设置SeekBar状态变化的监听
        seekbarVideo.setOnSeekBarChangeListener( new VideoOnSeekBarChangeListener() );

        seekbarVoice.setOnSeekBarChangeListener( new VoiceOnSeekBarChangeListener() );


        if (isUseSystem) {
            //监听视频播放卡顿（网络造成）
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                videoview.setOnInfoListener( new MyOnInfoListener() );
            }
        } else {

        }


    }

    class MyOnInfoListener implements MediaPlayer.OnInfoListener {

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_BUFFERING_START://视频卡了，开始缓存
                  //  Toast.makeText( VitamioVideoPlayer.this, "卡了", Toast.LENGTH_SHORT ).show();

                    ll_buffer.setVisibility( View.VISIBLE );
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END://卡结束了
                   // Toast.makeText( VitamioVideoPlayer.this, "卡结束了", Toast.LENGTH_SHORT ).show();

                    ll_buffer.setVisibility( View.GONE );
                    break;
            }


            return true;
        }
    }

    class VoiceOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (fromUser) {

                if (progress > 0) {
                    isMute = false;
                } else {
                    isMute = true;
                }


                updataVoice( progress, isMute );
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

            handler.removeMessages( HIDE_MediaController );
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed( HIDE_MediaController, 4000 );

        }
    }

    //设置音量的大小
    private void updataVoice(int progress, boolean isMute) {
        if (isMute) {
            //静音
            am.setStreamVolume( AudioManager.STREAM_MUSIC, 0, 0 );
            seekbarVoice.setProgress( 0 );
        } else {
            //1 同时调出系统的音量调节  0 不调
            am.setStreamVolume( AudioManager.STREAM_MUSIC, progress, 0 );
            seekbarVoice.setProgress( progress );
            currentVoice = progress;
        }


    }

    class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {


            /*
            *
            * SeekBar seekBar,
            * int progress,
              boolean fromUser :重要 如果是用户引起的是true， 不是用户引起的时false
              因为自动进度更新时这个方法也会执行，只不过由这个参数识别
            *
            * */


        /*当手指滑动时候，会引起SeekBar进度变化时回调*/

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                videoview.seekTo( progress );
            }
        }

        /*当手指出触碰时回调*/
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

            handler.removeMessages( HIDE_MediaController );
        }

        /*当手指离开的时候回调这个方法*/
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            handler.sendEmptyMessageDelayed( HIDE_MediaController, 4000 );
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        //当底层解码准备好的时候
        @Override
        public void onPrepared(MediaPlayer mp) {

            videoWidth = mp.getVideoWidth();
            videoHight = mp.getVideoHeight();
            videoview.start();

            //得到总时长
            int duration = (int) videoview.getDuration();
            seekbarVideo.setMax( duration );
            tvDuration.setText( utils.stringForTime( duration ) );

            //默认隐藏控制面板
            hideMediaController();

            //发消息
            handler.sendEmptyMessage( PROGRESS );


            // videoview.setViedoSize( 200,200 );

            //videoview.setViedoSize( mp.getVideoWidth(),mp.getVideoHeight() );

            //屏幕的默认播放
            setVideoType( DEFAULT_SCREEN );


            //把加载页面消失掉
            ll_loading.setVisibility( View.GONE );

           /* mp.setOnSeekCompleteListener( new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    Toast.makeText( SystemVideoPlayer.this,"拖动完成",Toast.LENGTH_SHORT ).show();

                }
            } );*/

        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        //出错
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText( VitamioVideoPlayer.this, "万能播放器也无能为力！", Toast.LENGTH_LONG ).show();

            showErrorDialog();



            return false;
        }


    }

    private void showErrorDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder( this );
        builder.setTitle( "提示" );
        builder.setMessage( "抱歉，无法播放该视频" );

        builder.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        } );

        builder.show();
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        //播放完成
        @Override
        public void onCompletion(MediaPlayer mp) {
            playNextVideo();
        }
    }

    @Override
    protected void onDestroy() {

        //取消广播
        if (receiver != null) {
            unregisterReceiver( receiver );
            receiver = null;
        }
        super.onDestroy();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //3.把事件传递给手势识别器，仅仅是解析，不拦截
        detector.onTouchEvent( event );

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://手指按下
                //1.按下记录值
                startY = event.getY();
                mVol = am.getStreamVolume( AudioManager.STREAM_MUSIC );
                touchRang = Math.min( screenHeight, screenWidth );
                handler.removeMessages( HIDE_MediaController );
                break;
            case MotionEvent.ACTION_MOVE://手指移动

              /*  //2.移动的记录相关值
                float endY = event.getY();
                float distanceY = startY - endY;
                float delta = (distanceY / touchRang) * maxVoice;
                int voice = (int) Math.min( maxVoice, Math.max( 0, mVol + delta ) );

                if (delta != 0) {

                    updataVoice( voice, false );

                }*/

                //2.移动的记录相关值
                float endY = event.getY();
                float endX = event.getX();
                float distanceY = startY - endY;

                if (endX < screenWidth / 2) {
                    //左边屏幕，调节亮度

                    final double FLING_MIN_DISTANCE = 0.5;
                    final double FLING_MIN_VELOCITY = 0.5;
                    if (distanceY > FLING_MIN_DISTANCE && Math.abs( distanceY ) > FLING_MIN_VELOCITY) {
                        setBrightness( 20 );
                    }
                    if (distanceY < FLING_MIN_DISTANCE && Math.abs( distanceY ) > FLING_MIN_VELOCITY) {
                        setBrightness( -20 );
                    }
                } else {

                    //右边屏幕调节声音
                    float delta = (distanceY / touchRang) * maxVoice;
                    int voice = (int) Math.min( maxVoice, Math.max( 0, mVol + delta ) );

                    if (delta != 0) {

                        updataVoice( voice, false );

                    }
                }
                break;
            case MotionEvent.ACTION_UP://手指离开
                handler.sendEmptyMessageDelayed( HIDE_MediaController, 4000 );
                break;
        }
        return super.onTouchEvent( event );
    }
    public void setBrightness(float brightness) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
            long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...
        } else if (lp.screenBrightness < 0.2) {
            lp.screenBrightness = (float) 0.2;
            long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...
        }
        getWindow().setAttributes(lp);
    }

    //显示控制面板
    private void showMediaController() {

        media_controller.setVisibility( View.VISIBLE );
        isshowMediaController = true;
    }

    //隐藏控制面板
    private void hideMediaController() {

        media_controller.setVisibility( View.GONE );
        isshowMediaController = false;
    }


    //监听物理键，实现声音的调节大小
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            currentVoice--;
            updataVoice( currentVoice, false );
            handler.removeMessages( HIDE_MediaController );
            handler.sendEmptyMessageDelayed( HIDE_MediaController, 4000 );

            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            currentVoice++;
            updataVoice( currentVoice, false );
            handler.removeMessages( HIDE_MediaController );
            handler.sendEmptyMessageDelayed( HIDE_MediaController, 4000 );

            return true;
        }
        return super.onKeyDown( keyCode, event );
    }
}
