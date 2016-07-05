package com.example.administrator.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.administrator.myapplication.utils.Constants;

import java.util.List;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class GridViewAdapter extends BaseAdapter
{
    private Context context;

    private String[] items;

    private List<Integer> enbleItem;

    private List<Boolean> booItem;

    private OnClickListener listener;

    public GridViewAdapter(Context context, String[] items,
                           List<Integer> enableItem, List<Boolean> booItem, OnClickListener listener)
    {
        this.context = context;
        this.items = items;
        this.enbleItem = enableItem;
        this.listener = listener;
        this.booItem = booItem;
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        Button btn;
        if (convertView == null)
        {
            btn = new Button(context);
        }
        else
        {
            btn = (Button) convertView;
        }
        btn.setText(getItem(position));
        btn.setGravity(Gravity.CENTER);
        btn.setPadding(0, 10, 0, 10);
        if (Constants.useFilter)
        {
            if (!enbleItem.contains(position)) btn.setEnabled(false);
            else if (booItem.get(enbleItem.indexOf(position))) btn.setTextColor(Color.parseColor("#238E23"));
        }
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onClick(position, v);
            }
        });
        return btn;
    }

    public interface OnClickListener
    {
        void onClick(int position, View v);
    }

}
