package com.yangs.mybluetooh;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.List;

/**
 * Created by yangs on 2017/11/3 0003.
 */

public class APPAplication extends Application {
    private static Context context;
    public static SharedPreferences save;
    private static Boolean DEBUG;
    public static AMapLocationClient mLocationClient = null;
    public static AMapLocationClientOption mLocationOption = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        save = getSharedPreferences("MainActivity", MODE_PRIVATE);
        DEBUG = true;
        if (!DEBUG) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                String phone = APPAplication.save.getString("phone", "");
                String texts = APPAplication.save.getString("text", "");
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        texts = texts + "   【" + aMapLocation.getAddress() + "】";
                    } else {
                        APPAplication.showToast("获取位置信息失败,将不附带位置！", 1);
                    }
                } else {
                    APPAplication.showToast("获取位置信息失败,将不附带位置！", 1);
                }
                android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                List<String> divideContents = smsManager.divideMessage(texts);
                for (String text : divideContents) {
                    smsManager.sendTextMessage(phone, null,
                            text, null, null);
                }
            }
        });
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setMockEnable(true);
        mLocationOption.setLocationCacheEnable(false);
        mLocationClient.setLocationOption(mLocationOption);
    }

    public static void showToast(final String src, final int i) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, src, i).show();
            }
        });
    }
}
