package com.android.print.demo.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.android.print.demov3.R;
import com.android.print.sdk.Barcode;
import com.android.print.sdk.PrinterConstants;
import com.android.print.sdk.PrinterInstance;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PrintUtils {
    public static void printText(Resources resources, PrinterInstance mPrinter) {
        mPrinter.init();
        mPrinter.printText(resources.getString(R.string.str_text));
        mPrinter.setPrinter(1, 2);
        mPrinter.setFont(0, 0, 0, 0);
        mPrinter.setPrinter(13, 0);
        mPrinter.printText(resources.getString(R.string.str_text_left));
        mPrinter.setPrinter(1, 2);
        mPrinter.setPrinter(13, 1);
        mPrinter.printText(resources.getString(R.string.str_text_center));
        mPrinter.setPrinter(1, 2);
        mPrinter.setPrinter(13, 2);
        mPrinter.printText(resources.getString(R.string.str_text_right));
        mPrinter.setPrinter(1, 3);
        mPrinter.setPrinter(13, 0);
        mPrinter.setFont(0, 0, 1, 0);
        mPrinter.printText(resources.getString(R.string.str_text_strong));
        mPrinter.setPrinter(1, 2);
        mPrinter.setFont(0, 0, 0, 1);
        mPrinter.sendByteData(new byte[]{28, 33, Byte.MIN_VALUE});
        mPrinter.printText(resources.getString(R.string.str_text_underline));
        mPrinter.sendByteData(new byte[]{28, 33, 0});
        mPrinter.setPrinter(1, 2);
        mPrinter.setFont(0, 0, 0, 0);
        mPrinter.printText(resources.getString(R.string.str_text_height));
        mPrinter.setPrinter(1, 2);
        for (int i = 0; i < 4; i++) {
            mPrinter.setFont(i, i, 0, 0);
            mPrinter.printText((i + 1) + resources.getString(R.string.times));
        }
        mPrinter.setPrinter(1, 1);
        mPrinter.setPrinter(1, 3);
        for (int i2 = 0; i2 < 4; i2++) {
            mPrinter.setFont(i2, i2, 0, 0);
            mPrinter.printText(resources.getString(R.string.bigger) + (i2 + 1) + resources.getString(R.string.bigger1));
            mPrinter.setPrinter(1, 3);
        }
        mPrinter.setFont(0, 0, 0, 0);
        mPrinter.setPrintModel(true, false, false, false);
        mPrinter.printText("文字加粗演示\n");
        mPrinter.setPrintModel(true, true, false, false);
        mPrinter.printText("文字加粗倍高演示\n");
        mPrinter.setPrintModel(true, false, true, false);
        mPrinter.printText("文字加粗倍宽演示\n");
        mPrinter.setPrintModel(true, true, true, false);
        mPrinter.printText("文字加粗倍高倍宽演示\n");
        mPrinter.setPrintModel(false, false, false, true);
        mPrinter.printText("文字下划线演示\n");
        mPrinter.setFont(0, 0, 0, 0);
        mPrinter.setPrinter(13, 0);
        mPrinter.setPrinter(1, 3);
    }

    public static void printImage(Resources resources, PrinterInstance mPrinter) {
        mPrinter.init();
        mPrinter.setFont(0, 0, 0, 0);
        mPrinter.setPrinter(13, 0);
        mPrinter.printText(resources.getString(R.string.str_image));
        mPrinter.setPrinter(1, 2);
        try {
            mPrinter.printImage(BitmapFactory.decodeStream(resources.getAssets().open("android.png")));
            mPrinter.printText("\n\n\n\n");
            try {
                Bitmap bitmap1 = BitmapFactory.decodeStream(resources.getAssets().open("support.png"));
                mPrinter.setPrinter(13, 1);
                mPrinter.printText(resources.getString(R.string.str_image));
                mPrinter.printImage(bitmap1);
                mPrinter.printText("\n\n\n\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void printBarcode(Resources resources, PrinterInstance mPrinter) {
        mPrinter.init();
        mPrinter.printText(resources.getString(R.string.print) + "BarcodeType.CODE39" + resources.getString(R.string.str_show));
        mPrinter.setPrinter(1, 2);
        mPrinter.printBarCode(new Barcode((byte) 4, 2, 150, 2, "123456"));
        mPrinter.setPrinter(1, 2);
        mPrinter.printText(resources.getString(R.string.print) + " BarcodeType.CODABAR" + resources.getString(R.string.str_show));
        mPrinter.setPrinter(1, 2);
        mPrinter.printBarCode(new Barcode((byte) 6, 2, 150, 2, "123456"));
        mPrinter.setPrinter(1, 2);
        mPrinter.printText(resources.getString(R.string.print) + "BarcodeType.ITF" + resources.getString(R.string.str_show));
        mPrinter.setPrinter(1, 2);
        mPrinter.printBarCode(new Barcode((byte) 5, 2, 150, 2, "123456"));
        mPrinter.setPrinter(1, 2);
        mPrinter.printText(resources.getString(R.string.print) + "BarcodeType.CODE93" + resources.getString(R.string.str_show));
        mPrinter.setPrinter(1, 2);
        mPrinter.printBarCode(new Barcode(PrinterConstants.BarcodeType.CODE93, 2, 150, 2, "123456"));
        mPrinter.setPrinter(1, 2);
        mPrinter.printText(resources.getString(R.string.print) + " BarcodeType.CODE128" + resources.getString(R.string.str_show));
        mPrinter.setPrinter(1, 2);
        mPrinter.printBarCode(new Barcode(PrinterConstants.BarcodeType.CODE128, 2, 150, 2, "123456"));
        mPrinter.setPrinter(1, 2);
        mPrinter.printText(resources.getString(R.string.print) + " BarcodeType.UPC_A" + resources.getString(R.string.str_show));
        mPrinter.setPrinter(1, 2);
        mPrinter.printBarCode(new Barcode((byte) 0, 2, 63, 2, "000000000000"));
        mPrinter.setPrinter(1, 2);
        mPrinter.printText(resources.getString(R.string.print) + "BarcodeType.UPC_E" + resources.getString(R.string.str_show));
        mPrinter.setPrinter(1, 2);
        mPrinter.printBarCode(new Barcode((byte) 1, 2, 63, 2, "000000000000"));
        mPrinter.setPrinter(1, 2);
        mPrinter.printText(resources.getString(R.string.print) + "BarcodeType.JAN13" + resources.getString(R.string.str_show));
        mPrinter.setPrinter(1, 2);
        mPrinter.printBarCode(new Barcode((byte) 2, 2, 63, 2, "000000000000"));
        mPrinter.setPrinter(1, 2);
        mPrinter.printText(resources.getString(R.string.print) + "BarcodeType.JAN8" + resources.getString(R.string.str_show));
        mPrinter.setPrinter(1, 2);
        mPrinter.printBarCode(new Barcode((byte) 3, 2, 63, 2, "0000000"));
        mPrinter.setPrinter(1, 2);
    }

    public static void printBigData(Resources resources, PrinterInstance mPrinter) {
        try {
            InputStream is = resources.getAssets().open("58-big-data-test.bin");
            byte[] fileByte = new byte[is.available()];
            is.read(fileByte);
            mPrinter.init();
            mPrinter.sendByteData(fileByte);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printUpdate(Resources resources, PrinterInstance mPrinter, String filePath) {
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            byte[] fileByte = new byte[fis.available()];
            fis.read(fileByte);
            mPrinter.init();
            mPrinter.updatePrint(fileByte);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
