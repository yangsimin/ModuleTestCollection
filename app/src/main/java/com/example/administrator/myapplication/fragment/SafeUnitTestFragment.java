package com.example.administrator.myapplication.fragment;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.utils.Constants;
import com.keymantek.serialport.utils.HexUtils;
import com.keymantek.serialport.utils.SerialPortOpera;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class SafeUnitTestFragment extends BaseFragment implements View.OnClickListener
{
    private static final int TYPE = 2;

    private SerialPortOpera mSerialPortOpera;
    private ReadThread readThread;
    private InputStream mInputStream;

    private EditText etReception;//接收区
    private EditText etSend;        //发送区
    private Button btnSend;
    private Button btnOn;
    private Button btnOff;
    private Button btnClearReceiver;
    private Button btnClearSend;
    private Button btnSetting;
    private Button btnAutosend;
    private Button btnBusiness;
    private Button btnOpera;
    private CheckBox cbSend;
    private CheckBox cbReceiver;

    private String strBanrate;    //波特率
    private String strBit;        //数据位
    private String strEvent;    //校验位
    private String strStop;    //停止位

    private Button btnWell;
    private Button btnBad;

    private boolean isAuto = false;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            //获取数据
            String data = etSend.getText().toString().trim();
            if (cbSend.isChecked())
                mSerialPortOpera.SerialPortWrite(HexUtils.hexStringToByte(data));
            else mSerialPortOpera.SerialPortWrite(data.getBytes());
            if (isAuto) handler.postDelayed(this, 1000);
        }
    };

    @Override
    public int getLayout()
    {
        return R.layout.fragment_safeunit_test;
    }

    @Override
    protected void initViews()
    {
        etReception = (EditText) rootView.findViewById(R.id.reception_et);
        etReception.setKeyListener(null);
        etSend = (EditText) rootView.findViewById(R.id.send_et);
        btnSend = (Button) rootView.findViewById(R.id.send);
        btnOn = (Button) rootView.findViewById(R.id.on);
        btnOff = (Button) rootView.findViewById(R.id.off);
        btnClearReceiver = (Button) rootView.findViewById(R.id.clear_receiver);
        btnClearSend = (Button) rootView.findViewById(R.id.clear_send);
        btnSetting = (Button) rootView.findViewById(R.id.setting);
        btnAutosend = (Button) rootView.findViewById(R.id.autosend);
        cbSend = (CheckBox) rootView.findViewById(R.id.cb_send);
        cbReceiver = (CheckBox) rootView.findViewById(R.id.cb_receiver);
        btnBusiness = (Button) rootView.findViewById(R.id.business_btn);
        btnOpera = (Button) rootView.findViewById(R.id.operationer_btn);

        btnWell = (Button) rootView.findViewById(R.id.well);
        btnBad = (Button) rootView.findViewById(R.id.bad);
    }

    @Override
    protected void initData()
    {
        //设置默认串口参数
        strBanrate = "9600";
        strBit = "8";
        strEvent = "E";
        strStop = "1";

        mSerialPortOpera = new SerialPortOpera();
    }

    @Override
    protected void initEvents()
    {
        //按钮组件设置监听
        btnSend.setOnClickListener(this);
        btnOn.setOnClickListener(this);
        btnOff.setOnClickListener(this);
        btnClearReceiver.setOnClickListener(this);
        btnClearSend.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnAutosend.setOnClickListener(this);
        btnBusiness.setOnClickListener(this);
        btnOpera.setOnClickListener(this);

        btnWell.setOnClickListener(this);
        btnBad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.on:
                btnOn.setEnabled(false);
                btnOff.setEnabled(true);
                btnSend.setEnabled(true);
                btnAutosend.setEnabled(true);
                try
                {
                    //打开串口并设置串口参数
                    mSerialPortOpera.openSerialPort(
                            "/dev/ttyMT2",
                            Integer.parseInt(strBanrate),
                            Integer.parseInt(strBit),
                            strEvent.toCharArray()[0],
                            Integer.parseInt(strStop),
                            TYPE);

                    //获取输入流
                    mInputStream = mSerialPortOpera.SerialPortReceiver();
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
                break;
            case R.id.send:
                //发送数据
                //获取编辑框内容
                handler.post(runnable);
                break;
            case R.id.autosend:
                isAuto = !isAuto;
                if (isAuto)
                {
                    btnSend.setEnabled(false);
                    btnAutosend.setText("停止自动发送");
                    handler.postDelayed(runnable, 1000);
                } else
                {
                    btnSend.setEnabled(true);
                    btnAutosend.setText("自动发送");
                    handler.removeCallbacks(runnable);
                }
                break;
            case R.id.off:
                btnOff.setEnabled(false);
                btnSend.setEnabled(false);
                btnOn.setEnabled(true);
                btnAutosend.setEnabled(false);
                if (readThread != null) readThread.cancel(true);
                mSerialPortOpera.closeSerialPort(TYPE);
                break;
            case R.id.clear_send:
                etSend.setText("");
                break;
            case R.id.clear_receiver:
                etReception.setText("");
                break;
            case R.id.setting:
                //加载设置窗口
                View view = View.inflate(getActivity(), R.layout.setting_layout, null);
                //获取四个Spinner
                Spinner spBanrate = (Spinner) view.findViewById(R.id.spinner_banrate);
                Spinner spBit = (Spinner) view.findViewById(R.id.spinner_bit);
                Spinner spEvent = (Spinner) view.findViewById(R.id.spinner_event);
                Spinner spStop = (Spinner) view.findViewById(R.id.spinner_stop);
                //设置默认参数
                setSpinnerItemSelectedByValue(spBanrate, strBanrate);
                setSpinnerItemSelectedByValue(spBit, strBit);
                setSpinnerItemSelectedByValue(spEvent, strEvent);
                setSpinnerItemSelectedByValue(spStop, strStop);
                //设置监听
                setSpinnerListener(spBanrate, "strBanrate");
                setSpinnerListener(spBit, "strBit");
                setSpinnerListener(spEvent, "strEvent");
                setSpinnerListener(spStop, "strStop");
                //设置弹出框
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view).setTitle("设置");
                builder.setPositiveButton("确定", null);
                builder.create().show();
                break;
            case R.id.well:
                getActivity().setResult(Constants.RESULT_WELL);
                getActivity().finish();
                break;
            case R.id.bad:
                getActivity().setResult(Constants.RESULT_BAD);
                getActivity().finish();
                break;
            case R.id.business_btn:
                etSend.setText("E9000300240212E6");
                break;
            case R.id.operationer_btn:
                etSend.setText("E9000300240111E6");
                break;
        }
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
                int count = 0;
                try
                {
                    while (count == 0)
                    {
                        count = mInputStream.available();
                    }
//					byte[] b = new byte[count];
                    byte[] buffer = new byte[count];
                    int readCount = 0; // 已经成功读取的字节的个数
                    while (readCount < count)
                    {
                        readCount += mInputStream.read(buffer, readCount, count
                                - readCount);
                    }
                    if (readCount > 0)
                    {
                        Log.w("iscoming", "有大小：" + readCount + "");
                        boolean isCheck = cbReceiver.isChecked();
                        if (isCheck)
                        {// 判断Hex是否为选中
                            // 将byte数组转换为十六进制字符串
                            String data = HexUtils.bytesToHexString(buffer,
                                    readCount);
                            publishProgress(data);
                        } else
                        {
                            publishProgress(new String(buffer, 0, readCount));
                        }
                    }
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
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
                etReception.append(data);
//                etReception.setText(data);
            }
            super.onProgressUpdate(values);
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

    //设置spinner的默认值
    private void setSpinnerItemSelectedByValue(Spinner spinner, Object value)
    {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        for (int i = 0; i < apsAdapter.getCount(); i++)
        {
            if (value.equals(apsAdapter.getItem(i).toString()))
            {
                spinner.setSelection(i, true);// 默认选中项
                break;
            }
        }
    }

    //设置spinner监听
    private void setSpinnerListener(Spinner spinner, final String str)
    {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if ("strBanrate".equals(str))
                {
                    strBanrate = SafeUnitTestFragment.this.getResources().getStringArray(R.array.banrate)[position];
                }

                if ("strBit".equals(str))
                {
                    strBit = SafeUnitTestFragment.this.getResources().getStringArray(R.array.nBits)[position];
                }

                if ("strEvent".equals(str))
                {
                    strEvent = SafeUnitTestFragment.this.getResources().getStringArray(R.array.nEvent)[position];
                }

                if ("strStop".equals(str))
                {
                    strStop = SafeUnitTestFragment.this.getResources().getStringArray(R.array.nStop)[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mSerialPortOpera.closeSerialPort(TYPE);
    }
}
