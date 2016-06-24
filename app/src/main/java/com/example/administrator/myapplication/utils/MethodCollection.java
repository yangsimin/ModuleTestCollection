package com.example.administrator.myapplication.utils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/16 0016.
 */
public class MethodCollection
{
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //获取当前时间
    public static String getCurTime()
    {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    //震动一次
    public static void vibrateOnce(Activity activity)
    {
        Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(700);
    }

    //求外置sd卡路径
    public static String getSDcardPath()
    {
//        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

//        return Environment.getExternalStorageDirectory().getPath();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            String mount = new String();
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure")) continue;
                if (line.contains("asec")) continue;

                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        mount = mount.concat("*" + columns[1] + "\n");
                    }
                } else if (line.contains("fuse")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        mount = mount.concat(columns[1] + "\n");
                    }
                }
            }
            Log.d("sdcard", "sdcard路径为：" + mount);
            return mount;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //判断是否可以写
    public static boolean checkSDcardWritable(String dir)
    {
        if (dir == null) return false;
        File directory = new File(dir);
        if (!directory.isDirectory())
        {
            if (!directory.mkdirs()) return false;
        }

        File f = new File(directory, ".test");
        try
        {
            if (f.exists())
            {
                f.delete();
            }
            if (!f.createNewFile()) return false;
            f.delete();
            return true;

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

//    public static void verifyStoragePermissions(Activity activity) {
//        // Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
//    }

    public static boolean writeToSD(String msg, Context context)
    {
        File file = new File("/storage/sdcard1/", "test.txt");
//        File file = new File(Environment.getExternalStorageDirectory().getPath(), "test.txt");
//        File file = new File("/mnt/sdcard2/", "text.txt");
        FileOutputStream fos;

        try
        {
            fos = new FileOutputStream(file);
            fos.write(msg.getBytes());
            fos.close();
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    //蓝牙开关
    public static void toggleOfBlueTooth(boolean state)
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (state)
        {
            bluetoothAdapter.enable();
        }
        else
        {
            bluetoothAdapter.disable();
        }
    }

    //获取蓝牙状态
    public static boolean isBlueToothAvailable()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    //判断GPRS是否可用
    public static boolean isGPRSAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        if (networkInfo != null && networkInfo.length > 0)
        {
            for (int i = 0; i < networkInfo.length; i++)
            {
                if (networkInfo[i].getType() == ConnectivityManager.TYPE_MOBILE)
                {
                    return networkInfo[i].isConnected();
                }
            }
        }
        return false;
    }

    //判断GPS是否可用
    public static boolean isGPSAvailable(Context context)
    {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static LocationClient getLocation(Context context, BDLocationListener listener, LocationClientOption option)
    {
        LocationClient client = new LocationClient(context, option);
        client.registerLocationListener(listener);
        client.start();
        return client;
    }

    public static void delayFinish(final Activity activity, long time)
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                activity.finish();
            }
        }, time);
    }
}
