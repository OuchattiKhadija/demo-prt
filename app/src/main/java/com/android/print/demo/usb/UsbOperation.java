package com.android.print.demo.usb;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.android.print.demo.IPrinterOpertion;
import com.android.print.sdk.PrinterInstance;
import com.android.print.sdk.usb.USBPort;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@TargetApi(12)
public class UsbOperation implements IPrinterOpertion {
    private static final String TAG = "UsbOpertion";
    private List<UsbDevice> deviceList;
    private IntentFilter filter;
    private boolean hasRegDisconnectReceiver;
    private Context mContext;
    /* access modifiers changed from: private */
    public UsbDevice mDevice;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public PrinterInstance mPrinter;
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            UsbDevice device;
            String action = intent.getAction();
            Log.i(UsbOperation.TAG, "receiver is: " + action);
            if (!"android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action) && "android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action) && (device = (UsbDevice) intent.getParcelableExtra("device")) != null && UsbOperation.this.mPrinter != null && UsbOperation.this.mPrinter.isConnected() && device.equals(UsbOperation.this.mDevice)) {
                UsbOperation.this.close();
            }
        }
    };

    public UsbOperation(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
        this.hasRegDisconnectReceiver = false;
        this.filter = new IntentFilter();
        this.filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        this.filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
    }

    public void open(Intent data) {
        this.mDevice = (UsbDevice) data.getParcelableExtra("device");
        this.mPrinter = new PrinterInstance(this.mContext, this.mDevice, this.mHandler);
        this.mPrinter.openConnection();
    }

    public void close() {
        if (this.mPrinter != null) {
            this.mPrinter.closeConnection();
            this.mPrinter = null;
        }
        if (this.hasRegDisconnectReceiver) {
            this.mContext.unregisterReceiver(this.myReceiver);
            this.hasRegDisconnectReceiver = false;
        }
    }

    public PrinterInstance getPrinter() {
        if (this.mPrinter != null && this.mPrinter.isConnected() && !this.hasRegDisconnectReceiver) {
            this.mContext.registerReceiver(this.myReceiver, this.filter);
            this.hasRegDisconnectReceiver = true;
        }
        return this.mPrinter;
    }

    public void chooseDevice() {
        ((Activity) this.mContext).startActivityForResult(new Intent(this.mContext, UsbDeviceList.class), 1);
    }

    public void usbAutoConn(UsbManager manager) {
        doDiscovery(manager);
        if (!this.deviceList.isEmpty()) {
            this.mDevice = this.deviceList.get(0);
        }
        if (this.mDevice != null) {
            this.mPrinter = new PrinterInstance(this.mContext, this.mDevice, this.mHandler);
            this.mPrinter.openConnection();
            return;
        }
        Toast.makeText(this.mContext, "opened failed!", 0).show();
    }

    private void doDiscovery(UsbManager manager) {
        HashMap<String, UsbDevice> devices = manager.getDeviceList();
        this.deviceList = new ArrayList();
        for (UsbDevice device : devices.values()) {
            if (USBPort.isUsbPrinter(device)) {
                this.deviceList.add(device);
            }
        }
    }

    public void btAutoConn(Context context, Handler mHandler2) {
    }
}
