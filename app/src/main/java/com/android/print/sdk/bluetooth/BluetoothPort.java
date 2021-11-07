package com.android.print.sdk.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import com.android.print.sdk.IPrinterPort;
import com.android.print.sdk.PrinterInstance;
import com.android.print.sdk.util.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class BluetoothPort implements IPrinterPort {
    private static final String TAG = "BluetoothPort";
    /* access modifiers changed from: private */
    public static InputStream inputStream;
    private static String mDeviceAddress;
    private static String mDeviceName;
    /* access modifiers changed from: private */
    public static BluetoothSocket mSocket;
    /* access modifiers changed from: private */
    public static OutputStream outputStream;
    private static int readLen;
    /* access modifiers changed from: private */
    public final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    /* access modifiers changed from: private */
    public BroadcastReceiver boundDeviceReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(intent.getAction())) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (BluetoothPort.this.mDevice.equals(device)) {
                    switch (device.getBondState()) {
                        case 10:
                            BluetoothPort.this.mContext.unregisterReceiver(BluetoothPort.this.boundDeviceReceiver);
                            BluetoothPort.this.setState(102);
                            Utils.Log(BluetoothPort.TAG, "bound cancel");
                            return;
                        case 11:
                            Utils.Log(BluetoothPort.TAG, "bounding......");
                            return;
                        case 12:
                            Utils.Log(BluetoothPort.TAG, "bound success");
                            BluetoothPort.this.mContext.unregisterReceiver(BluetoothPort.this.boundDeviceReceiver);
                            BluetoothPort.this.PairOrConnect(false);
                            return;
                        default:
                            return;
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public BluetoothAdapter mAdapter;
    /* access modifiers changed from: private */
    public ConnectThread mConnectThread;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public BluetoothDevice mDevice;
    private Handler mHandler;
    private PrinterInstance mPrinter;
    private int mState;

    public BluetoothPort(Context context, BluetoothDevice device, Handler handler) {
        this.mHandler = handler;
        this.mDevice = device;
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mState = 103;
        this.mContext = context;
    }

    public BluetoothPort() {
    }

    public BluetoothPort(Context context, String address, Handler handler) {
        this.mHandler = handler;
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mDevice = this.mAdapter.getRemoteDevice(address);
        this.mState = 103;
        this.mContext = context;
    }

    public void open() {
        Utils.Log(TAG, "connect to: " + this.mDevice.getName());
        if (this.mState != 103) {
            close();
        }
        if (this.mDevice.getBondState() == 10) {
            Log.i(TAG, "device.getBondState() is BluetoothDevice.BOND_NONE");
            PairOrConnect(true);
        } else if (this.mDevice.getBondState() == 12) {
            PairOrConnect(false);
        }
    }

    /* access modifiers changed from: private */
    public void PairOrConnect(boolean pair) {
        if (pair) {
            this.mContext.registerReceiver(this.boundDeviceReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
            boolean success = false;
            try {
                success = ((Boolean) BluetoothDevice.class.getMethod("createBond", new Class[0]).invoke(this.mDevice, new Object[0])).booleanValue();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            } catch (NoSuchMethodException e4) {
                e4.printStackTrace();
            }
            Log.i(TAG, "createBond is success? : " + success);
            return;
        }
        this.mConnectThread = new ConnectThread(this, (ConnectThread) null);
        this.mConnectThread.start();
    }

    /* access modifiers changed from: private */
    @TargetApi(10)
    public boolean ReTryConnect() {
        Utils.Log(TAG, "android SDK version is:" + Build.VERSION.SDK_INT);
        try {
            if (Build.VERSION.SDK_INT >= 10) {
                mSocket = this.mDevice.createInsecureRfcommSocketToServiceRecord(this.PRINTER_UUID);
            } else {
                mSocket = (BluetoothSocket) this.mDevice.getClass().getMethod("createRfcommSocket", new Class[]{Integer.TYPE}).invoke(this.mDevice, new Object[]{1});
            }
            mSocket.connect();
            return false;
        } catch (Exception e) {
            Utils.Log(TAG, "connect failed:");
            e.printStackTrace();
            return true;
        }
    }

    public void close() {
        Utils.Log(TAG, "close()");
        try {
            if (mSocket != null) {
                mSocket.close();
            }
        } catch (IOException e) {
            Utils.Log(TAG, "close socket failed");
            e.printStackTrace();
        }
        this.mConnectThread = null;
        this.mDevice = null;
        mSocket = null;
        if (this.mState != 102) {
            setState(103);
        }
    }

    public int write(byte[] data) {
        try {
            if (outputStream == null) {
                return -1;
            }
            outputStream.write(data);
            outputStream.flush();
            return 0;
        } catch (IOException e) {
            Utils.Log(TAG, "write error.");
            e.printStackTrace();
            return -1;
        }
    }

    public byte[] read() {
        byte[] readBuff = null;
        try {
            if (inputStream != null) {
                int available = inputStream.available();
                readLen = available;
                if (available > 0) {
                    readBuff = new byte[readLen];
                    inputStream.read(readBuff);
                }
            }
        } catch (IOException e) {
            Utils.Log(TAG, "read error");
            e.printStackTrace();
        }
        Log.w(TAG, "read length:" + readLen);
        return readBuff;
    }

    public static synchronized byte[] read(int timeout) {
        byte[] receiveBytes;
        synchronized (BluetoothPort.class) {
            receiveBytes = null;
            do {
                try {
                    Thread.sleep(50);
                    try {
                        int available = inputStream.available();
                        readLen = available;
                        if (available > 0 || timeout - 50 <= 0) {
                        }
                        int available2 = inputStream.available();
                        readLen = available2;
                        break;
                    } catch (IOException e) {
                        Utils.Log(TAG, "read error1");
                        e.printStackTrace();
                    }
                    Thread.sleep(50);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } while (timeout - 50 <= 0);
            if (readLen > 0) {
                receiveBytes = new byte[readLen];
                inputStream.read(receiveBytes);
            }
        }
        return receiveBytes;
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

    public PrinterInstance btAutoConn(Context context, BluetoothAdapter adapter, Handler mHandler2) {
        mDeviceAddress = Utils.getBtConnInfo(context).getProperty("mac");
        if (mDeviceAddress == null || mDeviceAddress.equals("")) {
            return null;
        }
        Log.v("mac", mDeviceAddress);
        this.mDevice = adapter.getRemoteDevice(mDeviceAddress);
        mDeviceName = this.mDevice.getName();
        this.mPrinter = new PrinterInstance(context, this.mDevice, mHandler2);
        this.mPrinter.openConnection();
        Log.v("btport", "open-success!");
        return this.mPrinter;
    }

    public PrinterInstance btConnnect(Context context, String mac, BluetoothAdapter adapter, Handler mHandler2) {
        this.mDevice = adapter.getRemoteDevice(mac);
        this.mPrinter = new PrinterInstance(context, this.mDevice, mHandler2);
        this.mPrinter.openConnection();
        Log.v("btport", "open-success!");
        return this.mPrinter;
    }

    public static byte getData(int time) {
        try {
            InputStream inputStream2 = mSocket.getInputStream();
            for (int j = 0; j < 5; j++) {
                if (inputStream2.available() != 0) {
                    byte[] b = new byte[inputStream2.available()];
                    int i = inputStream2.read(b);
                    if (j != 0) {
                        return b[i - 1];
                    }
                }
                Thread.sleep((long) time);
                write1(new byte[]{16, 4, 2});
            }
            return 0;
        } catch (Exception e) {
            Log.e("TAG", e.toString());
            return 0;
        }
    }

    public static byte getEndData(int time) {
        try {
            InputStream inputStream2 = mSocket.getInputStream();
            for (int j = 0; j < 5; j++) {
                if (inputStream2.available() != 0) {
                    byte[] b = new byte[inputStream2.available()];
                    int i = inputStream2.read(b);
                    if (j != 0) {
                        return b[i - 1];
                    }
                }
                Thread.sleep((long) time);
                write1(new byte[]{27, 118});
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int write1(byte[] data) {
        try {
            if (outputStream == null) {
                return -1;
            }
            outputStream.write(data);
            outputStream.flush();
            return 0;
        } catch (IOException e) {
            Utils.Log(TAG, "write error.");
            e.printStackTrace();
            return -1;
        }
    }

    public static String getmDeviceName() {
        return mDeviceName;
    }

    public static void setmDeviceName(String mDeviceName2) {
    }

    public static String getmDeviceAddress() {
        return mDeviceAddress;
    }

    public static void setmDeviceAddress(String mDeviceAddress2) {
    }

    private class ConnectThread extends Thread {
        /* synthetic */ ConnectThread(BluetoothPort bluetoothPort, ConnectThread connectThread) {
            this();
        }

        private ConnectThread() {
        }

        public void run() {
            boolean hasError = false;
            BluetoothPort.this.mAdapter.cancelDiscovery();
            try {
                BluetoothPort.mSocket = BluetoothPort.this.mDevice.createRfcommSocketToServiceRecord(BluetoothPort.this.PRINTER_UUID);
                BluetoothPort.mSocket.connect();
            } catch (IOException e) {
                Utils.Log(BluetoothPort.TAG, "ConnectThread failed. retry.");
                e.printStackTrace();
                hasError = BluetoothPort.this.ReTryConnect();
            }
            synchronized (this) {
                BluetoothPort.this.mConnectThread = null;
            }
            if (!hasError) {
                try {
                    BluetoothPort.inputStream = BluetoothPort.mSocket.getInputStream();
                    BluetoothPort.outputStream = BluetoothPort.mSocket.getOutputStream();
                } catch (IOException e2) {
                    hasError = true;
                    Utils.Log(BluetoothPort.TAG, "Get Stream failed");
                    e2.printStackTrace();
                }
            }
            if (hasError) {
                BluetoothPort.this.setState(102);
                BluetoothPort.this.close();
                return;
            }
            BluetoothPort.this.setState(101);
        }
    }
}
