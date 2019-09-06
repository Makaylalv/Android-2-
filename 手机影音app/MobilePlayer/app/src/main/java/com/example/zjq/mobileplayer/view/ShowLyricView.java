package com.example.zjq.mobileplayer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import android.widget.TextView;

import com.example.zjq.mobileplayer.bean.Lyric;
import com.example.zjq.mobileplayer.utils.DensityUtil;

import java.util.ArrayList;

/**
 * 自定义歌词显示控件
 * <p>
 * 注意！
 */
@SuppressLint("AppCompatCustomView")
public class ShowLyricView extends TextView {





    //歌词列表
    private ArrayList<Lyric> lyrics;
    private Paint paint;

    //白色画笔
    private Paint whitepaint;

    private int width;
    private int hight;

    //歌词列表中索引
    private int index;

    //每行的高
    private float textHight;

    //当前播放进度
    private float currentPosition;

    //高亮显示的时间
    private float sleepTime;

    //什么时刻到高亮那句歌词
    private float timePoint;





    //设置歌词
    public void setLyrics(ArrayList<Lyric> lyrics) {
        this.lyrics = lyrics;
    }

    public ShowLyricView(Context context) {
        this( context, null );
    }

    public ShowLyricView(Context context, @Nullable AttributeSet attrs) {
        this( context, attrs, 0 );
    }

    public ShowLyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );

        initView(context);
    }

    private void initView(Context context) {


        textHight= DensityUtil.dip2px( context,20);


        //创建画笔
        paint = new Paint();
        paint.setColor( Color.WHITE );
        paint.setTextSize( DensityUtil.dip2px( context,16) );
        //抗锯齿
        paint.setAntiAlias( true );
        //设置居中对齐
        paint.setTextAlign( Paint.Align.CENTER );

        whitepaint = new Paint();
        whitepaint.setColor( Color.GRAY );
        whitepaint.setTextSize( DensityUtil.dip2px( context,16) );
        //抗锯齿
        whitepaint.setAntiAlias( true );
        //设置居中对齐
        whitepaint.setTextAlign( Paint.Align.CENTER );



    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged( w, h, oldw, oldh );

        width = w;
        hight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw( canvas );

        if (lyrics != null && lyrics.size() > 0) {

            //歌词缓缓推移
            float plush=0;
            if (sleepTime==0){
                plush=0;
            }else {
                //平移
                //这一句所花时间：休眠时间=移动的距离：总距离（行高）
                //移动的距离=(这一句所花时间：休眠时间)*总距离（行高）

               // float delta=((currentPosition-timePoint)/sleepTime)*textHight;

                //屏幕的坐标=行高+移动的距离
                plush=textHight+((currentPosition-timePoint)/sleepTime)*textHight;
            }
            canvas.translate( 0,-plush );

            //绘制歌词;绘制当前句，
            String currentText = lyrics.get( index ).getContent();
            canvas.drawText( currentText, width / 2, hight / 2, paint );

            // 前面，
            float tempY = hight / 2;//Y轴的中间坐标
            for (int i = index - 1; i >= 0; i--) {

                //每一句歌词
                String precurrent = lyrics.get( i ).getContent();
                tempY = tempY - textHight;
                if (tempY < 0) {
                    break;
                }

                canvas.drawText( precurrent, width / 2, tempY, whitepaint );

            }


            // 后面

            tempY = hight / 2;//Y轴的中间坐标
            for (int i = index + 1; i < lyrics.size(); i++) {

                //每一句歌词
                String nextcurrent = lyrics.get( i ).getContent();
                tempY = tempY + textHight;
                if (tempY > hight) {
                    break;
                }

                canvas.drawText( nextcurrent, width / 2, tempY, whitepaint );

            }

        } else {
            //没有歌词
            canvas.drawText( "没有发现歌词...", width / 2, hight / 2, paint );
        }
    }

    /**
     * 根据当前播放的位置，找出该高亮显示那句歌词
     *
     * @param currentPosition
     */
    public void setshowNextLyric(int currentPosition) {



        this.currentPosition = currentPosition;
        if (lyrics == null || lyrics.size() == 0)
            return;


        for (int i = 1; i < lyrics.size(); i++) {

            if (currentPosition < lyrics.get( i ).getTimePoint()) {

                int tempIndex = i - 1;

                if (currentPosition >= lyrics.get( tempIndex ).getTimePoint()) {
                    //当前正在播放的哪句歌词
                    index = tempIndex;
                    sleepTime = lyrics.get( index ).getSleepTime();
                    timePoint = lyrics.get( index ).getTimePoint();
                }

            }

        }


        //重新绘制
        invalidate();//在主线程中调用

        /*//在子线程中
        postInvalidate();*/
    }
}
