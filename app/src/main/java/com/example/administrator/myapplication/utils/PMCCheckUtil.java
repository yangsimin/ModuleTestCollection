package com.example.administrator.myapplication.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.example.administrator.myapplication.model.PMCCheck;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/28 0028.
 */
public class PMCCheckUtil
{
    private static PMCCheck pmcCheckInstance;

    public static PMCCheck newInstance(Context context) throws IOException, XmlPullParserException
    {
        if (pmcCheckInstance == null)
        {
            pmcCheckInstance = parser(context);
        }
        return pmcCheckInstance;
    }

    public static PMCCheck parser(Context context) throws IOException, XmlPullParserException
    {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();
//        InputStream inStream = context.getAssets().open("PMCCheck.sys");
        InputStream inStream = new FileInputStream(new File(Environment.getExternalStorageDirectory(), "PMCCheck.sys"));
        xpp.setInput(inStream, "UTF-8");

        int eventType = xpp.getEventType();
        PMCCheck pmcCheck = null;
        PMCCheck.Result result = null;
        PMCCheck.Setting setting = null;
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            switch (eventType)
            {
                case XmlPullParser.START_DOCUMENT:
                    pmcCheck = new PMCCheck();
                    pmcCheck.setResults(new ArrayList<PMCCheck.Result>());
                    pmcCheck.setSettings(new ArrayList<PMCCheck.Setting>());
                    break;
                case XmlPullParser.START_TAG:
                    String stName = xpp.getName();
                    if ("设备唯一标识".equals(stName))
                    {
                        pmcCheck.setId(xpp.nextText());
                    }
                    else if ("设置序列号时间".equals(stName))
                    {
                        pmcCheck.setSerialTime(xpp.nextText());
                    }
                    else if ("序列号".equals(stName))
                    {
                        pmcCheck.setSerialNum(xpp.nextText());
                    }
                    else if ("掌机检测程序版本".equals(stName))
                    {
                        pmcCheck.setProVersion(xpp.nextText());
                    }
                    else if ("硬件版本号".equals(stName))
                    {
                        pmcCheck.setHardVersion(xpp.nextText());
                    }
                    else if ("检测结果集".equals(stName))
                    {
                        result = pmcCheck.new Result();
                    }
                    else if ("检测项唯一标识".equals(stName))
                    {
                        result.setItemId(xpp.nextText());
                    }
                    else if ("检测项名称".equals(stName))
                    {
                        result.setItemName(xpp.nextText());
                    }
                    else if ("检测项编码".equals(stName))
                    {
                        result.setItemNum(xpp.nextText());
                    }
                    else if ("检测项结果".equals(stName))
                    {
                        result.setItemResult(xpp.nextText());
                    }
                    else if ("检测时间".equals(stName))
                    {
                        result.setItemTestTime(xpp.nextText());
                    }
                    else if ("检测成功次数".equals(stName))
                    {
                        result.setItemWellCount(xpp.nextText());
                    }
                    else if ("检测失败次数".equals(stName))
                    {
                        result.setItemBadTCount(xpp.nextText());
                    }
                    else if ("配置集".equals(stName))
                    {
                        setting = pmcCheck.new Setting();
                    }
                    else if ("配置名称".equals(stName))
                    {
                        setting.setName(xpp.nextText());
                    }
                    else if ("配置值".equals(stName))
                    {
                        setting.setValue(xpp.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    String etName = xpp.getName();
                    if ("检测结果集".equals(etName))
                    {
                        pmcCheck.getResults().add(result);
                    }
                    else if ("配置集".equals(etName))
                    {
                        pmcCheck.getSettings().add(setting);
                    }
                    break;
            }
            eventType = xpp.next();
        }
        return pmcCheck;
    }

    public static void serializer(PMCCheck pmcCheck) throws IOException
    {
        OutputStream outStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "PMCCheck.sys"));
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(outStream, "UTF-8");
        serializer.startDocument("UTF-8", true);
        serializer.startTag("http://www.w3.org/2001/XMLSchema-instance", "设备检测信息");

        setTextByTag(serializer, "设备唯一标识", pmcCheck.getId());
        setTextByTag(serializer, "设置序列号时间", pmcCheck.getSerialTime());
        setTextByTag(serializer, "序列号", pmcCheck.getSerialNum());
        setTextByTag(serializer, "掌机检测程序版本", pmcCheck.getProVersion());
        setTextByTag(serializer, "硬件版本号", pmcCheck.getHardVersion());

        for (int i = 0; i < pmcCheck.getResults().size(); i++)
        {
            PMCCheck.Result result = pmcCheck.getResults().get(i);
            serializer.startTag(null, "检测结果集");
            setTextByTag(serializer, "检测项唯一标识", result.getItemId());
            setTextByTag(serializer, "检测项名称", result.getItemName());
            setTextByTag(serializer, "检测项编码", result.getItemNum());
            setTextByTag(serializer, "检测项结果", result.getItemResult());
            setTextByTag(serializer, "检测时间", result.getItemTestTime());
            setTextByTag(serializer, "检测成功次数", result.getItemWellCount());
            setTextByTag(serializer, "检测失败次数", result.getItemBadTCount());
            serializer.endTag(null, "检测结果集");
        }

        for (int i = 0; i < pmcCheck.getSettings().size(); i++)
        {
            PMCCheck.Setting setting = pmcCheck.getSettings().get(i);
            serializer.startTag(null, "配置集");
            setTextByTag(serializer, "配置名称", setting.getName());
            setTextByTag(serializer, "配置值", setting.getValue());
            serializer.endTag(null, "配置集");
        }

        serializer.endTag("http://www.w3.org/2001/XMLSchema-instance", "设备检测信息");
        serializer.endDocument();
        serializer.flush();
        outStream.close();
        Log.d("pmc", "写入成功,地址为" + Environment.getExternalStorageDirectory());
    }

    private static void setTextByTag(XmlSerializer myser, String tag, String text) throws IOException
    {
        myser.startTag(null, tag);
        myser.text(text);
        myser.endTag(null, tag);
    }
}
