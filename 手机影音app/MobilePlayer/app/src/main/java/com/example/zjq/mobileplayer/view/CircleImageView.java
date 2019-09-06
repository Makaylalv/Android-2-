package com.example.zjq.mobileplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import org.xutils.image.AsyncDrawable;


public class CircleImageView extends AppCompatImageView {

    private Bitmap b;
    public CircleImageView(Context context) {
        super( context );
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
    }

    //自定义view实现过程中很重要的ondraw（）绘制图形的方法
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable=getDrawable();
        //避免初始化之前的错误
        if (drawable==null){
            return;
        }
        if (getWidth()==0||getHeight()==0){
            return;
        }



        if(drawable instanceof BitmapDrawable){
             b=((BitmapDrawable)drawable).getBitmap();
        }else if(drawable instanceof AsyncDrawable){
            b = Bitmap
                    .createBitmap(
                            getWidth(),
                            getHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas1 = new Canvas(b);
            // canvas.setBitmap(bitmap);
            drawable.setBounds(0, 0, getWidth(),
                    getHeight());
            drawable.draw(canvas1);
        }






        if (b==null){
            return;
        }

        Bitmap bitmap=b.copy( Bitmap.Config.ARGB_8888,true );

        int w =getWidth();

        Bitmap roundBitMap=getCroppedBitmap(bitmap,w);
        canvas.drawBitmap(roundBitMap,0,0,null);

    }

    /*
    bmp 初始的Bitmap对象
    radius 圆形图片直径大小
    return 返回裁剪后 bitmap类型对象
    */
    private Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        //比较初始bitmap宽高和给定的圆形直径，判断是否需要缩放裁剪bitmap对象
        if (bmp.getWidth()!=radius||bmp.getHeight()!=radius) {
            sbmp = Bitmap.createScaledBitmap( bmp, radius, radius, false );
        }
        else{
            sbmp=bmp;
        }
            Bitmap output=Bitmap.createBitmap( sbmp.getWidth(),sbmp.getHeight(), Bitmap.Config.ARGB_8888 );
            Canvas canvas=new Canvas( output );

            final Paint paint=new Paint(  );
            final Rect rect=new Rect(0,0,sbmp.getWidth(),sbmp.getHeight());

            paint.setAntiAlias( true );
            paint.setFilterBitmap( true );
            paint.setDither( true );
            canvas.drawARGB( 0,0,0,0 );
            paint.setColor( Color.parseColor( "#BAB399" ) );
            //?
            canvas.drawCircle( sbmp.getWidth()/2+0.7f,sbmp.getHeight()/2+0.7f,sbmp.getWidth()/2+0.1f,paint );
            //核心
            paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN ) );
            canvas.drawBitmap( sbmp,rect,rect,paint );

            return output;


    }
}
