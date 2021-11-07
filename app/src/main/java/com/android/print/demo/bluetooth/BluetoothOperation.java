package com.android.print.demo.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import com.android.print.demo.IPrinterOpertion;
import com.android.print.sdk.PrinterInstance;
import com.android.print.sdk.bluetooth.BluetoothPort;
import com.android.print.sdk.util.Utils;

public class BluetoothOperation implements IPrinterOpertion {
    public static boolean hasRegDisconnectReceiver;
    private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private IntentFilter filter;
    private Context mContext;
    /* access modifiers changed from: private */
    public BluetoothDevice mDevice;
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    public PrinterInstance mPrinter;
    private String mac;
    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            if (action.equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                if (device != null && BluetoothOperation.this.mPrinter != null && BluetoothOperation.this.mPrinter.isConnected() && device.equals(BluetoothOperation.this.mDevice)) {
                    BluetoothOperation.this.close();
                }
                BluetoothOperation.this.mHandler.obtainMessage(103).sendToTarget();
            }
        }
    };

    public BluetoothOperation(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
        this.filter = new IntentFilter();
        this.filter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        this.mContext.registerReceiver(this.myReceiver, this.filter);
        hasRegDisconnectReceiver = true;
    }

    public void open(Intent data) {
        this.mac = data.getExtras().getString(BluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
        this.mPrinter = new BluetoothPort().btConnnect(this.mContext, this.mac, this.adapter, this.mHandler);
        Utils.saveBtConnInfo(this.mContext, this.mac);
    }

    public void btAutoConn(Context context, Handler mHandler2) {
        this.mPrinter = new BluetoothPort().btAutoConn(context, this.adapter, mHandler2);
        if (this.mPrinter == null) {
            mHandler2.obtainMessage(104).sendToTarget();
        }
    }

    public void close() {
        if (this.mPrinter != null) {
            this.mPrinter.closeConnection();
            this.mPrinter = null;
        }
        if (hasRegDisconnectReceiver) {
            this.mContext.unregisterReceiver(this.myReceiver);
            hasRegDisconnectReceiver = false;
        }
    }

    public PrinterInstance getPrinter() {
        if (this.mPrinter != null && this.mPrinter.isConnected() && !hasRegDisconnectReceiver) {
            this.mContext.registerReceiver(this.myReceiver, this.filter);
            hasRegDisconnectReceiver = true;
        }
        return this.mPrinter;
    }

    public void chooseDevice() {
        if (!this.adapter.isEnabled()) {
            ((Activity) this.mContext).startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 2);
            return;
        }
        ((Activity) this.mContext).startActivityForResult(new Intent(this.mContext, BluetoothDeviceList.class), 1);
    }

    public void usbAutoConn(UsbManager manager) {
    }

    public BroadcastReceiver getMyReceiver() {
        return this.myReceiver;
    }
}
