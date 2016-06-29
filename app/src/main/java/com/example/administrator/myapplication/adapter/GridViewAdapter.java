package com.example.administrator.myapplication.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class GridViewAdapter extends BaseAdapter
{
    private Context context;

    private String[] items;

    private List<Integer> enbleItem;

    private OnClickListener listener;

    public GridViewAdapter(Context context, String[] items,
                           List<Integer> enableItem, OnClickListener listener)
    {
        this.context = context;
        this.items = items;
        this.enbleItem = enableItem;
        this.listener = listener;
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
//        btn.setTextColor(Color.BLACK);
        btn.setGravity(Gravity.CENTER);
//        btn.setBackground(context.getResources().getDrawable(R.drawable.selector_btn));
        btn.setPadding(0, 10, 0, 10);
        if (!enbleItem.contains(position))
            btn.setEnabled(false);
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
