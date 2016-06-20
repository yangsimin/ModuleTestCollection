package com.example.administrator.myapplication.fragment;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.receiver.MyAdmin;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class SleepTestFragment extends BaseFragment
{

    private DevicePolicyManager manager;

    @Override
    public int getLayout()
    {
        return R.layout.fragment_sleep_test;
    }

    @Override
    protected void initData()
    {
        manager = (DevicePolicyManager) getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(getActivity(), MyAdmin.class);
        boolean isAdminActive = manager.isAdminActive(componentName);
        if (!isAdminActive)
        {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            getActivity().startActivity(intent);
            manager.lockNow();
        }
        else
        {
            manager.lockNow();
        }
    }

}
