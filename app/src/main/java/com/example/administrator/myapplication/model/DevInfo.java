package com.example.administrator.myapplication.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class DevInfo
{
    private String SN;
    private String DeviceID;
    private String HardVersion;
    private List<Functions> functionsList;

    public class Functions
    {
        private String Code;
        private String Value;

        public String getCode()
        {
            return Code;
        }

        public void setCode(String code)
        {
            Code = code;
        }

        public String getValue()
        {
            return Value;
        }

        public void setValue(String value)
        {
            Value = value;
        }
    }

    public List<Functions> getFunctionsList()
    {
        return functionsList;
    }

    public void setFunctionsList(List<Functions> functionsList)
    {
        this.functionsList = functionsList;
    }

    public String getSN()
    {
        return SN;
    }

    public void setSN(String SN)
    {
        this.SN = SN;
    }

    public String getDeviceID()
    {
        return DeviceID;
    }

    public void setDeviceID(String deviceID)
    {
        DeviceID = deviceID;
    }

    public String getHardVersion()
    {
        return HardVersion;
    }

    public void setHardVersion(String hardVersion)
    {
        HardVersion = hardVersion;
    }

    @Override
    public String toString()
    {
        return "DevInfo{" +
                "SN='" + SN + '\'' +
                ", DeviceID='" + DeviceID + '\'' +
                ", HardVersion='" + HardVersion + '\'' +
                '}';
    }
}
