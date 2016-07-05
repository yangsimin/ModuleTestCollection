package com.example.administrator.myapplication.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class TouchTestFragment extends BaseFragment
{
    private static final int count = 48;

    @Override
    public int getLayout()
    {
        return R.layout.fragment_touch_test;
    }

    @Override
    protected void initData()
    {
        final List<View> list = new ArrayList<>();

        GridLayout gl = (GridLayout) rootView.findViewById(R.id.main_gl);
        for (int i = 0; i < gl.getChildCount(); i++)
        {
            TextView tv = (TextView) gl.getChildAt(i);
            tv.setText(i + "");
            tv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    v.setBackgroundColor(Color.GREEN);
                    if (!list.contains(v)) list.add(v);
                    if (list.size() == count)
                    {
                        getActivity().setResult(Constants.RESULT_WELL);
                        getActivity().finish();
                    }
                }
            });
        }
    }


}
