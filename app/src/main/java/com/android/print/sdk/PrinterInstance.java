package com.android.print.sdk;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.support.p000v4.view.InputDeviceCompat;
import android.support.p003v7.widget.helper.ItemTouchHelper;
import com.android.print.demo.BuildConfig;
import com.android.print.sdk.PrinterConstants;
import com.android.print.sdk.bluetooth.BluetoothPort;
import com.android.print.sdk.usb.USBPort;
import com.android.print.sdk.util.Command;
import com.android.print.sdk.util.Utils;
import com.android.print.sdk.wifi.WiFiPort;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PrinterInstance implements Serializable {
    public static boolean DEBUG = true;
    private static String TAG = "PrinterInstance";
    private static final long serialVersionUID = 1;
    private final String SDK_VERSION = BuildConfig.VERSION_NAME;
    private String charsetName = "gbk";
    private IPrinterPort myPrinter;
    private int sendSleep = ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;

    public PrinterInstance(Context context, BluetoothDevice bluetoothDevice, Handler handler) {
        this.myPrinter = new BluetoothPort(context, bluetoothDevice, handler);
    }

    public PrinterInstance(Context context, UsbDevice usbDevice, Handler handler) {
        this.myPrinter = new USBPort(context, usbDevice, handler);
        this.sendSleep = 10;
    }

    public PrinterInstance(String ipAddress, int portNumber, Handler handler) {
        this.myPrinter = new WiFiPort(ipAddress, portNumber, handler);
    }

    public String getEncoding() {
        return this.charsetName;
    }

    public void setEncoding(String charsetName2) {
        this.charsetName = charsetName2;
    }

    public String getSDK_Vesion() {
        return BuildConfig.VERSION_NAME;
    }

    public boolean isConnected() {
        return this.myPrinter.getState() == 101;
    }

    public void openConnection() {
        this.myPrinter.open();
    }

    public void closeConnection() {
        this.myPrinter.close();
    }

    public int printText(String content) {
        byte[] data = null;
        try {
            if (this.charsetName != "") {
                data = content.getBytes(this.charsetName);
            } else {
                data = content.getBytes();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sendByteData(data);
    }

    public int printTextinRussian(String content) {
        Map map = new HashMap();
        map.put(1040, Byte.MIN_VALUE);
        map.put(1041, (byte) -127);
        map.put(1042, (byte) -126);
        map.put(1043, (byte) -125);
        map.put(1044, (byte) -124);
        map.put(1045, (byte) -123);
        map.put(1046, (byte) -122);
        map.put(1047, (byte) -121);
        map.put(1048, (byte) -120);
        map.put(1049, (byte) -119);
        map.put(1050, (byte) -118);
        map.put(1051, (byte) -117);
        map.put(1052, (byte) -116);
        map.put(1053, (byte) -115);
        map.put(1054, (byte) -114);
        map.put(1055, (byte) -113);
        map.put(1056, (byte) -112);
        map.put(1057, (byte) -111);
        map.put(1058, (byte) -110);
        map.put(1059, (byte) -109);
        map.put(1060, (byte) -108);
        map.put(1061, (byte) -107);
        map.put(1062, (byte) -106);
        map.put(1063, (byte) -105);
        map.put(1064, (byte) -104);
        map.put(1065, (byte) -103);
        map.put(1066, (byte) -102);
        map.put(1067, (byte) -101);
        map.put(1068, (byte) -100);
        map.put(1069, (byte) -99);
        map.put(1070, (byte) -98);
        map.put(1071, (byte) -97);
        map.put(1072, (byte) -96);
        map.put(1073, (byte) -95);
        map.put(1074, (byte) -94);
        map.put(1075, (byte) -93);
        map.put(1076, (byte) -92);
        map.put(1077, (byte) -91);
        map.put(1078, (byte) -90);
        map.put(1079, (byte) -89);
        map.put(1080, (byte) -88);
        map.put(1081, (byte) -87);
        map.put(1082, (byte) -86);
        map.put(1083, (byte) -85);
        map.put(1084, (byte) -84);
        map.put(1085, (byte) -83);
        map.put(1086, (byte) -82);
        map.put(1087, (byte) -81);
        map.put(9617, (byte) -80);
        map.put(9618, (byte) -79);
        map.put(9619, (byte) -78);
        map.put(9474, (byte) -77);
        map.put(9508, (byte) -76);
        map.put(9569, (byte) -75);
        map.put(9670, (byte) -74);
        map.put(9558, (byte) -73);
        map.put(9557, (byte) -72);
        map.put(9571, (byte) -71);
        map.put(9553, (byte) -70);
        map.put(9559, (byte) -69);
        map.put(9565, (byte) -68);
        map.put(9564, (byte) -67);
        map.put(9553, (byte) -66);
        map.put(9488, (byte) -65);
        map.put(9492, (byte) -64);
        map.put(9524, (byte) -63);
        map.put(9516, (byte) -62);
        map.put(9500, (byte) -61);
        map.put(9472, (byte) -60);
        map.put(9532, (byte) -59);
        map.put(9566, (byte) -58);
        map.put(9557, (byte) -57);
        map.put(9562, (byte) -56);
        map.put(9556, (byte) -55);
        map.put(9577, (byte) -54);
        map.put(9574, (byte) -53);
        map.put(9568, (byte) -52);
        map.put(9552, (byte) -51);
        map.put(9580, (byte) -50);
        map.put(9575, (byte) -49);
        map.put(9576, (byte) -48);
        map.put(9572, (byte) -47);
        map.put(9573, (byte) -46);
        map.put(9561, (byte) -45);
        map.put(9560, (byte) -44);
        map.put(9554, (byte) -43);
        map.put(9555, (byte) -42);
        map.put(9579, (byte) -41);
        map.put(9578, (byte) -40);
        map.put(9496, (byte) -39);
        map.put(9484, (byte) -38);
        map.put(9608, (byte) -37);
        map.put(9604, (byte) -36);
        map.put(9612, (byte) -35);
        map.put(9616, (byte) -34);
        map.put(9600, (byte) -33);
        map.put(1088, (byte) -32);
        map.put(1089, (byte) -31);
        map.put(1090, (byte) -30);
        map.put(1091, (byte) -29);
        map.put(1092, (byte) -28);
        map.put(1093, (byte) -27);
        map.put(1094, (byte) -26);
        map.put(1095, (byte) -25);
        map.put(1096, (byte) -24);
        map.put(1097, (byte) -23);
        map.put(1098, (byte) -22);
        map.put(1099, (byte) -21);
        map.put(1100, (byte) -20);
        map.put(1101, (byte) -19);
        map.put(1102, (byte) -18);
        map.put(1103, (byte) -17);
        map.put(Integer.valueOf(InputDeviceCompat.SOURCE_GAMEPAD), (byte) -16);
        map.put(1105, (byte) -15);
        map.put(1028, (byte) -14);
        map.put(1108, (byte) -13);
        map.put(1031, (byte) -12);
        map.put(1111, (byte) -11);
        map.put(1038, (byte) -10);
        map.put(1118, (byte) -9);
        map.put(176, (byte) -8);
        map.put(8729, (byte) -7);
        map.put(183, (byte) -6);
        map.put(8730, (byte) -5);
        map.put(8470, (byte) -4);
        map.put(164, (byte) -3);
        map.put(9632, (byte) -2);
        map.put(160, (byte) -1);
        byte[] realData = new byte[5000];
        try {
            byte[] data = content.getBytes("UNICODE");
            int k = 3;
            realData[0] = 27;
            realData[1] = 116;
            realData[2] = 7;
            for (int i = 2; i < data.length; i += 2) {
                int c = (data[i + 1] * PrinterConstants.BarcodeType.UPC_A) + data[i];
                if (map.get(Integer.valueOf(c)) != null) {
                    realData[k] = ((Byte) map.get(Integer.valueOf(c))).byteValue();
                    k++;
                    new byte[1][0] = ((Byte) map.get(Integer.valueOf(c))).byteValue();
                } else {
                    realData[k] = data[i];
                    k++;
                }
            }
            realData[k] = 10;
        } catch (Exception e) {
        }
        return sendByteData(realData);
    }

    public int sendByteData(byte[] data) {
        if (data == null) {
            return -1;
        }
        Utils.Log(TAG, "sendByteData length is: " + data.length);
        return this.myPrinter.write(data);
    }

    public int printImage(Bitmap bitmap) {
        return sendByteData(new Command().addBitImage(bitmap).getCommand());
    }

    public int printImage(Bitmap bitmap, int width, int mode) {
        return sendByteData(new Command().addRastBitImage(bitmap, width, mode).getCommand());
    }

    public int updatePrint(byte[] fileByte) {
        byte[] buff = new Command().addApplication(fileByte).getCommand();
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(buff);
            byte[] by = new byte[1024];
            while (true) {
                int len = bis.read(by, 0, by.length);
                if (len == -1) {
                    bis.close();
                    return buff.length;
                }
                byte[] sendData = new byte[len];
                System.arraycopy(by, 0, sendData, 0, len);
                sendByteData(sendData);
                Thread.sleep((long) this.sendSleep);
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public void prn_PageSetup(int pageWidth, int pageHeight) {
        printText(LabelPrint.label_set_page(pageWidth, pageHeight, 0));
    }

    public void prn_PagePrint(int rotate) {
        printText(LabelPrint.label_print(rotate));
    }

    public void prn_DrawLine(int lineWidth, int x0, int y0, int x1, int y1) {
        printText(LabelPrint.label_put_lines(lineWidth, x0, y0, x1, y1));
    }

    public void prn_DrawText(int x, int y, String text, String fontName, int fontsize, int rotate, int bold, int underline, int reverse) {
        printText(LabelPrint.label_put_text(x, y, text, fontName, fontsize, rotate, bold, underline, reverse));
    }

    public void prn_DrawBarcode(int x, int y, String text, int barcodetype, int rotate, int linewidth, int height) {
        printText(LabelPrint.label_put_barcode(x, y, text, barcodetype, rotate, linewidth, height));
    }

    public int setFont(int mWidth, int mHeight, int mBold, int mUnderline) {
        int mFontSize = 0;
        int mFontMode = 0;
        int mRetVal = 0;
        if (mBold == 0 || mBold == 1) {
            mFontMode = 0 | (mBold << 3);
        } else {
            mRetVal = 3;
        }
        if (mUnderline == 0 || mUnderline == 1) {
            mFontMode |= mUnderline << 7;
        } else {
            mRetVal = 4;
        }
        setPrinter(16, mFontMode);
        if (mWidth < 0 || mWidth > 7) {
            mRetVal = 1;
        } else {
            mFontSize = 0 | (mWidth << 4);
        }
        if (mHeight < 0 || mHeight > 7) {
            mRetVal = 2;
        } else {
            mFontSize |= mHeight;
        }
        setPrinter(17, mFontSize);
        return mRetVal;
    }

    public int printTable(Table table) {
        return printText(table.getTableText());
    }

    public int printBarCode(Barcode barcode) {
        return sendByteData(barcode.getBarcodeData());
    }

    public void init() {
        setPrinter(0);
    }

    public byte[] read() {
        return this.myPrinter.read();
    }

    public boolean setPrinter(int command) {
        byte[] arrayOfByte = null;
        switch (command) {
            case 0:
                arrayOfByte = new byte[]{27, 64};
                break;
            case 1:
                arrayOfByte = new byte[]{0};
                break;
            case 2:
                arrayOfByte = new byte[]{12};
                break;
            case 3:
                arrayOfByte = new byte[]{10};
                break;
            case 4:
                arrayOfByte = new byte[]{13};
                break;
            case 5:
                arrayOfByte = new byte[]{9};
                break;
            case 6:
                arrayOfByte = new byte[]{27, 50};
                break;
        }
        sendByteData(arrayOfByte);
        return true;
    }

    public boolean setPrinter(int command, int value) {
        byte[] arrayOfByte = new byte[3];
        switch (command) {
            case 0:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 74;
                break;
            case 1:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = PrinterConstants.BarcodeType.PDF417;
                break;
            case 2:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 33;
                break;
            case 3:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 85;
                break;
            case 4:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 86;
                break;
            case 5:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 87;
                break;
            case 6:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 45;
                break;
            case 7:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 43;
                break;
            case 8:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 105;
                break;
            case 9:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 99;
                break;
            case 10:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 51;
                break;
            case 11:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 32;
                break;
            case 12:
                arrayOfByte[0] = 28;
                arrayOfByte[1] = 80;
                break;
            case 13:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 97;
                if (value > 2 || value < 0) {
                    return false;
                }
            case 16:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 33;
                break;
            case 17:
                arrayOfByte[0] = 29;
                arrayOfByte[1] = 33;
                break;
        }
        arrayOfByte[2] = (byte) value;
        sendByteData(arrayOfByte);
        return true;
    }

    public void setCharacterMultiple(int x, int y) {
        byte[] arrayOfByte = new byte[3];
        arrayOfByte[0] = 29;
        arrayOfByte[1] = 33;
        if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
            arrayOfByte[2] = (byte) ((x * 16) + y);
            sendByteData(arrayOfByte);
        }
    }

    public void setLeftMargin(int nL, int nH) {
        sendByteData(new byte[]{29, 76, (byte) nL, (byte) nH});
    }

    public void setPrintModel(boolean isBold, boolean isDoubleHeight, boolean isDoubleWidth, boolean isUnderLine) {
        byte[] arrayOfByte = new byte[3];
        arrayOfByte[0] = 27;
        arrayOfByte[1] = 33;
        int temp = 0;
        if (isBold) {
            temp = (byte) 8;
        }
        if (isDoubleHeight) {
            temp = (byte) (temp | 16);
        }
        if (isDoubleWidth) {
            temp = (byte) (temp | 32);
        }
        if (isUnderLine) {
            temp = (byte) (temp | 128);
        }
        arrayOfByte[2] = (byte) temp;
        sendByteData(arrayOfByte);
    }

    public void cutPaper() {
        sendByteData(new byte[]{29, 86, 66, 0});
    }

    public void ringBuzzer(byte time) {
        sendByteData(new byte[]{29, 105, time});
    }

    public void openCashbox(boolean cashbox1, boolean cashbox2) {
        if (cashbox1) {
            sendByteData(new byte[]{27, 112, 0, 50, 50});
        }
        if (cashbox2) {
            sendByteData(new byte[]{27, 112, 1, 50, 50});
        }
    }
}
