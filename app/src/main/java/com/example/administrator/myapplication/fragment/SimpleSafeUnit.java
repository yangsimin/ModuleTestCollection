package com.example.administrator.myapplication.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.utils.Constants;
import com.example.administrator.myapplication.utils.MethodCollection;
import com.example.administrator.myapplication.utils.PMCCheckUtil;
import com.keymantek.serialport.utils.HexUtils;
import com.keymantek.serialport.utils.SerialPortOpera;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class SimpleSafeUnit extends BaseFragment
{
    private static final String DATA = "E9000200FCE7E6";
    private static final String STR_BANRATE = "9600";
    private static final String STR_BIT = "8";
    private static final String STR_EVENT = "E";
    private static final String STR_STOP = "1";
    private static final int TYPE = 2;

    private String safeVersion;

    private TextView tvReceive, tvSend;

    private InputStream inStream;

    private SerialPortOpera mSerialPortOpera;

    private ReadThread readThread;

    private StringBuffer buffer;

    private Handler handler = new Handler();

    private Boolean autoSend;

    private int sendTimes;

    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            String receStr = buffer.toString();
            buffer.delete(0, buffer.length());
            if (!TextUtils.isEmpty(receStr))
            {
                String trim = receStr.trim();
                Log.d("result", "数据：" + trim);
                Log.d("result", "数据长度:" + trim.length() + "");
                String[] split = trim.split(" ");
                if ("E9".equals(split[0])) ;
                {
                    if ("06".equals(split[2]))
                    {
                        safeVersion = split[6] + split[7] + split[8];
                        try
                        {
                            PMCCheckUtil.getInstance(getActivity()).setSafeVersion(safeVersion);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        } catch (XmlPullParserException e)
                        {
                            e.printStackTrace();
                        }
                        StringBuffer buf = new StringBuffer();
                        for (int i = 0; i < split.length; i++)
                        {
                            buf.append(split[i]);
                            buf.append(" ");
                            if ("E6".equals(split[i]))
                            {
                                String s = buf.toString();
                                tvReceive.setText("接收数据：" + s + "(合格！)");
                                Log.d("result", s);
                                Log.d("result", "检测合格");
                                try
                                {
                                    inStream.close();
                                } catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                                mSerialPortOpera.closeSerialPort(TYPE);
                                autoSend = false;
                                getActivity().setResult(Constants.RESULT_WELL);
                                MethodCollection.delayFinish(getActivity(), 2000);
                                return;
                            }
                        }
                    }
                }
            }

//            Log.d("result", "不合格次数:" + sendTimes);
            if (sendTimes++ > 16)

            {
//                Log.d("result", "检测不合格！！");
                tvReceive.setText("检测不合格！");
                autoSend = false;
                try
                {
                    inStream.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                mSerialPortOpera.closeSerialPort(TYPE);
                getActivity().setResult(Constants.RESULT_BAD);
                MethodCollection.delayFinish(getActivity(), 2000);
            }

            if (autoSend)

            {
                mSerialPortOpera.SerialPortWrite(HexUtils.hexStringToByte(DATA));
                handler.postDelayed(this, 1000);
                tvReceive.setText("发送次数：" + sendTimes);
            }
        }
    };


    @Override
    public int getLayout()
    {
        return R.layout.fragment_simple_laserif;
    }

    @Override
    protected void initViews()
    {
        tvSend = (TextView) rootView.findViewById(R.id.send_tv);
        tvSend.setText("发送数据：E9000200FCE7E6");
        tvReceive = (TextView) rootView.findViewById(R.id.receive_tv);
        mSerialPortOpera = new SerialPortOpera();
    }

    @Override
    protected void initData()
    {
        buffer = new StringBuffer();

        try
        {
            mSerialPortOpera.openSerialPort(
                    "/dev/ttyMT2",
                    Integer.parseInt(STR_BANRATE),
                    Integer.parseInt(STR_BIT),
                    STR_EVENT.toCharArray()[0],
                    Integer.parseInt(STR_STOP),
                    TYPE);

            //获取输入流
            inStream = mSerialPortOpera.SerialPortReceiver();
            //创建读取线程
            readThread = new ReadThread();
            readThread.execute();

        } catch (SecurityException e)
        {
            DisplayError(R.string.error_security);
        } catch (IOException e)
        {
            DisplayError(R.string.error_unknown);
        } catch (InvalidParameterException e)
        {
            DisplayError(R.string.error_configuration);
        }

        //发送数据
        autoSend = true;
        handler.post(runnable);
    }

    @Override
    public void onDetach()
    {
        if (mSerialPortOpera != null)
        {
            mSerialPortOpera.closeSerialPort(TYPE);
            try
            {
                inStream.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        autoSend = false;
        handler.removeCallbacks(runnable);
        handler = null;
        readThread.cancel(true);
        super.onDetach();
    }

    //后台读取数据的线程
    private class ReadThread extends AsyncTask<Void, String, String>
    {
        @Override
        protected String doInBackground(Void... params)
        {
            // TODO Auto-generated method stub
            while (!isCancelled())
            {
//                readData();
                int readCount;
                byte[] bytes = new byte[1024];
                try
                {
                    if ((readCount = inStream.read(bytes)) != -1)
                    {
                        Log.w("iscoming", "有大小：" + readCount + "");
                        String data = HexUtils.bytesToHexString(bytes, readCount);
                        publishProgress(data);
                    }

                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            // TODO Auto-generated method stub
            String data = values[0];
            if (data != null)
            {
                buffer.append(data);
//                tvReceive.append(data);
            }

            super.onProgressUpdate(values);
        }

        private void readData()
        {
            int count = 0;
            try
            {
                while (count == 0)
                {
                    count = inStream.available();
                }
//					byte[] b = new byte[count];
                byte[] buffer = new byte[count];
                int readCount = 0; // 已经成功读取的字节的个数
                while (readCount < count)
                {
                    readCount += inStream.read(buffer, readCount, count
                            - readCount);
                }
                if (readCount > 0)
                {
                    Log.w("iscoming", "有大小：" + readCount + "");
                    //默认十六进制数据
                    String data = HexUtils.bytesToHexString(buffer, readCount);
                    publishProgress(data);
                }
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    //产生异常是的错误对话框
    public void DisplayError(int resourceId)
    {
        AlertDialog.Builder bb = new AlertDialog.Builder(getActivity());
        bb.setTitle("Error");
        bb.setMessage(resourceId);
        bb.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                getActivity().finish();
            }
        });
        bb.show();
    }

}
