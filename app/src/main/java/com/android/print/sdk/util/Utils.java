package com.android.print.sdk.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import com.android.print.sdk.PrinterInstance;
import com.android.print.sdk.bluetooth.BluetoothPort;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class Utils {
    public static Bitmap compressBitmap(Bitmap srcBitmap, int maxLength) {
        double ratio;
        int destHeight;
        int destWidth;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            byte[] srcBytes = bitmap2Bytes(srcBitmap);
            BitmapFactory.decodeByteArray(srcBytes, 0, srcBytes.length, opts);
            int srcWidth = opts.outWidth;
            int srcHeight = opts.outHeight;
            if (srcWidth > srcHeight) {
                ratio = (double) (srcWidth / maxLength);
                destWidth = maxLength;
                destHeight = (int) (((double) srcHeight) / ratio);
            } else {
                ratio = (double) (srcHeight / maxLength);
                destHeight = maxLength;
                destWidth = (int) (((double) srcWidth) / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inSampleSize = ((int) ratio) + 1;
            newOpts.inJustDecodeBounds = false;
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            return BitmapFactory.decodeByteArray(srcBytes, 0, srcBytes.length, newOpts);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while (true) {
            int len = inStream.read(buffer);
            if (len == -1) {
                byte[] data = outStream.toByteArray();
                outStream.close();
                inStream.close();
                return data;
            }
            outStream.write(buffer, 0, len);
        }
    }

    public static Bitmap getImageFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes == null) {
            return null;
        }
        if (opts != null) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) (w / width), (float) (h / height));
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0022 A[SYNTHETIC, Splitter:B:15:0x0022] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0027 A[SYNTHETIC, Splitter:B:18:0x0027] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0038 A[SYNTHETIC, Splitter:B:27:0x0038] */
    /* JADX WARNING: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.File saveFileFromBytes(byte[] r8, java.lang.String r9) {
        /*
            r5 = 0
            r2 = 0
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x001c }
            r3.<init>(r9)     // Catch:{ Exception -> 0x001c }
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x004f, all -> 0x0048 }
            r4.<init>(r3)     // Catch:{ Exception -> 0x004f, all -> 0x0048 }
            java.io.BufferedOutputStream r6 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x004f, all -> 0x0048 }
            r6.<init>(r4)     // Catch:{ Exception -> 0x004f, all -> 0x0048 }
            r6.write(r8)     // Catch:{ Exception -> 0x0052, all -> 0x004b }
            if (r6 == 0) goto L_0x0045
            r6.close()     // Catch:{ IOException -> 0x0041 }
            r2 = r3
            r5 = r6
        L_0x001b:
            return r2
        L_0x001c:
            r0 = move-exception
        L_0x001d:
            r0.printStackTrace()     // Catch:{ all -> 0x0035 }
            if (r5 == 0) goto L_0x0025
            r5.close()     // Catch:{ IOException -> 0x0030 }
        L_0x0025:
            if (r5 == 0) goto L_0x001b
            r5.close()     // Catch:{ IOException -> 0x002b }
            goto L_0x001b
        L_0x002b:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x001b
        L_0x0030:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ all -> 0x0035 }
            goto L_0x0025
        L_0x0035:
            r7 = move-exception
        L_0x0036:
            if (r5 == 0) goto L_0x003b
            r5.close()     // Catch:{ IOException -> 0x003c }
        L_0x003b:
            throw r7
        L_0x003c:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x003b
        L_0x0041:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0045:
            r2 = r3
            r5 = r6
            goto L_0x001b
        L_0x0048:
            r7 = move-exception
            r2 = r3
            goto L_0x0036
        L_0x004b:
            r7 = move-exception
            r2 = r3
            r5 = r6
            goto L_0x0036
        L_0x004f:
            r0 = move-exception
            r2 = r3
            goto L_0x001d
        L_0x0052:
            r0 = move-exception
            r2 = r3
            r5 = r6
            goto L_0x001d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.print.sdk.util.Utils.saveFileFromBytes(byte[], java.lang.String):java.io.File");
    }

    public static int printBitmap2File(Bitmap bitmap, String filePath) {
        File file;
        if (filePath.endsWith(".png")) {
            file = new File(filePath);
        } else {
            file = new File(String.valueOf(filePath) + ".png");
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static byte[] bitmap2PrinterBytes(Bitmap bitmap, int left) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        byte[] imgbuf = new byte[(((width / 8) + left + 4) * height)];
        byte[] bitbuf = new byte[(width / 8)];
        int[] p = new int[8];
        int s = 0;
        System.out.println("+++++++++++++++ Total Bytes: " + (((width / 8) + 4) * height));
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width / 8; x++) {
                for (int m = 0; m < 8; m++) {
                    if (bitmap.getPixel((x * 8) + m, y) == -1) {
                        p[m] = 0;
                    } else {
                        p[m] = 1;
                    }
                }
                bitbuf[x] = (byte) ((p[0] * 128) + (p[1] * 64) + (p[2] * 32) + (p[3] * 16) + (p[4] * 8) + (p[5] * 4) + (p[6] * 2) + p[7]);
            }
            if (y != 0) {
                s++;
                imgbuf[s] = 22;
            } else {
                imgbuf[s] = 22;
            }
            int s2 = s + 1;
            imgbuf[s2] = (byte) ((width / 8) + left);
            for (int j = 0; j < left; j++) {
                s2++;
                imgbuf[s2] = 0;
            }
            for (int n = 0; n < width / 8; n++) {
                s2++;
                imgbuf[s2] = bitbuf[n];
            }
            int s3 = s2 + 1;
            imgbuf[s3] = 21;
            s = s3 + 1;
            imgbuf[s] = 1;
        }
        Log.v("imgbuf", Arrays.toString(imgbuf));
        return imgbuf;
    }

    public static byte[] bitmap2PrinterBytes_stylus(Bitmap bitmap, int multiple, int left) {
        byte[] imgBuf;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth() + left;
        boolean need_0a = false;
        if (width < 240) {
            imgBuf = new byte[(((height / 8) + 1) * (width + 6))];
            need_0a = true;
        } else {
            imgBuf = new byte[((((height / 8) + 1) * (width + 5)) + 2)];
        }
        byte[] tmpBuf = new byte[(width + 5)];
        int[] p = new int[8];
        int s = 0;
        for (int y = 0; y < (height / 8) + 1; y++) {
            tmpBuf[0] = 27;
            int t = 0 + 1;
            tmpBuf[t] = 42;
            int t2 = t + 1;
            tmpBuf[t2] = (byte) multiple;
            int t3 = t2 + 1;
            tmpBuf[t3] = (byte) (width % 240);
            int t4 = t3 + 1;
            tmpBuf[t4] = (byte) (width / 240 > 0 ? 1 : 0);
            boolean allZERO = true;
            for (int x = 0; x < width; x++) {
                for (int m = 0; m < 8; m++) {
                    if ((y * 8) + m >= height || x < left) {
                        p[m] = 0;
                    } else {
                        p[m] = bitmap.getPixel(x - left, (y * 8) + m) == -1 ? 0 : 1;
                    }
                }
                int value = (p[0] * 128) + (p[1] * 64) + (p[2] * 32) + (p[3] * 16) + (p[4] * 8) + (p[5] * 4) + (p[6] * 2) + p[7];
                t4++;
                tmpBuf[t4] = (byte) value;
                if (value != 0) {
                    allZERO = false;
                }
            }
            if (allZERO) {
                if (s == 0) {
                    imgBuf[s] = 27;
                } else {
                    s++;
                    imgBuf[s] = 27;
                }
                int s2 = s + 1;
                imgBuf[s2] = 74;
                s = s2 + 1;
                imgBuf[s] = 8;
            } else {
                for (int i = 0; i < t4 + 1; i++) {
                    if (i == 0 && s == 0) {
                        imgBuf[s] = tmpBuf[i];
                    } else {
                        s++;
                        imgBuf[s] = tmpBuf[i];
                    }
                }
                if (need_0a) {
                    s++;
                    imgBuf[s] = 10;
                }
            }
        }
        if (!need_0a) {
            int s3 = s + 1;
            imgBuf[s3] = 13;
            s = s3 + 1;
            imgBuf[s] = 10;
        }
        byte[] realBuf = new byte[(s + 1)];
        for (int i2 = 0; i2 < s + 1; i2++) {
            realBuf[i2] = imgBuf[i2];
        }
        StringBuffer sb = new StringBuffer();
        for (int i3 = 0; i3 < realBuf.length; i3++) {
            String temp = Integer.toHexString(realBuf[i3] & 255);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            sb.append(String.valueOf(temp) + " ");
            if ((i3 != 0 && i3 % 100 == 0) || i3 == realBuf.length - 1) {
                Log.e("12345", sb.toString());
                sb = new StringBuffer();
            }
        }
        return realBuf;
    }

    public static int getStringCharacterLength(String line) {
        int length = 0;
        for (int j = 0; j < line.length(); j++) {
            if (line.charAt(j) > 256) {
                length += 2;
            } else {
                length++;
            }
        }
        return length;
    }

    public static int getSubLength(String line, int width) {
        int temp;
        int length = 0;
        for (int j = 0; j < line.length(); j++) {
            if (line.charAt(j) > 256) {
                length += 2;
            } else {
                length++;
            }
            if (length > width) {
                int temp2 = line.substring(0, j - 1).lastIndexOf(" ");
                if (temp2 != -1) {
                    return temp2;
                }
                if (j - 1 == 0) {
                    temp = 1;
                } else {
                    temp = j - 1;
                }
                return temp;
            }
        }
        return line.length();
    }

    public static boolean isNum(byte temp) {
        return temp >= 48 && temp <= 57;
    }

    public static void Log(String tag, String msg) {
        if (PrinterInstance.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static boolean saveBtConnInfo(Context context, String mac) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir(), "btinfo.properties"));
            Properties pro = new Properties();
            pro.setProperty("mac", mac);
            pro.store(fos, "btinfo.properties");
            fos.close();
            Log.v("utils", "save-success!");
            return true;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static Properties getBtConnInfo(Context context) {
        try {
            FileInputStream fis = new FileInputStream(new File(context.getFilesDir(), "btinfo.properties"));
            Properties pro = new Properties();
            pro.load(fis);
            Log.v("utils", "get-success!");
            fis.close();
            return pro;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getPrinterStatus(int time) {
        int Ret = 0;
        byte data = BluetoothPort.getData(time);
        Log.v("data", new StringBuilder(String.valueOf(data)).toString());
        if ((data & 32) != 0) {
            Ret = 1;
        }
        if ((data & 4) != 0) {
            Ret = 2;
        }
        if (data == 0) {
            return 3;
        }
        return Ret;
    }

    public static int getPrintStatus(int time) {
        byte data = BluetoothPort.getEndData(time);
        Log.v("i", new StringBuilder(String.valueOf(data)).toString());
        if (data == Byte.MIN_VALUE || data == 128) {
            return 1;
        }
        return 0;
    }
}
