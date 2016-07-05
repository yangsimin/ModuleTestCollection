package com.example.administrator.myapplication.fragment;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.utils.Constants;
import com.example.administrator.myapplication.utils.MethodCollection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class GPRSTestFragment extends BaseFragment
{
    private TextView tvGPRS;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (msg.what == 1)
            {
                tvGPRS.setText("当前手机移动数据：可用");
                getActivity().setResult(Constants.RESULT_WELL);
            } else
            {
                tvGPRS.setText("当前手机移动数据：不可用");
                getActivity().setResult(Constants.RESULT_BAD);
            }
            MethodCollection.delayFinish(getActivity(), 1000);
        }
    };

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
//        if (MethodCollection.isGPRSAvailable(getActivity()))
//        {
//            tvGPRS.setText("当前手机移动数据：可用");
//            getActivity().setResult(Constants.RESULT_WELL);
//        } else
//        {
//            tvGPRS.setText("当前手机移动数据：不可用");
//            getActivity().setResult(Constants.RESULT_BAD);
//        }
//        MethodCollection.delayFinish(getActivity(), 1000);

        tvGPRS.setText("测试网络中，请等待...");

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
//                if (pingIpAddress("www.baidu.com"))
//                {
////                    Toast.makeText(getActivity(), "successful~", Toast.LENGTH_SHORT).show();
//                    Log.d("GPRS", "successful~");
//                }
//                else
//                {
////                    Toast.makeText(getActivity(), "failed~", Toast.LENGTH_SHORT).show();
//                    Log.d("GPRS", "failed~");
//                }
                String result = doGet("http://www.baidu.com");

                if (result != null)
                {
                    handler.sendEmptyMessage(1);
                }
                else
                {
                    handler.sendEmptyMessage(0);
                }

            }
        }).start();

    }


    private boolean pingIpAddress(String ipAddress)
    {
        try
        {
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 100 " + ipAddress);
            int status = process.waitFor();
            if (status == 0) return true;
            else return false;
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private String doGet(String ipAddress)
    {
        try
        {
            URL url = new URL(ipAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
//            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line);
                }
                reader.close();
                return buffer.toString();
            }
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (ProtocolException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
