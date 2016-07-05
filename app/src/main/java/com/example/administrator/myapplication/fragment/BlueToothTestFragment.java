package com.example.administrator.myapplication.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.adapter.BlueToothDeviceAdapter;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.utils.ClsUtils;
import com.example.administrator.myapplication.utils.Constants;
import com.example.administrator.myapplication.utils.MethodCollection;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/6/16 0016.
 */
public class BlueToothTestFragment extends BaseFragment
{
    private TextView btAction, btState;
    private BluetoothReceiver searchReceiver;

    private ProgressBar btSearching;
    private ListView lvDevices;

    private List<BluetoothDevice> devices;
    private BlueToothDeviceAdapter adapter;

    private BluetoothAdapter bluetoothAdapter;
    private ParingReceived paringReceiver;

    //    private String btAddress = "A0:E4:53:A6:1B:7E";
    private String btAddress = "00:1B:35:01:FE:E2";

    @Override
    public int getLayout()
    {
        return R.layout.fragment_bluetooth_test;
    }

    @Override
    protected void initViews()
    {
        btState = (TextView) rootView.findViewById(R.id.bluetooth_state_tv);
        btAction = (TextView) rootView.findViewById(R.id.bluetooth_action_tv);
        btSearching = (ProgressBar) rootView.findViewById(R.id.bluetooth_searching_pb);
        lvDevices = (ListView) rootView.findViewById(R.id.bluetooth_devices_lv);
    }

    @Override
    protected void initData()
    {
        //获取蓝牙设备
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //注册搜索广播
        searchReceiver = new BluetoothReceiver();
        IntentFilter searchFilter = new IntentFilter();
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        searchFilter.addAction(BluetoothDevice.ACTION_FOUND);
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        searchFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(searchReceiver, searchFilter);

        //注册配对广播
        paringReceiver = new ParingReceived();
        IntentFilter paringFilter = new IntentFilter();
        paringFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        paringFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//            paringFilter.setPriority(Integer.MAX_VALUE);
        getActivity().registerReceiver(paringReceiver, paringFilter);

        if (!bluetoothAdapter.isEnabled()) bluetoothAdapter.enable();
        else
        {
            btState.setText("蓝牙：打开");
            bluetoothAdapter.startDiscovery();//开始搜索
        }

        devices = new ArrayList<>();
        adapter = new BlueToothDeviceAdapter(getActivity(), devices, new BlueToothDeviceAdapter.onDeviceClickListener()
        {
            @Override
            public void onClick(BluetoothDevice device)
            {
                try
                {
                    //若被点击，终止搜索
                    bluetoothAdapter.cancelDiscovery();
                    //若已配对，开始传输
                    if (BluetoothDevice.BOND_BONDED == device.getBondState()) connect(device);
                        //建立配对
                    else device.createBond();

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        lvDevices.setAdapter(adapter);

    }

    /**
     * 搜索蓝牙设备广播
     */
    private class BluetoothReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
            {
                //开始搜索
                btAction.setText("开始搜索蓝牙设备...");
                btSearching.setVisibility(View.VISIBLE);
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
            {
                if (bluetoothAdapter.isEnabled())
                {
                    btState.setText("蓝牙：打开");
                    bluetoothAdapter.startDiscovery();
                }
                else btState.setText("蓝牙：关闭");
            }
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                //找到设备
                btAction.setText("搜索到了蓝牙设备，继续搜索...");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (devices.indexOf(device) == -1)
                {
                    devices.add(device);
                }
                adapter.notifyDataSetChanged();

                if (btAddress.equals(device.getAddress()))
                {
                    //找到设备，终止搜索
                    bluetoothAdapter.cancelDiscovery();
                    //若还没配对，则开始配对
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) device.createBond();

                        //若已经配对，则开始传输
                    else try
                    {
                        connect(device);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                //搜索结束
                btAction.setText("搜索结束");
                btSearching.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 配对广播
     */
    private BluetoothDevice btDevice = null;

    private class ParingReceived extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action))
            {
                btAction.setText("开始配对...");
                btSearching.setVisibility(View.VISIBLE);
                btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try
                {
//                    ClsUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
                    btDevice.setPin(ClsUtils.convertPinToBytes(btDevice.getClass(), "1234"));

                } catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
            {
                if (btDevice != null)
                {
                    int bondState = btDevice.getBondState();
                    switch (bondState)
                    {
                        case BluetoothDevice.BOND_BONDING:
                            btAction.setText("正在配对...");
                            btSearching.setVisibility(View.VISIBLE);
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            btAction.setText("配对完成");
                            btSearching.setVisibility(View.GONE);
                            try
                            {
                                connect(btDevice);
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            break;
                        case BluetoothDevice.BOND_NONE:
                            btAction.setText("未有配对");
                            btSearching.setVisibility(View.GONE);
                            break;
                    }
                }
            }

        }
    }

    private void connect(BluetoothDevice device) throws IOException
    {
        //建立连接
        BluetoothSocket mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        mSocket.connect();
        if (mSocket.isConnected())
        {
            btAction.setText("正在传输数据");
            btSearching.setVisibility(View.VISIBLE);
            OutputStream mOutputStream = mSocket.getOutputStream();
            mOutputStream.write("successful~\n".getBytes());
            mOutputStream.flush();
            btAction.setText("数据传输完成");
            btSearching.setVisibility(View.GONE);
            mOutputStream.close();
            bluetoothAdapter.disable();
            getActivity().setResult(Constants.RESULT_WELL);
            MethodCollection.delayFinish(getActivity(), 1000);
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        if (searchReceiver != null)
        {
            getActivity().unregisterReceiver(searchReceiver);
        }
        if (paringReceiver != null)
        {
            getActivity().unregisterReceiver(paringReceiver);
        }
        if (bluetoothAdapter != null)
        {
            bluetoothAdapter.disable();
            bluetoothAdapter.cancelDiscovery();
        }
    }
}
