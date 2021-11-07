package com.android.print.sdk;

public class LabelPrint {
    public static byte[] LabelBuf = new byte[8192];
    public static int LabelDataSize = 0;

    public static void buf_write(byte[] buf, int len, int timeout) {
        if (LabelDataSize <= 8192) {
            for (int i = 0; i < len; i++) {
                LabelBuf[LabelDataSize + i] = buf[i];
            }
            LabelDataSize += len;
        }
    }

    public static String label_set_page(int width, int height, int rotate) {
        return "! 20 200 200 " + height + " 1\r\nPW " + width + "\r\n";
    }

    public static String label_print(int rotate) {
        return "PR " + rotate + "\r\nFORM\r\nPRINT\r\n";
    }

    public static String label_put_lines(int width, int x0, int y0, int x1, int y1) {
        return "LINE " + x0 + " " + y0 + " " + x1 + " " + y1 + " " + width + "\r\n";
    }

    public static String label_put_text(int x, int y, String text, String fontName, int fontsize, int rotate, int bold, int underline, int reverse) {
        int fontNaID;
        int fontSzID;
        int bold2;
        if (fontName.equals("宋体")) {
            if (fontsize < 25) {
                fontNaID = 24;
                fontSzID = 0;
            } else if (fontsize <= 24 || fontsize >= 36) {
                fontNaID = 24;
                fontSzID = 11;
            } else {
                fontNaID = 4;
                fontSzID = 11;
            }
        } else if (fontsize < 40) {
            fontNaID = 4;
            fontSzID = 11;
        } else if (fontsize <= 39 || fontsize >= 60) {
            fontNaID = 24;
            fontSzID = 11;
        } else {
            fontNaID = 4;
            fontSzID = 22;
        }
        if (fontNaID == 4) {
            bold2 = 1;
        } else {
            bold2 = 0;
        }
        String st1 = "TEXT";
        if (rotate == 1) {
            st1 = "T90";
        } else if (rotate == 2) {
            st1 = "T180";
        } else if (rotate == 3) {
            st1 = "T270";
        }
        return "UT " + underline + "\r\nSETBOLD " + bold2 + "\r\nIT " + reverse + "\r\n" + st1 + " " + fontNaID + " " + fontSzID + " " + x + " " + y + " " + text + "\r\n";
    }

    public static String label_put_barcode(int x, int y, String text, int barcodetype, int rotate, int linewidth, int height) {
        String st1 = "B";
        if (rotate != 0) {
            st1 = "VB";
        }
        return String.valueOf(st1) + " 128 1 2 " + height + " " + x + " " + y + " " + text + "\r\n";
    }
}
