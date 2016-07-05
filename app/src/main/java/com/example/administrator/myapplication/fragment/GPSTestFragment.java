package com.example.administrator.myapplication.fragment;

import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.utils.Constants;
import com.example.administrator.myapplication.utils.MethodCollection;

/**
 * Created by Administrator on 2016/6/16 0016.
 */
public class GPSTestFragment extends BaseFragment
{
    private TextView gpsState;

    private BDLocationListener listener = new BDLocationListener()
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            StringBuffer sb = new StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
            sb.append("\n纬度为 : ");
            sb.append(location.getLatitude());
            sb.append("\n经度为 : ");
            sb.append(location.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                sb.append("\n时速 : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("km/h");
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
                sb.append("\n海拔 : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("m");
                sb.append("\n方向 : ");
                sb.append(location.getDirection());// 单位度
                sb.append("度");
                sb.append("\n地址 : ");
                sb.append(location.getAddrStr());
                sb.append("\n定位模式 : ");
                sb.append("GPS定位");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                sb.append("\n地址为 : ");
                sb.append(location.getAddrStr());
                //运营商信息
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
                sb.append("\n定位模式 : ");
                sb.append("网络定位");
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                sb.append("\n定位模式 : ");
//                sb.append("离线定位成功");
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                sb.append("\n定位模式 : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                sb.append("\n定位模式 : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                sb.append("\n定位模式 : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//            }
//            sb.append("\nlocationdescribe : ");
//            sb.append(location.getLocationDescribe());// 位置语义化信息
//            List<Poi> list = location.getPoiList();// POI数据
//            if (list != null) {
//                sb.append("\npoilist size = : ");
//                sb.append(list.size());
//                for (Poi p : list) {
//                    sb.append("\npoi= : ");
//                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//                }
            }
            else
            {
                gpsState.setText("GPS已开启，但无法定位，请到空旷地方测试。");
                getActivity().setResult(Constants.RESULT_BAD);
                return;
            }
            String msg = sb.toString();
            gpsState.setText(msg);
            getActivity().setResult(Constants.RESULT_WELL);
        }
    };
    private LocationClient client;

    @Override
    public int getLayout()
    {
        return R.layout.fragment_gps_test;
    }

    @Override
    protected void initViews()
    {
        gpsState = (TextView) rootView.findViewById(R.id.gps_state);
    }

    @Override
    protected void initEvents()
    {

    }

    @Override
    protected void initData()
    {
        if (MethodCollection.isGPSAvailable(getActivity()))
        {
            getLocation();
        }
        else
        {
            gpsState.setText("GPS不可用");
        }
        MethodCollection.delayFinish(getActivity(), 3000);
    }

    private void getLocation()
    {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要

        client = MethodCollection.getLocation(getActivity().getApplicationContext(), listener, option);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        if (client != null)
        {
            client.stop();
        }
    }
}
