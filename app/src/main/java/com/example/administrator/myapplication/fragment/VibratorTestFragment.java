package com.example.administrator.myapplication.fragment;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.utils.MethodCollection;

/**
 * Created by Administrator on 2016/6/16 0016.
 */
public class VibratorTestFragment extends BaseFragment
{
    @Override
    public int getLayout()
    {
        return R.layout.fragment_vibrator_test;
    }

    @Override
    protected void initEvents()
    {
        MethodCollection.vibrateOnce(getActivity());
    }
}
