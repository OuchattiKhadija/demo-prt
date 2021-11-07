package com.android.print.sdk.usb;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.support.graphics.drawable.PathInterpolatorCompat;
import android.util.Log;
import com.android.print.sdk.IPrinterPort;
import com.android.print.sdk.util.Utils;
import java.io.ByteArrayInputStream;

@TargetApi(12)
public class USBPort implements IPrinterPort {
    private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";
    private static final String TAG = "USBPrinter";
    /* access modifiers changed from: private */
    public UsbDeviceConnection connection;
    /* access modifiers changed from: private */
    public UsbEndpoint inEndpoint;
    private boolean isOldUSB;
    /* access modifiers changed from: private */
    public ConnectThread mConnectThread;
    /* access modifiers changed from: private */
    public Context mContext;
    private Handler mHandler;
    private int mState;
    /* access modifiers changed from: private */
    public UsbDevice mUsbDevice;
    /* access modifiers changed from: private */
    public UsbManager mUsbManager;
    /* access modifiers changed from: private */
    public final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.w(USBPort.TAG, "receiver action: " + action);
            if (USBPort.ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    USBPort.this.mContext.unregisterReceiver(USBPort.this.mUsbReceiver);
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra("device");
                    if (!intent.getBooleanExtra("permission", false) || !USBPort.this.mUsbDevice.equals(device)) {
                        USBPort.this.setState(102);
                        Log.e(USBPort.TAG, "permission denied for device " + device);
                    } else {
                        USBPort.this.connect();
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public UsbEndpoint outEndpoint;
    /* access modifiers changed from: private */
    public UsbInterface usbInterface;

    public USBPort(Context context, UsbDevice usbDevice, Handler handler) {
        this.mContext = context;
        this.mUsbManager = (UsbManager) this.mContext.getSystemService("usb");
        this.mUsbDevice = usbDevice;
        this.mHandler = handler;
        this.mState = 103;
    }

    public void open() {
        Log.d(TAG, "connect to: " + this.mUsbDevice.getDeviceName());
        if (this.mState != 103) {
            close();
        }
        if (!isUsbPrinter(this.mUsbDevice)) {
            setState(102);
        } else if (this.mUsbManager.hasPermission(this.mUsbDevice)) {
            connect();
        } else {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
            this.mContext.registerReceiver(this.mUsbReceiver, new IntentFilter(ACTION_USB_PERMISSION));
            this.mUsbManager.requestPermission(this.mUsbDevice, pendingIntent);
        }
    }

    /* access modifiers changed from: private */
    public void connect() {
        this.mConnectThread = new ConnectThread(this, (ConnectThread) null);
        this.mConnectThread.start();
    }

    public void close() {
        Utils.Log(TAG, "close()");
        if (this.connection != null) {
            this.connection.releaseInterface(this.usbInterface);
            this.connection.close();
            this.connection = null;
        }
        this.mConnectThread = null;
        if (this.mState != 102) {
            setState(103);
        }
    }

    public int write(byte[] data) {
        if (this.connection == null) {
            return -1;
        }
        if (data.length < 64) {
            return this.connection.bulkTransfer(this.outEndpoint, data, data.length, PathInterpolatorCompat.MAX_NUM_POINTS);
        }
        try {
            byte[] buff = new byte[64];
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            int sentLength = 0;
            while (true) {
                int length = bis.read(buff);
                if (length == -1) {
                    bis.close();
                    return sentLength;
                }
                byte[] realData = new byte[length];
                System.arraycopy(buff, 0, realData, 0, length);
                sentLength += this.connection.bulkTransfer(this.outEndpoint, realData, realData.length, PathInterpolatorCompat.MAX_NUM_POINTS);
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public byte[] read() {
        if (this.connection != null) {
            byte[] retData = new byte[64];
            int readLen = this.connection.bulkTransfer(this.inEndpoint, retData, retData.length, PathInterpolatorCompat.MAX_NUM_POINTS);
            Log.w(TAG, "read length:" + readLen);
            if (readLen > 0) {
                if (readLen == 64) {
                    return retData;
                }
                byte[] realData = new byte[readLen];
                System.arraycopy(retData, 0, realData, 0, readLen);
                return realData;
            }
        }
        return null;
    }

    public boolean isOldUSB() {
        return this.isOldUSB;
    }

    public static boolean isUsbPrinter(UsbDevice device) {
        int vendorId = device.getVendorId();
        int productId = device.getProductId();
        Utils.Log(TAG, "device name: " + device.getDeviceName());
        Utils.Log(TAG, "vid:" + vendorId + " pid:" + productId);
        return true;
    }

    /* access modifiers changed from: private */
    public synchronized void setState(int state) {
        Utils.Log(TAG, "setState() " + this.mState + " -> " + state);
        if (this.mState != state) {
            this.mState = state;
            if (this.mHandler != null) {
                this.mHandler.obtainMessage(this.mState).sendToTarget();
            }
        }
    }

    public int getState() {
        return this.mState;
    }

    private class ConnectThread extends Thread {
        /* synthetic */ ConnectThread(USBPort uSBPort, ConnectThread connectThread) {
            this();
        }

        private ConnectThread() {
        }

        public void run() {
            boolean hasError = true;
            if (USBPort.this.mUsbManager.hasPermission(USBPort.this.mUsbDevice)) {
                try {
                    USBPort.this.usbInterface = USBPort.this.mUsbDevice.getInterface(0);
                    for (int i = 0; i < USBPort.this.usbInterface.getEndpointCount(); i++) {
                        UsbEndpoint ep = USBPort.this.usbInterface.getEndpoint(i);
                        if (ep.getType() == 2) {
                            if (ep.getDirection() == 0) {
                                USBPort.this.outEndpoint = ep;
                            } else {
                                USBPort.this.inEndpoint = ep;
                            }
                        }
                    }
                    USBPort.this.connection = USBPort.this.mUsbManager.openDevice(USBPort.this.mUsbDevice);
                    if (USBPort.this.connection != null && USBPort.this.connection.claimInterface(USBPort.this.usbInterface, true)) {
                        hasError = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            synchronized (this) {
                USBPort.this.mConnectThread = null;
            }
            if (hasError) {
                USBPort.this.setState(102);
                USBPort.this.close();
                return;
            }
            USBPort.this.setState(101);
        }
    }
}
