package com.android.print.sdk.util;

import java.lang.reflect.Array;

public class DataConversion {
    public static String toHexString(byte[] arg, int length) {
        if (arg == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(length * 2);
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02x", new Object[]{new Integer(arg[i] & 255)}));
            sb.append(" ");
        }
        return sb.toString();
    }

    public static byte[] toByteArray(String arg) {
        if (arg != null) {
            char[] NewArray = new char[arg.length()];
            char[] array = arg.toCharArray();
            int length = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i] != ' ') {
                    NewArray[length] = array[i];
                    length++;
                }
            }
            int EvenLength = length % 2 == 0 ? length : length + 1;
            if (EvenLength != 0) {
                int[] data = new int[EvenLength];
                data[EvenLength - 1] = 0;
                for (int i2 = 0; i2 < length; i2++) {
                    if (NewArray[i2] >= '0' && NewArray[i2] <= '9') {
                        data[i2] = NewArray[i2] - '0';
                    } else if (NewArray[i2] >= 'a' && NewArray[i2] <= 'f') {
                        data[i2] = (NewArray[i2] - 'a') + 10;
                    } else if (NewArray[i2] >= 'A' && NewArray[i2] <= 'F') {
                        data[i2] = (NewArray[i2] - 'A') + 10;
                    }
                }
                byte[] byteArray = new byte[(EvenLength / 2)];
                for (int i3 = 0; i3 < EvenLength / 2; i3++) {
                    byteArray[i3] = (byte) ((data[i3 * 2] * 16) + data[(i3 * 2) + 1]);
                }
                return byteArray;
            }
        }
        return new byte[0];
    }

    public static String toAsciiString(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            sb.append(Integer.toHexString(s.charAt(i)));
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    public static byte[] intToByte4(int i) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (i & 255);
        targets[2] = (byte) ((i >> 8) & 255);
        targets[1] = (byte) ((i >> 16) & 255);
        targets[0] = (byte) ((i >> 24) & 255);
        return targets;
    }

    public static int checkSum(byte[] by) {
        int sum = 0;
        for (byte b : by) {
            sum += b & 255;
        }
        return sum;
    }

    public static <T> T invertArray(T array) {
        int len = Array.getLength(array);
        Object dest = Array.newInstance(array.getClass().getComponentType(), len);
        System.arraycopy(array, 0, dest, 0, len);
        for (int i = 0; i < len / 2; i++) {
            Object temp = Array.get(dest, i);
            Array.set(dest, i, Array.get(dest, (len - i) - 1));
            Array.set(dest, (len - i) - 1, temp);
        }
        return dest;
    }
}
