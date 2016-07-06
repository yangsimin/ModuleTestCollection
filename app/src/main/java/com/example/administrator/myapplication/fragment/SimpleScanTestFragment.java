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
import com.keymantek.serialport.utils.HexUtils;
import com.keymantek.serialport.utils.SerialPortOpera;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class SimpleScanTestFragment extends BaseFragment
{
    private static final String DATA = "04E40400FF14";
    private static final String STR_BANRATE = "9600";
    private static final String STR_BIT = "8";
    private static final String STR_EVENT = "N";
    private static final String STR_STOP = "1";
    private static final int TYPE = 3;

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
            Log.d("scan", receStr);
            if (!TextUtils.isEmpty(receStr))
            {
                if (receStr.contains("04 D0 00 00 FF 2C 12 F3 00 00 0B"))
                {
                    String fc = receStr.substring(receStr.indexOf("0B") + 3, receStr.indexOf("FC")).trim();
                    String[] split = fc.split(" ");
                    for (int i = 0; i < split.length; i++)
                    {
                        int num = Integer.parseInt(split[i])%10;
                        tvReceive.append(num + "");
                    }
                    mSerialPortOpera.closeSerialPort(TYPE);
                    autoSend = false;
                }
            }

//            tvReceive.setText(receStr);

            if (autoSend)
            {
                mSerialPortOpera.SerialPortWrite(HexUtils.hexStringToByte(DATA));
                handler.postDelayed(this, 2000);
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
        tvSend.setText("发送数据：04E40400FF14");
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
                    "/dev/ttyMT3",
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
                readData();
//                int readCount;
//                byte[] bytes = new byte[40];
//                try
//                {
//                    if ((readCount = inStream.read(bytes)) != -1)
//                    {
//                        Log.w("iscoming", "有大小：" + readCount + "");
////                        String data = bytesToHexString(bytes, readCount);
//                        String data = HexUtils.bytesToHexString(bytes, bytes.length);
//                        publishProgress(data);
//                    }
//
//                } catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
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

    public static final String bytesToHexString(byte[] bArray, int size)
    {
        StringBuffer sb = new StringBuffer(size);
        String sTemp;
        for (int i = 0; i < size; i++)
        {
            sTemp = Integer.valueOf(0xF0 & bArray[i]).toString();
            sb.append(sTemp);
            sTemp = Integer.valueOf(0x0F & bArray[i]).toString();
            sb.append(sTemp);
        }
        return sb.toString();
    }
}
