package com.example.administrator.myapplication.model;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class DevInfo
{
    private String SN;
    private String DeviceID;
    private String HardVersion;

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
