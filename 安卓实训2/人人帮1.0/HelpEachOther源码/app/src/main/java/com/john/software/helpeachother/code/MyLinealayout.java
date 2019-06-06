package com.john.software.helpeachother.code;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * 解决虚拟菜单键底部导航栏被遮盖的问题
 * Created by Administrator on 2018/1/19.
 */

public class MyLinealayout {

        // For more information, see https://code.google.com/p/android/issues/detail?id=5497
        // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

        public static void assistActivity (Activity activity) {
            new MyLinealayout(activity);
        }
        private Activity activity;
        private View mChildOfContent;
        private int usableHeightPrevious,a;
        private FrameLayout.LayoutParams frameLayoutParams;

        private MyLinealayout(Activity activity) {
            this.activity = activity;
            FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
            mChildOfContent = content.getChildAt(0);
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    possiblyResizeChildOfContent();
                }
            });
            frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
        }

        private void possiblyResizeChildOfContent() {
            int usableHeightNow = computeUsableHeight();
//            LogUtils.e("possiblyResizeChildOfContent","usableHeightNow:"+usableHeightNow);
//            LogUtils.e("possiblyResizeChildOfContent","usableHeightPrevious:"+usableHeightPrevious);
           if (usableHeightNow != usableHeightPrevious) {
                int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
                  Log.i("呀呀呀啊呀啊呀",String.valueOf(usableHeightSansKeyboard));
                //这个判断是为了解决19之前的版本不支持沉浸式状态栏导致布局显示不完全的问题
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
                    Rect frame = new Rect();
                    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                    int statusBarHeight = frame.top;
                    usableHeightSansKeyboard -= statusBarHeight;
                }
                int heightDifference = usableHeightSansKeyboard - usableHeightNow;
                if (heightDifference > (usableHeightSansKeyboard/4)) {
                    // keyboard probably just became visible
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                } else {
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
                        a=0;
                    }else
                    // keyboard probably just became hidden
                    //此处-70是后加的
                    if(usableHeightSansKeyboard==1280){a=50;
                    }else if(usableHeightSansKeyboard==2392) {a=90;
                    }else if(usableHeightSansKeyboard==800) {a=40;
                    }else a=70;
                    //Log.i("及及及及",String.valueOf(statusBarHeight));
                    Log.i("哈哈哈哈哈",String.valueOf(usableHeightSansKeyboard));
                    frameLayoutParams.height = usableHeightSansKeyboard-a;
                }
                mChildOfContent.requestLayout();
                usableHeightPrevious = usableHeightNow;
            }
        }

        private int computeUsableHeight() {
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            Rect r = new Rect();
            mChildOfContent.getWindowVisibleDisplayFrame(r);

            //这个判断是为了解决19之后的版本在弹出软键盘时，键盘和推上去的布局（adjustResize）之间有黑色区域的问题
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                return (r.bottom - r.top)+statusBarHeight;
            }
            return (r.bottom - r.top);
        }


}
