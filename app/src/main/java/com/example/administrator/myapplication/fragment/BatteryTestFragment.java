package com.example.administrator.myapplication.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;

/**
 * Created by Administrator on 2016/6/17 0017.
 */
public class BatteryTestFragment extends BaseFragment
{
    private TextView tvBatteryInfo;
    private BroadcastReceiver receiver;

    @Override
    public int getLayout()
    {
        return R.layout.fragment_only_textview;
    }

    @Override
    protected void initViews()
    {
        tvBatteryInfo = (TextView) rootView.findViewById(R.id.content_tv);
    }

    @Override
    protected void initData()
    {
        createReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(receiver, filter);
    }

    private void createReceiver()
    {
        receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_BATTERY_CHANGED))
                {
                    // BatteryManager.BATTERY_STATUS_CHARGING 表示是充电状态
                    // BatteryManager.BATTERY_STATUS_DISCHARGING 放电中
                    // BatteryManager.BATTERY_STATUS_NOT_CHARGING 未充电
                    // BatteryManager.BATTERY_STATUS_FULL 电池满
                    //电池状态
                    int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);

                    String strStatus = null;
                    if (status == BatteryManager.BATTERY_STATUS_CHARGING) strStatus = "充电中";
                    else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) strStatus = "放电中";
                    else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) strStatus = "未充电";
                    else if (status == BatteryManager.BATTERY_STATUS_FULL) strStatus = "电池满";
                    else if (status == BatteryManager.BATTERY_STATUS_UNKNOWN) strStatus = "未知";


                    // BatteryManager.BATTERY_HEALTH_GOOD 良好
                    // BatteryManager.BATTERY_HEALTH_OVERHEAT 过热
                    // BatteryManager.BATTERY_HEALTH_DEAD 没电
                    // BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE 过电压
                    // BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE 未知错误
                    //电池健康情况
                    int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);

                    String strHealth = null;
                    if (health == BatteryManager.BATTERY_HEALTH_GOOD) strHealth = "良好";
                    else if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) strHealth = "过热";
                    else if (health == BatteryManager.BATTERY_HEALTH_DEAD) strHealth = "没电";
                    else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) strHealth = "过压";
                    else if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) strHealth = "未知错误";


                    //电池最大容量
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                    //电池电压
                    int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
                    //电池电量
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                    //电池温度
                    int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
                    StringBuffer bufInfo = new StringBuffer();
                    bufInfo.append("电池状态：");
                    bufInfo.append(strStatus);
                    bufInfo.append("\n电池健康情况：");
                    bufInfo.append(strHealth);
//                    bufInfo.append("\n电池最大容量：");
//                    bufInfo.append(scale);
                    bufInfo.append("\n电池电压：");
                    bufInfo.append(voltage);
                    bufInfo.append("mv");
                    bufInfo.append("\n电池温度：");
                    bufInfo.append(temperature * 0.1);
                    bufInfo.append("℃");
                    bufInfo.append("\n电池剩余电量:");
                    bufInfo.append(level);
                    bufInfo.append("%");

                    tvBatteryInfo.setText(bufInfo.toString());
                }
            }
        };
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (receiver != null)
        {
            getActivity().unregisterReceiver(receiver);
        }

    }
}
