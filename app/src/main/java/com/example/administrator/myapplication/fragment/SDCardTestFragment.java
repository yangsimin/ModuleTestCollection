package com.example.administrator.myapplication.fragment;

import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.utils.Contants;
import com.example.administrator.myapplication.utils.MethodCollection;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class SDCardTestFragment extends BaseFragment
{
    private TextView tvSDCard;

    @Override
    public int getLayout()
    {
        return R.layout.fragment_sdcard_test;
    }

    @Override
    protected void initViews()
    {
        tvSDCard = (TextView) rootView.findViewById(R.id.sdcard_tv);
    }

    @Override
    protected void initData()
    {
        if (MethodCollection.writeToSD("hello world"))
        {
            tvSDCard.setText("SD卡状态：可用");
            getActivity().setResult(Contants.RESULT_WELL);
        }
        else
        {
            tvSDCard.setText("SD卡状态：不可用");
            getActivity().setResult(Contants.RESULT_BAD);
        }

        MethodCollection.delayFinish(getActivity(), 1000);
    }
}
