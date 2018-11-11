package cn.bjhdltcdn.p2plive.service;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import cn.bjhdltcdn.p2plive.app.App;
//import com.baidu.location.BDAbstractLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.location.LocationClientOption.LocationMode;

/**
 * @author baidu
 */
public class LocationService {
//    private LocationClient client = null;
//    private LocationClientOption mOption, DIYoption;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private Object objLock = new Object();

    /***
     *
     * @param locationContext
     */
    public LocationService(Context locationContext) {
        initLocation(locationContext);
//        synchronized (objLock) {
//            if (client == null) {
//                client = new LocationClient(locationContext);
//                client.setLocOption(getDefaultLocationClientOption());
//            }
//        }
    }

//    /***
//     *
//     * @param listener
//     * @return
//     */
//
//    public boolean registerListener(BDAbstractLocationListener listener) {
//        boolean isSuccess = false;
//        if (listener != null) {
//            client.registerLocationListener(listener);
//            isSuccess = true;
//        }
//        return isSuccess;
//    }
//
//    public void unregisterListener(BDAbstractLocationListener listener) {
//        if (listener != null) {
//            client.unRegisterLocationListener(listener);
//        }
//    }

//    /***
//     *
//     * @param option
//     * @return isSuccessSetOption
//     */
//    public boolean setLocationOption(LocationClientOption option) {
//        boolean isSuccess = false;
//        if (option != null) {
//            if (client.isStarted()) {
//                client.stop();
//            }
//            DIYoption = option;
//            client.setLocOption(option);
//        }
//        return isSuccess;
//    }

//    public LocationClientOption getOption() {
//        return DIYoption;
//    }
//
//    /***
//     *
//     * @return DefaultLocationClientOption
//     */
//    private LocationClientOption getDefaultLocationClientOption() {
//        if (mOption == null) {
//            mOption = new LocationClientOption();
//            mOption.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//            mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
//            mOption.setScanSpan(3000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//            mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//            mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
//            mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
//            mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//            mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//            mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//            mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//            mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//
//            mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
//
//        }
//        return mOption;
//    }

    /**
     * 初始化定位
     */
    private void initLocation(Context locationContext) {
        if (locationClient==null) {
            //初始化client
            locationClient = new AMapLocationClient(locationContext);
            locationOption = getDefaultOption();
            //设置定位参数
            locationClient.setLocationOption(locationOption);
        }
    }

    /**
     * 设置定位监听
     *
     * @param locationListener
     */
    public void setLocationListener(AMapLocationListener locationListener) {
        // 设置定位监听
        if (locationListener!=null) {
            initLocation(App.getInstance());
            locationClient.setLocationListener(locationListener);
        }
    }

    /**
     * 解除定位监听
     *
     * @param locationListener
     */
    public void unRegisterLocationListener(AMapLocationListener locationListener) {
        locationClient.unRegisterLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     */
    public void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
//    public void start() {
//        synchronized (objLock) {
//            if (client != null && !client.isStarted()) {
//                client.start();
//            }
//        }
//    }
//
//    public void stop() {
//        synchronized (objLock) {
//            if (client != null && client.isStarted()) {
//                client.stop();
//            }
//        }
//    }
//
//    public boolean requestHotSpotState() {
//
//        return client.requestHotSpotState();
//
//    }

}
