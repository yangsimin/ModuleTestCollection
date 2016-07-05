package com.example.administrator.myapplication.fragment;

import android.util.Log;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.model.PMCCheck;
import com.example.administrator.myapplication.utils.PMCCheckUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Administrator on 2016/6/28 0028.
 */
public class PMCCheckFragment extends BaseFragment
{
    @Override
    public int getLayout()
    {
        return R.layout.fragment_only_textview;
    }

    @Override
    protected void initData()
    {
        try
        {
            PMCCheckUtil.serializer(PMCCheckUtil.getInstance(getActivity()));
            Log.d("pmc", "写入成功");

            PMCCheck pmcCheck = PMCCheckUtil.parser(getActivity());
            String str = pmcCheck.toString();
            Log.d("pmc", str);

        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
    }
}
