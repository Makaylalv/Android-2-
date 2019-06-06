package com.john.software.helpeachother.code.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.john.software.helpeachother.R;


/**
 * Created by Administrator on 2018/1/17.
 */

public class MyToastForBlue {
        private Toast mToast;
        private MyToastForBlue(Context context, CharSequence text, int duration) {
            View v = LayoutInflater.from(context).inflate(R.layout.mytoast_layout_for_blue, null);
            TextView textView = (TextView) v.findViewById(R.id.textView1);
            textView.setText(text);
            mToast = new Toast(context);
            mToast.setDuration(duration);
            mToast.setView(v);
        }

        public static com.john.software.helpeachother.code.Util.MyToastForBlue makeText(Context context, CharSequence text, int duration) {
            return new  com.john.software.helpeachother.code.Util.MyToastForBlue(context, text, duration);
        }
        public void show() {
            if (mToast != null) {
                mToast.show();
                // mToast.setGravity(Gravity.CENTER_HORIZONTAL, 0, -500);
            }
        }
        public void setGravity(int gravity, int xOffset, int yOffset) {
            if (mToast != null) {
                mToast.setGravity(gravity, xOffset, yOffset);
            }
        }
    }

