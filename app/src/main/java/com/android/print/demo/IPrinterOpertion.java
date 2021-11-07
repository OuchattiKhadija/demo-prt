package com.android.print.demo;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import com.android.print.sdk.PrinterInstance;

public interface IPrinterOpertion {
    void btAutoConn(Context context, Handler handler);

    void chooseDevice();

    void close();

    PrinterInstance getPrinter();

    void open(Intent intent);

    void usbAutoConn(UsbManager usbManager);
}
