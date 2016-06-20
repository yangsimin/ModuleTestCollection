package com.example.administrator.myapplication.fragment;

import android.media.MediaPlayer;
import android.media.RingtoneManager;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;

import java.io.IOException;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class SoundTestFragment extends BaseFragment
{
    @Override
    public int getLayout()
    {
        return R.layout.fragment_sound_test;
    }

    @Override
    protected void initData()
    {
        MediaPlayer mp = new MediaPlayer();
        try
        {
            mp.setDataSource(getActivity(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mp.prepare();
            mp.start();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
