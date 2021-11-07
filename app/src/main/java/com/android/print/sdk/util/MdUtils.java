package com.android.print.sdk.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.support.p003v7.widget.helper.ItemTouchHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MdUtils {
    public static final int ALGORITHM_DITHER_16x16 = 16;
    public static final int ALGORITHM_DITHER_8x8 = 8;
    public static final int ALGORITHM_GRAYTEXTMODE = 1;
    public static final int ALGORITHM_TEXTMODE = 2;
    private static int[][] Floyd16x16;
    private static int[][] Floyd8x8;

    /* renamed from: p0 */
    private static int[] f49p0;

    /* renamed from: p1 */
    private static int[] f50p1;

    /* renamed from: p2 */
    private static int[] f51p2;

    /* renamed from: p3 */
    private static int[] f52p3;

    /* renamed from: p4 */
    private static int[] f53p4;

    /* renamed from: p5 */
    private static int[] f54p5;

    /* renamed from: p6 */
    private static int[] f55p6;

    static {
        int[] iArr = new int[2];
        iArr[1] = 128;
        f49p0 = iArr;
        int[] iArr2 = new int[2];
        iArr2[1] = 64;
        f50p1 = iArr2;
        int[] iArr3 = new int[2];
        iArr3[1] = 32;
        f51p2 = iArr3;
        int[] iArr4 = new int[2];
        iArr4[1] = 16;
        f52p3 = iArr4;
        int[] iArr5 = new int[2];
        iArr5[1] = 8;
        f53p4 = iArr5;
        int[] iArr6 = new int[2];
        iArr6[1] = 4;
        f54p5 = iArr6;
        int[] iArr7 = new int[2];
        iArr7[1] = 2;
        f55p6 = iArr7;
        int[] iArr8 = new int[16];
        iArr8[1] = 128;
        iArr8[2] = 32;
        iArr8[3] = 160;
        iArr8[4] = 8;
        iArr8[5] = 136;
        iArr8[6] = 40;
        iArr8[7] = 168;
        iArr8[8] = 2;
        iArr8[9] = 130;
        iArr8[10] = 34;
        iArr8[11] = 162;
        iArr8[12] = 10;
        iArr8[13] = 138;
        iArr8[14] = 42;
        iArr8[15] = 170;
        Floyd16x16 = new int[][]{iArr8, new int[]{192, 64, 224, 96, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 72, 232, 104, 194, 66, 226, 98, 202, 74, 234, 106}, new int[]{48, 176, 16, 144, 56, 184, 24, 152, 50, 178, 18, 146, 58, 186, 26, 154}, new int[]{240, 112, 208, 80, 248, 120, 216, 88, 242, 114, 210, 82, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, 122, 218, 90}, new int[]{12, 140, 44, 172, 4, 132, 36, 164, 14, 142, 46, 174, 6, 134, 38, 166}, new int[]{204, 76, 236, 108, 196, 68, 228, 100, 206, 78, 238, 110, 198, 70, 230, 102}, new int[]{60, 188, 28, 156, 52, 180, 20, 148, 62, 190, 30, 158, 54, 182, 22, 150}, new int[]{252, 124, 220, 92, 244, 116, 212, 84, 254, 126, 222, 94, 246, 118, 214, 86}, new int[]{3, 131, 35, 163, 11, 139, 43, 171, 1, 129, 33, 161, 9, 137, 41, 169}, new int[]{195, 67, 227, 99, 203, 75, 235, 107, 193, 65, 225, 97, 201, 73, 233, 105}, new int[]{51, 179, 19, 147, 59, 187, 27, 155, 49, 177, 17, 145, 57, 185, 25, 153}, new int[]{243, 115, 211, 83, 251, 123, 219, 91, 241, 113, 209, 81, 249, 121, 217, 89}, new int[]{15, 143, 47, 175, 7, 135, 39, 167, 13, 141, 45, 173, 5, 133, 37, 165}, new int[]{207, 79, 239, 111, 199, 71, 231, 103, 205, 77, 237, 109, 197, 69, 229, 101}, new int[]{63, 191, 31, 159, 55, 183, 23, 151, 61, 189, 29, 157, 53, 181, 21, 149}, new int[]{254, 127, 223, 95, 247, 119, 215, 87, 253, 125, 221, 93, 245, 117, 213, 85}};
        int[] iArr9 = new int[8];
        iArr9[1] = 32;
        iArr9[2] = 8;
        iArr9[3] = 40;
        iArr9[4] = 2;
        iArr9[5] = 34;
        iArr9[6] = 10;
        iArr9[7] = 42;
        Floyd8x8 = new int[][]{iArr9, new int[]{48, 16, 56, 24, 50, 18, 58, 26}, new int[]{12, 44, 4, 36, 14, 46, 6, 38}, new int[]{60, 28, 52, 20, 62, 30, 54, 22}, new int[]{3, 35, 11, 43, 1, 33, 9, 41}, new int[]{51, 19, 59, 27, 49, 17, 57, 25}, new int[]{15, 47, 7, 39, 13, 45, 5, 37}, new int[]{63, 31, 55, 23, 61, 29, 53, 21}};
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) (w / width), (float) (h / height));
        return Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
    }

    public static void saveMyBitmap(Bitmap mBitmap) {
        File f = new File(Environment.getExternalStorageDirectory().getPath(), "Btatotest.jpeg");
        try {
            f.createNewFile();
        } catch (IOException e) {
        }
        try {
            FileOutputStream fOut = new FileOutputStream(f);
            try {
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                FileOutputStream fileOutputStream = fOut;
            } catch (FileNotFoundException e2) {
                FileOutputStream fileOutputStream2 = fOut;
            } catch (IOException e3) {
                FileOutputStream fileOutputStream3 = fOut;
            }
        } catch (FileNotFoundException | IOException e4) {
        }
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        Bitmap bmpGrayscale = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        c.drawBitmap(bmpOriginal, 0.0f, 0.0f, paint);
        return bmpGrayscale;
    }

    static byte[] pixToEscRastBitImageCmd(byte[] src, int nWidth, int nMode) {
        int nHeight = src.length / nWidth;
        byte[] data = new byte[((src.length / 8) + 8)];
        data[0] = 29;
        data[1] = 118;
        data[2] = 48;
        data[3] = (byte) (nMode & 1);
        data[4] = (byte) ((nWidth / 8) % 256);
        data[5] = (byte) ((nWidth / 8) / 256);
        data[6] = (byte) (nHeight % 256);
        data[7] = (byte) (nHeight / 256);
        int k = 0;
        for (int i = 8; i < data.length; i++) {
            data[i] = (byte) (f49p0[src[k]] + f50p1[src[k + 1]] + f51p2[src[k + 2]] + f52p3[src[k + 3]] + f53p4[src[k + 4]] + f54p5[src[k + 5]] + f55p6[src[k + 6]] + src[k + 7]);
            k += 8;
        }
        return data;
    }

    public static byte[] pixToEscRastBitImageCmd(byte[] src) {
        byte[] data = new byte[(src.length / 8)];
        int k = 0;
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (f49p0[src[k]] + f50p1[src[k + 1]] + f51p2[src[k + 2]] + f52p3[src[k + 3]] + f53p4[src[k + 4]] + f54p5[src[k + 5]] + f55p6[src[k + 6]] + src[k + 7]);
            k += 8;
        }
        return data;
    }

    public static byte[] pixToEscNvBitImageCmd(byte[] src, int width, int height) {
        byte[] data = new byte[((src.length / 8) + 4)];
        data[0] = (byte) ((width / 8) % 256);
        data[1] = (byte) ((width / 8) / 256);
        data[2] = (byte) ((height / 8) % 256);
        data[3] = (byte) ((height / 8) / 256);
        for (int i = 0; i < width; i++) {
            int k = 0;
            for (int j = 0; j < height / 8; j++) {
                data[j + 4 + ((i * height) / 8)] = (byte) (f49p0[src[i + k]] + f50p1[src[i + k + (width * 1)]] + f51p2[src[i + k + (width * 2)]] + f52p3[src[i + k + (width * 3)]] + f53p4[src[i + k + (width * 4)]] + f54p5[src[i + k + (width * 5)]] + f55p6[src[i + k + (width * 6)]] + src[i + k + (width * 7)]);
                k += width * 8;
            }
        }
        return data;
    }

    public static byte[] pixToTscCmd(byte[] src) {
        byte[] data = new byte[(src.length / 8)];
        int j = 0;
        for (int k = 0; k < data.length; k++) {
            data[k] = (byte) (((byte) (((((((f49p0[src[j]] + f50p1[src[j + 1]]) + f51p2[src[j + 2]]) + f52p3[src[j + 3]]) + f53p4[src[j + 4]]) + f54p5[src[j + 5]]) + f55p6[src[j + 6]]) + src[j + 7])) ^ -1);
            j += 8;
        }
        return data;
    }

    public static byte[] pixToTscCmd(int x, int y, int mode, byte[] src, int nWidth) {
        byte[] bitmap = null;
        try {
            bitmap = ("BITMAP " + x + "," + y + "," + (nWidth / 8) + "," + (src.length / nWidth) + "," + mode + ",").getBytes("GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] arrayOfByte = new byte[(src.length / 8)];
        int j = 0;
        for (int k = 0; k < arrayOfByte.length; k++) {
            arrayOfByte[k] = (byte) (((byte) (((((((f49p0[src[j]] + f50p1[src[j + 1]]) + f51p2[src[j + 2]]) + f52p3[src[j + 3]]) + f53p4[src[j + 4]]) + f54p5[src[j + 5]]) + f55p6[src[j + 6]]) + src[j + 7])) ^ -1);
            j += 8;
        }
        byte[] data = new byte[(bitmap.length + arrayOfByte.length)];
        System.arraycopy(bitmap, 0, data, 0, bitmap.length);
        System.arraycopy(arrayOfByte, 0, data, bitmap.length, arrayOfByte.length);
        return data;
    }

    private static void format_K_dither16x16(int[] orgpixels, int xsize, int ysize, byte[] despixels) {
        int k = 0;
        for (int y = 0; y < ysize; y++) {
            for (int x = 0; x < xsize; x++) {
                if ((orgpixels[k] & 255) > Floyd16x16[x & 15][y & 15]) {
                    despixels[k] = 0;
                } else {
                    despixels[k] = 1;
                }
                k++;
            }
        }
    }

    public static byte[] bitmapToBWPix(Bitmap mBitmap) {
        int[] pixels = new int[(mBitmap.getWidth() * mBitmap.getHeight())];
        byte[] data = new byte[(mBitmap.getWidth() * mBitmap.getHeight())];
        Bitmap grayBitmap = toGrayscale(mBitmap);
        grayBitmap.getPixels(pixels, 0, mBitmap.getWidth(), 0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        format_K_dither16x16(pixels, grayBitmap.getWidth(), grayBitmap.getHeight(), data);
        return data;
    }

    private static void format_K_dither16x16_int(int[] orgpixels, int xsize, int ysize, int[] despixels) {
        int k = 0;
        for (int y = 0; y < ysize; y++) {
            for (int x = 0; x < xsize; x++) {
                if ((orgpixels[k] & 255) > Floyd16x16[x & 15][y & 15]) {
                    despixels[k] = -1;
                } else {
                    despixels[k] = -16777216;
                }
                k++;
            }
        }
    }

    private static void format_K_dither8x8_int(int[] orgpixels, int xsize, int ysize, int[] despixels) {
        int k = 0;
        for (int y = 0; y < ysize; y++) {
            for (int x = 0; x < xsize; x++) {
                if (((orgpixels[k] & 255) >> 2) > Floyd8x8[x & 7][y & 7]) {
                    despixels[k] = -1;
                } else {
                    despixels[k] = -16777216;
                }
                k++;
            }
        }
    }

    public static int[] bitmapToBWPix_int(Bitmap mBitmap, int algorithm) {
        int[] pixels = new int[0];
        switch (algorithm) {
            case 2:
                return pixels;
            case 8:
                Bitmap grayBitmap = toGrayscale(mBitmap);
                int[] pixels2 = new int[(grayBitmap.getWidth() * grayBitmap.getHeight())];
                grayBitmap.getPixels(pixels2, 0, grayBitmap.getWidth(), 0, 0, grayBitmap.getWidth(), grayBitmap.getHeight());
                format_K_dither8x8_int(pixels2, grayBitmap.getWidth(), grayBitmap.getHeight(), pixels2);
                return pixels2;
            default:
                Bitmap grayBitmap2 = toGrayscale(mBitmap);
                int[] pixels3 = new int[(grayBitmap2.getWidth() * grayBitmap2.getHeight())];
                grayBitmap2.getPixels(pixels3, 0, grayBitmap2.getWidth(), 0, 0, grayBitmap2.getWidth(), grayBitmap2.getHeight());
                format_K_dither16x16_int(pixels3, grayBitmap2.getWidth(), grayBitmap2.getHeight(), pixels3);
                return pixels3;
        }
    }

    public static Bitmap toBinaryImage(Bitmap mBitmap, int nWidth, int algorithm) {
        int width = ((nWidth + 7) / 8) * 8;
        int height = (mBitmap.getHeight() * width) / mBitmap.getWidth();
        Bitmap rszBitmap = resizeImage(mBitmap, width, height);
        rszBitmap.setPixels(bitmapToBWPix_int(rszBitmap, algorithm), 0, width, 0, 0, width, height);
        return rszBitmap;
    }
}
