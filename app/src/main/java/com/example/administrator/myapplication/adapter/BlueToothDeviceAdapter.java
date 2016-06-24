package com.example.administrator.myapplication.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class BlueToothDeviceAdapter extends BaseAdapter
{
    private List<BluetoothDevice> devices;

    private Context context;

    private onDeviceClickListener listener;

    public BlueToothDeviceAdapter(Context context, List<BluetoothDevice> devices
            , onDeviceClickListener listener)
    {
        this.devices = devices;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getCount()
    {
        return devices.size();
    }

    @Override
    public BluetoothDevice getItem(int position)
    {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        TextView tv = new TextView(context);
        tv.setText(getItem(position).getName() + " " + getItem(position).getAddress());
        tv.setClickable(true);
        tv.setTextSize(20);
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("onclick", "点击了");
                listener.onClick(getItem(position));
            }
        });
        return tv;
    }

    public interface onDeviceClickListener
    {
        void onClick(BluetoothDevice device);
    }
}
