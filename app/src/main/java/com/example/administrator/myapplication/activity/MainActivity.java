package com.example.administrator.myapplication.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.adapter.GridViewAdapter;
import com.example.administrator.myapplication.model.DevInfo;
import com.example.administrator.myapplication.model.PMCCheck;
import com.example.administrator.myapplication.utils.Constants;
import com.example.administrator.myapplication.utils.DevInfoUtil;
import com.example.administrator.myapplication.utils.MethodCollection;
import com.example.administrator.myapplication.utils.PMCCheckUtil;
import com.example.administrator.myapplication.widget.MyGridView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private MyGridView gvMain;

    private GridViewAdapter gridViewAdapter;

    private TextView tvVersion;

    private int index;

    private boolean beginAll;

    private List<Integer> enableItem;

    private List<Boolean> booItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gvMain = (MyGridView) findViewById(R.id.main_gv);
        tvVersion = (TextView) findViewById(R.id.version_tv);

        //获取包信息
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try
        {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        //设置版本号
        tvVersion.setText(info.versionName);

        //是否使用配置文件
        if (Constants.useFilter) testFilter();

        gridViewAdapter = new GridViewAdapter(this, Constants.ITEMS, enableItem, booItem, new GridViewAdapter.OnClickListener()
        {
            @Override
            public void onClick(int position, View v)
            {
                if (position == Constants.ITEMS.length - 1)
                {
                    beginAll = true;
                    jumpToBaseCheckActivity(0);
                } else
                {
                    jumpToBaseCheckActivity(position);
                }
            }
        });
        gvMain.setAdapter(gridViewAdapter);
    }

    private void testFilter()
    {
        PMCCheck pmcCheck = null;
        try
        {
            pmcCheck = PMCCheckUtil.getInstance(this);
        } catch (IOException | XmlPullParserException e)
        {
            e.printStackTrace();
            showAlertDialog("错误！", "没有找到配置文件！");
        }

        try
        {
            DevInfo devInfo = DevInfoUtil.getInstance();
            Log.d("devinfo", "系统" + devInfo.toString());
            Log.d("devinfo", "PMC" + pmcCheck.getId());
            if (!devInfo.getDeviceID().equals(pmcCheck.getId()))
                showAlertDialog("错误", "设备唯一标识与文件不一样，无法检测");
            else
            {
                //写入配置值
                List<PMCCheck.Setting> settings = pmcCheck.getSettings();
                List<DevInfo.Functions> functionsList = devInfo.getFunctionsList();
                for (int i = 0; i < settings.size(); i++)
                {
                    PMCCheck.Setting setting = settings.get(i);
                    for (int j = 0; j < functionsList.size(); j++)
                    {
                        DevInfo.Functions functions = functionsList.get(j);
                        if (setting.getName().equals(functions.getCode()))
                        {
                            setting.setValue(functions.getValue());
                        }
                    }
                }

                pmcCheck.setSettings(settings);
                pmcCheck.setSerialNum(devInfo.getSN());
                pmcCheck.setHardVersion(devInfo.getHardVersion());
                //获取版本号
                PackageManager manager = this.getPackageManager();
                PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
                pmcCheck.setProVersion(info.versionName);
                PMCCheckUtil.serializer(pmcCheck);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            showAlertDialog("错误！", "没有找到系统文件！");
        }
        enableItem = new ArrayList<>();
        booItem = new ArrayList<>();
        enableItem.add(Constants.ITEMS.length - 1);
        booItem.add(false);
        try
        {
            List<PMCCheck.Result> results = PMCCheckUtil.getInstance(this).getResults();
            for (int i = 0; i < results.size(); i++)
            {
                String name = results.get(i).getItemName();
                for (int j = 0; j < Constants.ITEMS.length; j++)
                {
                    if (Constants.ITEMS[j].equals(name))
                    {
                        enableItem.add(j);
                        booItem.add(Boolean.parseBoolean(results.get(i).getItemResult()));
                    }
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        TextView tvBtn = (TextView) gvMain.getChildAt(requestCode);
        if (tvBtn == null) return;

        PMCCheck.Result result = getItemByCode(requestCode);
        switch (resultCode)
        {
            case Constants.RESULT_WELL:
                tvBtn.setTextColor(Color.parseColor("#238E23"));
                //写入xml中
                if (result != null)
                {
                    result.setItemWellCount(Integer.parseInt(result.getItemWellCount()) + 1 + "");
                    result.setItemResult("true");
                }
                break;
            case Constants.RESULT_BAD:
                //写入xml中
                tvBtn.setTextColor(Color.parseColor("#FF2400"));
                if (result != null)
                {
                    result.setItemBadCount(Integer.parseInt(result.getItemBadCount()) + 1 + "");
                    result.setItemResult("false");
                }
                break;
        }

        if (result != null)
        {
            result.setItemTestTime(MethodCollection.getCurTime("yyyy-MM-dd'T'HH:mm:ssZZZZZ"));
            try
            {
                PMCCheckUtil.serializer(PMCCheckUtil.getInstance(this));
            } catch (IOException | XmlPullParserException e)
            {
                e.printStackTrace();
            }
        }

        if (beginAll & index < Constants.ITEMS.length - 2)
        {
            jumpToBaseCheckActivity(++index);
            if (index == Constants.ITEMS.length - 2)
            {
                beginAll = false;
                index = 0;
            }
        }
    }

    private void jumpToBaseCheckActivity(int position)
    {
        Intent intent = new Intent(MainActivity.this, BaseCheckActivity.class);
        intent.putExtra("position", position);
        startActivityForResult(intent, position);
    }

    private PMCCheck.Result getItemByCode(int requestCode)
    {
        try
        {
            List<PMCCheck.Result> results = PMCCheckUtil.getInstance(this).getResults();
            String itemName = Constants.ITEMS[requestCode];
            for (int i = 0; i < results.size(); i++)
            {
                if (results.get(i).getItemName().equals(itemName)) return results.get(i);
            }

        } catch (IOException | XmlPullParserException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private void showAlertDialog(String title, String msg)
    {
        AlertDialog.Builder bb = new AlertDialog.Builder(this);
        bb.setTitle(title);
        bb.setMessage(msg);
        bb.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                MainActivity.this.finish();
            }
        });
        bb.setCancelable(false);
        bb.show();
    }
}
