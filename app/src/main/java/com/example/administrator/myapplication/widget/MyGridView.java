package com.example.administrator.myapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class MyGridView extends GridView
{
    public MyGridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        return true;
    }
}
