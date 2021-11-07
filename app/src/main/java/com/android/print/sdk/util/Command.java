package com.android.print.sdk.util;

import android.graphics.Bitmap;
import android.util.Log;
import java.util.Vector;

public class Command {
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public Vector<Byte> Command = new Vector<>();

    private void addArrayToCommand(byte[] array) {
        for (byte valueOf : array) {
            this.Command.add(Byte.valueOf(valueOf));
        }
    }

    public Command addBitImage(Bitmap bitmap) {
        return addRastBitImage(bitmap, bitmap.getWidth(), 0);
    }

    public Command addRastBitImage(Bitmap bitmap, int nWidth, int nMode) {
        if (bitmap != null) {
            int width = ((nWidth + 7) / 8) * 8;
            byte[] src = MdUtils.bitmapToBWPix(MdUtils.resizeImage(MdUtils.toGrayscale(bitmap), width, (bitmap.getHeight() * width) / bitmap.getWidth()));
            int height = src.length / width;
            addArrayToCommand(new byte[]{29, 118, 48, (byte) (nMode & 1), (byte) ((width / 8) % 256), (byte) ((width / 8) / 256), (byte) (height % 256), (byte) (height / 256)});
            byte[] codecontent = MdUtils.pixToEscRastBitImageCmd(src);
            for (byte valueOf : codecontent) {
                this.Command.add(Byte.valueOf(valueOf));
            }
        } else {
            Log.d("BMP", "bmp.  null ");
        }
        return this;
    }

    public byte[] getCommand() {
        return toPrimitive((Byte[]) this.Command.toArray(new Byte[this.Command.size()]));
    }

    public Command addApplication(byte[] fileByte) {
        addArrayToCommand(new byte[]{27, 35, 35, 85, 80, 80, 71});
        addArrayToCommand((byte[]) DataConversion.invertArray(DataConversion.intToByte4(DataConversion.checkSum(fileByte))));
        addArrayToCommand((byte[]) DataConversion.invertArray(DataConversion.intToByte4(fileByte.length)));
        addArrayToCommand(fileByte);
        return this;
    }

    public static byte[] toPrimitive(Byte[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].byteValue();
        }
        return result;
    }
}
