package com.example.zjq.mobileplayer.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zjq.mobileplayer.base.BasePager;

@SuppressLint("ValidFragment")
public class ReplaceFragment extends Fragment {

    private BasePager currPager;

    public ReplaceFragment(BasePager pager) {
        this.currPager=pager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        BasePager pager=currPager;
        if (pager!=null){
            return pager.rootview;
        }
        return  null;

    }

}
