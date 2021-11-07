package com.android.print.sdk.wifi;

import android.os.Handler;
import android.util.Log;
import com.android.print.sdk.IPrinterPort;
import com.android.print.sdk.util.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

public class WiFiPort implements IPrinterPort {
    private static String TAG = "WifiPrinter";
    /* access modifiers changed from: private */
    public String address;
    /* access modifiers changed from: private */
    public InputStream inputStream;
    /* access modifiers changed from: private */
    public ConnectThread mConnectThread;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public Socket mSocket;
    private int mState = 103;
    /* access modifiers changed from: private */
    public OutputStream outputStream;
    /* access modifiers changed from: private */
    public int port;
    private int readLen;

    public WiFiPort(String ipAddress, int portNumber, Handler handler) {
        this.address = ipAddress;
        this.port = portNumber;
        this.mHandler = handler;
    }

    public void open() {
        Utils.Log(TAG, "open connect to: " + this.address);
        if (this.mState != 103) {
            close();
        }
        Log.v("wifitest", "enter");
        this.mConnectThread = new ConnectThread(this, (ConnectThread) null);
        this.mConnectThread.start();
    }

    public void close() {
        try {
            if (this.outputStream != null) {
                this.outputStream.close();
            }
            if (this.inputStream != null) {
                this.inputStream.close();
            }
            if (this.mSocket != null) {
                this.mSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.outputStream = null;
        this.inputStream = null;
        this.mSocket = null;
        this.mConnectThread = null;
        if (this.mState != 102) {
            setState(103);
        }
    }

    public int write(byte[] data) {
        try {
            if (this.outputStream == null) {
                return -1;
            }
            this.outputStream.write(data);
            this.outputStream.flush();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public byte[] read() {
        byte[] readBuff = null;
        int firstByte = 0;
        try {
            if (this.inputStream == null || (firstByte = this.inputStream.read()) != -1) {
                if (this.inputStream != null) {
                    int available = this.inputStream.available();
                    this.readLen = available;
                    if (available > 0) {
                        readBuff = new byte[this.readLen];
                        this.inputStream.read(readBuff);
                    }
                }
                if (firstByte > 0) {
                    if (readBuff == null) {
                        return new byte[]{(byte) firstByte};
                    }
                    byte[] by = new byte[(readBuff.length + 1)];
                    by[0] = (byte) firstByte;
                    System.arraycopy(readBuff, 0, by, 1, readBuff.length);
                    readBuff = by;
                }
                Log.w(TAG, "read length:" + this.readLen);
                return readBuff;
            }
            setState(103);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int read1() {
        try {
            return this.inputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
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

    public Boolean isServerClose() {
        try {
            this.mSocket.sendUrgentData(255);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private class ConnectThread extends Thread {
        /* synthetic */ ConnectThread(WiFiPort wiFiPort, ConnectThread connectThread) {
            this();
        }

        private ConnectThread() {
        }

        public void run() {
            boolean hasError = true;
            SocketAddress mSocketAddress = new InetSocketAddress(WiFiPort.this.address, WiFiPort.this.port);
            try {
                WiFiPort.this.mSocket = new Socket();
                WiFiPort.this.mSocket.setSoTimeout(5000);
                WiFiPort.this.mSocket.connect(mSocketAddress, 5000);
                WiFiPort.this.outputStream = WiFiPort.this.mSocket.getOutputStream();
                WiFiPort.this.inputStream = WiFiPort.this.mSocket.getInputStream();
                hasError = false;
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            synchronized (this) {
                WiFiPort.this.mConnectThread = null;
            }
            if (hasError) {
                WiFiPort.this.setState(102);
                WiFiPort.this.close();
                Log.v("wifitest", "failed");
                return;
            }
            WiFiPort.this.setState(101);
            Log.v("wifitest", "success");
        }
    }
}
