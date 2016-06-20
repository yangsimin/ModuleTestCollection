package com.example.administrator.myapplication.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.utils.Contants;
import com.example.administrator.myapplication.utils.MethodCollection;

/**
 * Created by Administrator on 2016/6/16 0016.
 */
public class BlueToothTestFragment extends BaseFragment
{
    private TextView btState;

    @Override
    public int getLayout()
    {
        return R.layout.fragment_bluetooth_test;
    }

    @Override
    protected void initViews()
    {
        btState = (TextView) rootView.findViewById(R.id.bluetooth_state);
    }

    @Override
    protected void initData()
    {
        if (MethodCollection.isBlueToothAvailable())
        {
            btState.setText("蓝牙当前状态：打开");
            getActivity().setResult(Contants.RESULT_WELL);
        }
        else
        {
            btState.setText("蓝牙当前状态：关闭");
            getActivity().setResult(Contants.RESULT_BAD);
        }

        MethodCollection.delayFinish(getActivity(), 1000);


        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) btState.setText("该设备无蓝牙模块");
        else
        {
            if (!bluetoothAdapter.isEnabled()) bluetoothAdapter.enable();

            Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoveryIntent);
            bluetoothAdapter.startDiscovery();
        }
    }

}
