package com.example.administrator.myapplication.model;

import java.util.List;

/**
 * Created by Administrator on 2016/6/28 0028.
 */

public class PMCCheck
{
    private String id;
    private String serialTime;
    private String serialNum;
    private String proVersion;
    private String hardVersion;
    private List<Result> results;
    private List<Setting> settings;

    public class Result
    {
        @Override
        public String toString()
        {
            return "Result{" +
                    "itemId='" + itemId + '\n' +
                    ", itemName='" + itemName + '\n' +
                    ", itemNum='" + itemNum + '\n' +
                    ", itemResult='" + itemResult + '\n' +
                    ", itemTestTime='" + itemTestTime + '\n' +
                    ", itemWellCount='" + itemWellCount + '\n' +
                    ", itemBadTCount='" + itemBadTCount + '\n' +
                    '}';
        }

        private String itemId;
        private String itemName;
        private String itemNum;
        private String itemResult;
        private String itemTestTime;
        private String itemWellCount;
        private String itemBadTCount;

        public String getItemId()
        {
            return itemId;
        }

        public void setItemId(String itemId)
        {
            this.itemId = itemId;
        }

        public String getItemName()
        {
            return itemName;
        }

        public void setItemName(String itemName)
        {
            this.itemName = itemName;
        }

        public String getItemNum()
        {
            return itemNum;
        }

        public void setItemNum(String itemNum)
        {
            this.itemNum = itemNum;
        }

        public String getItemResult()
        {
            return itemResult;
        }

        public void setItemResult(String itemResult)
        {
            this.itemResult = itemResult;
        }

        public String getItemTestTime()
        {
            return itemTestTime;
        }

        public void setItemTestTime(String itemTestTime)
        {
            this.itemTestTime = itemTestTime;
        }

        public String getItemWellCount()
        {
            return itemWellCount;
        }

        public void setItemWellCount(String itemWellCount)
        {
            this.itemWellCount = itemWellCount;
        }

        public String getItemBadTCount()
        {
            return itemBadTCount;
        }

        public void setItemBadTCount(String itemBadTCount)
        {
            this.itemBadTCount = itemBadTCount;
        }
    }

    public class Setting
    {
        private String name;
        private String value;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSerialTime()
    {
        return serialTime;
    }

    public void setSerialTime(String serialTime)
    {
        this.serialTime = serialTime;
    }

    public String getSerialNum()
    {
        return serialNum;
    }

    public void setSerialNum(String serialNum)
    {
        this.serialNum = serialNum;
    }

    public String getProVersion()
    {
        return proVersion;
    }

    public void setProVersion(String proVersion)
    {
        this.proVersion = proVersion;
    }

    public String getHardVersion()
    {
        return hardVersion;
    }

    public void setHardVersion(String hardVersion)
    {
        this.hardVersion = hardVersion;
    }

    public List<Result> getResults()
    {
        return results;
    }

    public void setResults(List<Result> results)
    {
        this.results = results;
    }

    public List<Setting> getSettings()
    {
        return settings;
    }

    public void setSettings(List<Setting> settings)
    {
        this.settings = settings;
    }

    @Override
    public String toString()
    {
        return "PMCCheck{" +
                "id='" + id + '\n' +
                ", serialTime='" + serialTime + '\n' +
                ", serialNum='" + serialNum + '\n' +
                ", proVersion='" + proVersion + '\n' +
                ", hardVersion='" + hardVersion + '\n' +
                ", results=" + results +
                ", settings=" + settings +
                '}';
    }
}
