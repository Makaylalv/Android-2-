package com.example.zjq.mobileplayer.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.zjq.mobileplayer.IMusicPlayerService;
import com.example.zjq.mobileplayer.R;
import com.example.zjq.mobileplayer.activity.AudioPlayerActivity;
import com.example.zjq.mobileplayer.bean.MediaItem;
import com.example.zjq.mobileplayer.utils.CacheUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerService extends Service {

    public static final String OPENAUDIO = "com.zjq.mobileplayer_OPENAUDIO";
    private ArrayList<MediaItem> mediaItems;
    private int position;


    //状态栏通知
    private NotificationManager manager;

    /**
     * 当前播放的音频文件对象
     */
    private MediaItem mediaItem;

    private MediaPlayer mediaPlayer;

    //顺序播放
    public static final int REPEAT_NORMAL = 1;

    //单曲循环
    public static final int REPEAT_SINGLE = 2;

    //全部循环
    public static final int REPEAT_ALL = 3;

    //默认播放模式
    private int playmode = REPEAT_NORMAL;

    @Override
    public void onCreate() {
        super.onCreate();

        playmode = CacheUtils.getPlaymode( this, "playmode" );

        //加载音乐列表
        getDataFromLocal();
    }

    private void getDataFromLocal() {
        mediaItems = new ArrayList<>();


        new Thread() {
            @Override
            public void run() {
                super.run();

                //内容解析者
                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {

                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST,
                };

                Cursor cursor = resolver.query( uri, objs, null, null, null );
                if (cursor != null) {
                    while (cursor.moveToNext()) {

                        MediaItem mediaItem = new MediaItem();

                        mediaItems.add( mediaItem );//写在上面也可以

                        String name = cursor.getString( 0 );
                        mediaItem.setName( name );

                        Long duration = cursor.getLong( 1 );
                        mediaItem.setDuration( duration );

                        Long size = cursor.getLong( 2 );
                        mediaItem.setSize( size );

                        String data = cursor.getString( 3 );
                        mediaItem.setData( data );

                        String artist = cursor.getString( 4 );
                        mediaItem.setArtist( artist );

                    }
                    cursor.close();

                }

            }
        }.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        return stub;
    }

    private IMusicPlayerService.Stub stub = new IMusicPlayerService.Stub() {

        MusicPlayerService serivce = MusicPlayerService.this;

        @Override
        public void openAudio(int position) throws RemoteException {

            serivce.openAudio( position );
        }

        @Override
        public void start() throws RemoteException {

            serivce.start();
        }

        @Override
        public void pause() throws RemoteException {

            serivce.pause();
        }

        @Override
        public void stop() throws RemoteException {

            serivce.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return serivce.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return serivce.getDuration();
        }

        @Override
        public String getArtist() throws RemoteException {
            return serivce.getArtist();
        }

        @Override
        public String getName() throws RemoteException {
            return serivce.getName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return serivce.getAudioPath();
        }

        @Override
        public void next() throws RemoteException {

            serivce.next();
        }

        @Override
        public void pre() throws RemoteException {

            serivce.pre();
        }

        @Override
        public void setPlayMode(int playmode) throws RemoteException {

            serivce.setPlayMode( playmode );
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return serivce.getPlayMode();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mediaPlayer.isPlaying();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            mediaPlayer.seekTo( position );
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return mediaPlayer.getAudioSessionId();
        }

        @Override
        public void Notification() throws RemoteException {

            serivce.Notification();
        }
    };


    //根据位置打开对应的音频文件
    private void openAudio(int position) {
        this.position = position;
        if (mediaItems != null && mediaItems.size() > 0) {

            mediaItem = mediaItems.get( position );

            if (mediaPlayer != null) {

                //释放,置为空闲
                mediaPlayer.reset();
            }

            try {

                mediaPlayer = new MediaPlayer();

                //设置监听，播放出错，完成，准备好
                mediaPlayer.setOnPreparedListener( new MyOnPreparedListener() );
                mediaPlayer.setOnCompletionListener( new MyOnCompletionListener() );
                mediaPlayer.setOnErrorListener( new MyOnErrorListener() );
                mediaPlayer.setDataSource( mediaItem.getData() );
                mediaPlayer.prepareAsync();

                if (playmode == MusicPlayerService.REPEAT_SINGLE) {
                    //单曲循环播放-不会触发播放完成的回调
                    mediaPlayer.setLooping( true );
                } else {
                    mediaPlayer.setLooping( false );
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText( MusicPlayerService.this, "还没有数据！", Toast.LENGTH_SHORT ).show();
        }

    }



    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {

            //EventBus代替
            /*//通知Activity来获取信息--广播
            notifyChange( OPENAUDIO );*/


            EventBus.getDefault().post( mediaItem );

            start();
        }
    }

    private void notifyChange(String action) {
        //根据动作发广播
        Intent intent = new Intent( action );
        sendBroadcast( intent );
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            //出错了 也播放下一个
            next();
            return true;


        }
    }


    //播放音乐

    private void start() {

        mediaPlayer.start();

        Notification();


    }


    @TargetApi(Build.VERSION_CODES.O)
    private void Notification() {

        String channelID = "1";
        String channelName = "channel_name";
        NotificationChannel channel = new NotificationChannel( channelID, channelName, NotificationManager.IMPORTANCE_HIGH );
        NotificationManager manager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        manager.createNotificationChannel( channel );
        Notification.Builder builder = new Notification.Builder( this );
        Intent intent = new Intent( this, AudioPlayerActivity.class );
        intent.putExtra( "notification", true );
        PendingIntent pendingIntent = PendingIntent.getActivity( this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        builder.setSmallIcon( R.drawable.notification_music_playing );
        builder.setContentText( "正在播放：" + getName() );
        builder.setContentTitle( "手机影音" );

        //使用过后，如何取消呢？Android为我们提供两种方式移除通知，一种是Notification自己维护，使用setAutoCancel()方法设置是否维护
        builder.setAutoCancel( true );
        builder.setContentIntent( pendingIntent );
        //创建通知时指定channelID
        builder.setChannelId( channelID );
        Notification notification = builder.build();
        manager.notify( 1, notification );
    }

    //暂停音乐
    private void pause() {

        mediaPlayer.pause();
    }

    //停止音乐
    private void stop() {

    }

    //得到当前的播放进度
    private int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    //得到当前音频的总时长
    private int getDuration() {
        return mediaPlayer.getDuration();
    }

    //得到艺术家名字
    private String getArtist() {
        if (mediaItem.getArtist() != null) {
            return mediaItem.getArtist();
        } else {
            return "";
        }

    }

    //得到歌曲名字
    private String getName() {
        return mediaItem.getName();
    }

    //得到歌曲播放的路径
    private String getAudioPath() {
        return mediaItem.getData();
    }

    //播放下一个
    private void next() {
        //1.根据当前的播放模式设置下一个位置

        setNextPosition();
        //2.根据当前的播放模式和下标位置去播放音频

        openNextAudio();
    }

    private void openNextAudio() {


        int playmode = getPlayMode();

        if (playmode == MusicPlayerService.REPEAT_NORMAL) {

            if (position < mediaItems.size()) {
                //正常的
                openAudio( position );
            } else {
                position = mediaItems.size() - 1;
            }

        } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {

            openAudio( position );

        } else if (playmode == MusicPlayerService.REPEAT_ALL) {


            openAudio( position );

        } else {

            if (position < mediaItems.size()) {
                //正常的
                openAudio( position );
            } else {
                position = mediaItems.size() - 1;
            }
        }


    }

    private void setNextPosition() {

        int playmode = getPlayMode();

        if (playmode == MusicPlayerService.REPEAT_NORMAL) {

            position++;

        } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {

            position++;
            if (position >= mediaItems.size()) {
                position = 0;
            }

        } else if (playmode == MusicPlayerService.REPEAT_ALL) {

            position++;
            if (position >= mediaItems.size()) {
                position = 0;
            }

        } else {
            position++;

        }


    }

    //播放上一个
    private void pre() {
        //1.根据当前的播放模式设置上一个位置

        setPrePosition();
        //2.根据当前的播放模式和下标位置去播放音频

        openPreAudio();

    }

    private void openPreAudio() {

        int playmode = getPlayMode();

        if (playmode == MusicPlayerService.REPEAT_NORMAL) {

            if (position >= 0) {
                //正常的
                openAudio( position );
            } else {
                position = 0;
            }

        } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {

            openAudio( position );

        } else if (playmode == MusicPlayerService.REPEAT_ALL) {


            openAudio( position );

        } else {

            if (position >= 0) {
                //正常的
                openAudio( position );
            } else {
                position = 0;
            }
        }


    }

    private void setPrePosition() {
        int playmode = getPlayMode();

        if (playmode == MusicPlayerService.REPEAT_NORMAL) {

            position--;

        } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {

            position--;
            if (position < 0) {
                position = mediaItems.size() - 1;
            }

        } else if (playmode == MusicPlayerService.REPEAT_ALL) {

            position--;
            if (position < 0) {
                position = mediaItems.size() - 1;
            }

        } else {
            position--;

        }

    }

    //设置播放模式
    private void setPlayMode(int playmode) {

        this.playmode = playmode;

        CacheUtils.putPlaymode( this, "playmode", playmode );

        if (playmode == MusicPlayerService.REPEAT_SINGLE) {
            //单曲循环播放-不会触发播放完成的回调
            mediaPlayer.setLooping( true );
        } else {
            mediaPlayer.setLooping( false );
        }


    }

    //得到播放模式
    private int getPlayMode() {

        return playmode;
    }


    private boolean isPlaying() {

        return mediaPlayer.isPlaying();
    }

}
