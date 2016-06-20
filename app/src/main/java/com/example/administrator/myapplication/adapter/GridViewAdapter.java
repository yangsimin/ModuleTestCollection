package com.example.administrator.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class GridViewAdapter extends BaseAdapter
{
    private Context context;

    private String[] items;

    public GridViewAdapter(Context context, String[] items)
    {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount()
    {
        return items.length;
    }

    @Override
    public String getItem(int position)
    {
        return items[position];
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView btn;
        if (convertView == null)
        {
            btn = new TextView(context);
        }
        else
        {
            btn = (TextView) convertView;
        }
        btn.setText(getItem(position));
        btn.setTextColor(Color.BLACK);
        btn.setGravity(Gravity.CENTER);
        btn.setBackground(context.getResources().getDrawable(R.drawable.selector_btn));
        btn.setPadding(0, 10, 0, 10);
        return btn;
    }
}
