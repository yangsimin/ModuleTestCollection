package com.example.administrator.myapplication.fragment;

import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.utils.Contants;
import com.example.administrator.myapplication.utils.MethodCollection;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class GPRSTestFragment extends BaseFragment
{
    private TextView tvGPRS;

    @Override
    public int getLayout()
    {
        return R.layout.fragment_gprs_test;
    }

    @Override
    protected void initViews()
    {
        tvGPRS = (TextView) rootView.findViewById(R.id.gprs_tv);
    }

    @Override
    protected void initData()
    {
        if (MethodCollection.isGPRSAvailable(getActivity()))
        {
            tvGPRS.setText("当前手机移动数据：可用");
            getActivity().setResult(Contants.RESULT_WELL);
        }
        else
        {
            tvGPRS.setText("当前手机移动数据：不可用");
            getActivity().setResult(Contants.RESULT_BAD);
        }
        MethodCollection.delayFinish(getActivity(), 1000);
    }
}
