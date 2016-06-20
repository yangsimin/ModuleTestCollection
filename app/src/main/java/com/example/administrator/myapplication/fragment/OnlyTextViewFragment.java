package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;

/**
 * Created by Administrator on 2016/6/16 0016.
 */
public class OnlyTextViewFragment extends BaseFragment
{
    private TextView tvContent;

    @Override
    public int getLayout()
    {
        return R.layout.fragment_only_textview;
    }

    @Override
    protected void initViews()
    {
        tvContent = (TextView) rootView.findViewById(R.id.content_tv);
        Bundle arguments = getArguments();
        tvContent.setText(arguments.getString("content"));

    }
}
