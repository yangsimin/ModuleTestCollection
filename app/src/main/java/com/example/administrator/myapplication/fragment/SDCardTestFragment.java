package com.example.administrator.myapplication.fragment;

import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.utils.Constants;
import com.example.administrator.myapplication.utils.MethodCollection;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class SDCardTestFragment extends BaseFragment
{
    private TextView tvSDCard;

    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;

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
        if (MethodCollection.writeToSD("hello world", getActivity()))
//        checkPermission();
//        if (MethodCollection.checkSDcardWritable("storage/sdcard1/Android/data/com.example.administrator.myapplication/"))
        {
            tvSDCard.setText("SD卡状态：可用");
            getActivity().setResult(Constants.RESULT_WELL);
        }
        else
        {
            tvSDCard.setText("SD卡状态：不可用");
            getActivity().setResult(Constants.RESULT_BAD);
        }
        MethodCollection.delayFinish(getActivity(), 1000);
    }
}
