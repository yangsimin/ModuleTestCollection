package com.example.administrator.myapplication.fragment;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.utils.MethodCollection;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class CheckTimeFragment extends BaseFragment
{
    private boolean isRunning;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            tvTime.setText(MethodCollection.getCurTime());
            if (isRunning)
            {
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    private TextView tvTime;

    @Override
    public int getLayout()
    {
        return R.layout.fragment_check_time;
    }

    @Override
    protected void initViews()
    {
        tvTime = (TextView) rootView.findViewById(R.id.time_tv);
    }

    @Override
    protected void initData()
    {
        isRunning = true;
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        isRunning = false;
    }
}
