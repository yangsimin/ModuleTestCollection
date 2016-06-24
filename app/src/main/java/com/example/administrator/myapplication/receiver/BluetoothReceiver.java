package com.example.administrator.myapplication.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class BluetoothReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
        {
            //开始搜索
        }
        if (BluetoothDevice.ACTION_FOUND.equals(action))
        {
            //找到设备
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        }
        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
        {
            //搜索结束
        }
    }
}
