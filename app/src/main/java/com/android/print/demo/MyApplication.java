package com.android.print.demo;

import android.app.Application;
import com.android.print.sdk.util.Utils;
import java.util.Properties;

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        initPrintSDK();
    }

    private void initPrintSDK() {
        try {
            Properties pro = Utils.getBtConnInfo(this);
            if (pro.isEmpty()) {
                pro.put("mac", "");
            }
        } catch (Exception e) {
            Utils.saveBtConnInfo(this, "");
        }
    }
}
