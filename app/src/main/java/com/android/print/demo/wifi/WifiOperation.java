package com.android.print.demo.wifi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import com.android.print.demo.IPrinterOpertion;
import com.android.print.sdk.PrinterInstance;
import java.util.Timer;

public class WifiOperation implements IPrinterOpertion {
    protected static WifiManager wifiManager;
    private int errorNumber;
    private Context mContext;
    private Handler mHandler;
    private PrinterInstance mPrinter;
    private Timer timer = new Timer();

    public WifiOperation(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
        wifiManager = (WifiManager) this.mContext.getSystemService("wifi");
    }

    public void close() {
        if (this.mPrinter != null) {
            this.mPrinter.closeConnection();
            this.mPrinter = null;
        }
    }

    public PrinterInstance getPrinter() {
        return this.mPrinter;
    }

    public void open(Intent data) {
        String ipAddress = data.getStringExtra("ip_address");
        Log.v("ipaddress", ipAddress);
        this.mPrinter = new PrinterInstance(ipAddress, 9100, this.mHandler);
        this.mPrinter.openConnection();
    }

    public void chooseDevice() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        Intent intent = new Intent();
        intent.setClass(this.mContext, WifiDeviceList.class);
        ((Activity) this.mContext).startActivityForResult(intent, 1);
    }

    public void usbAutoConn(UsbManager manager) {
    }

    public void btAutoConn(Context context, Handler mHandler2) {
    }
}
