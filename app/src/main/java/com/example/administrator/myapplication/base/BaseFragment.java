package com.example.administrator.myapplication.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public abstract class BaseFragment extends Fragment
{
    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        rootView = inflater.inflate(getLayout(), container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initEvents();
        initData();
    }

    public abstract int getLayout();

    protected void initViews(){}

    protected void initData(){}

    protected void initEvents(){}
}
