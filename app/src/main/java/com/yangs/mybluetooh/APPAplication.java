package com.yangs.mybluetooh;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by yangs on 2017/11/3 0003.
 */

public class APPAplication extends Application {
    private static Context context;
    public static SharedPreferences save;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        save = getSharedPreferences("MainActivity", MODE_PRIVATE);
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
