package com.android.print.sdk;

import com.android.print.sdk.PrinterConstants;
import com.android.print.sdk.util.Utils;
import java.io.UnsupportedEncodingException;

public class Barcode {
    private static final String TAG = "Barcode";
    private byte barcodeType;
    private String charsetName = "gbk";
    private String content;
    private int param1;
    private int param2;
    private int param3;

    public Barcode(byte barcodeType2) {
        this.barcodeType = barcodeType2;
    }

    public Barcode(byte barcodeType2, int param12, int param22, int param32) {
        this.barcodeType = barcodeType2;
        this.param1 = param12;
        this.param2 = param22;
        this.param3 = param32;
    }

    public Barcode(byte barcodeType2, int param12, int param22, int param32, String content2) {
        this.barcodeType = barcodeType2;
        this.param1 = param12;
        this.param2 = param22;
        this.param3 = param32;
        this.content = content2;
    }

    public void setBarcodeParam(byte param12, byte param22, byte param32) {
        this.param1 = param12;
        this.param2 = param22;
        this.param3 = param32;
    }

    public void setBarcodeContent(String content2) {
        this.content = content2;
    }

    public void setBarcodeContent(String content2, String charsetName2) {
        this.content = content2;
        this.charsetName = charsetName2;
    }

    public byte[] getBarcodeData() {
        byte[] realCommand;
        int index;
        int index2;
        switch (this.barcodeType) {
            case 72:
                realCommand = getBarcodeCommand1(this.content, new byte[]{this.barcodeType, (byte) this.content.length()});
                break;
            case 73:
                byte[] tempCommand = new byte[1024];
                int index3 = 0;
                int strLength = this.content.length();
                int tempLength = strLength;
                char[] charArray = this.content.toCharArray();
                boolean preHasCodeA = false;
                boolean preHasCodeB = false;
                boolean preHasCodeC = false;
                boolean needCodeC = false;
                int i = 0;
                while (true) {
                    int index4 = index3;
                    if (i >= strLength) {
                        realCommand = getBarcodeCommand1(new String(tempCommand, 0, tempLength), new byte[]{this.barcodeType, (byte) tempLength});
                        break;
                    } else {
                        byte a = (byte) charArray[i];
                        if (a < 0 || a > 31) {
                            if (a >= 48 && a <= 57) {
                                if (!preHasCodeC) {
                                    int m = 1;
                                    while (true) {
                                        if (m < 9) {
                                            if (i + m == strLength || !Utils.isNum((byte) charArray[i + m])) {
                                                needCodeC = false;
                                            } else {
                                                if (m == 8) {
                                                    needCodeC = true;
                                                }
                                                m++;
                                            }
                                        }
                                    }
                                    needCodeC = false;
                                }
                                if (needCodeC) {
                                    if (!preHasCodeC) {
                                        int index5 = index4 + 1;
                                        tempCommand[index4] = 123;
                                        index4 = index5 + 1;
                                        tempCommand[index5] = 67;
                                        preHasCodeA = false;
                                        preHasCodeB = false;
                                        preHasCodeC = true;
                                        tempLength += 2;
                                    }
                                    if (i != strLength - 1) {
                                        byte b = (byte) charArray[i + 1];
                                        if (Utils.isNum(b)) {
                                            index3 = index4 + 1;
                                            tempCommand[index4] = (byte) (((a - 48) * 10) + (b - 48));
                                            tempLength--;
                                            i++;
                                        }
                                    }
                                }
                            }
                            if (!preHasCodeB) {
                                int index6 = index4 + 1;
                                tempCommand[index4] = 123;
                                tempCommand[index6] = 66;
                                preHasCodeA = false;
                                preHasCodeB = true;
                                preHasCodeC = false;
                                tempLength += 2;
                                index = index6 + 1;
                            } else {
                                index = index4;
                            }
                            tempCommand[index] = a;
                            index3 = index + 1;
                        } else {
                            if (i == 0 || !preHasCodeA) {
                                int index7 = index4 + 1;
                                tempCommand[index4] = 123;
                                tempCommand[index7] = 65;
                                preHasCodeA = true;
                                preHasCodeB = false;
                                preHasCodeC = false;
                                tempLength += 2;
                                index2 = index7 + 1;
                            } else {
                                index2 = index4;
                            }
                            tempCommand[index2] = a;
                            index3 = index2 + 1;
                        }
                        i++;
                    }
                }
                break;
            case 100:
            case 101:
            case 102:
                realCommand = getBarcodeCommand2(this.content, this.barcodeType, this.param1, this.param2, this.param3);
                break;
            default:
                realCommand = getBarcodeCommand1(this.content, new byte[]{this.barcodeType});
                break;
        }
        if (PrinterInstance.DEBUG) {
            StringBuffer sb = new StringBuffer();
            for (byte b2 : realCommand) {
                String temp = Integer.toHexString(b2 & 255);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                sb.append(String.valueOf(temp) + " ");
            }
            Utils.Log(TAG, "bar code command: " + sb.toString());
        }
        return realCommand;
    }

    private byte[] getBarcodeCommand1(String content2, byte[] byteArray) {
        byte[] tmpByte;
        int index;
        int index2;
        int index3;
        try {
            if (this.charsetName != "") {
                tmpByte = content2.getBytes(this.charsetName);
            } else {
                tmpByte = content2.getBytes();
            }
            byte[] command = new byte[(tmpByte.length + 13)];
            int index4 = 0 + 1;
            command[0] = 29;
            int index5 = index4 + 1;
            command[index4] = 119;
            if (this.param1 < 2 || this.param1 > 6) {
                command[index5] = 2;
                index = index5 + 1;
            } else {
                command[index5] = (byte) this.param1;
                index = index5 + 1;
            }
            int index6 = index + 1;
            command[index] = 29;
            int index7 = index6 + 1;
            command[index6] = 104;
            if (this.param2 < 1 || this.param2 > 255) {
                command[index7] = -94;
                index2 = index7 + 1;
            } else {
                command[index7] = (byte) this.param2;
                index2 = index7 + 1;
            }
            int index8 = index2 + 1;
            command[index2] = 29;
            int index9 = index8 + 1;
            command[index8] = PrinterConstants.BarcodeType.CODE93;
            if (this.param3 < 0 || this.param3 > 3) {
                command[index9] = 0;
                index3 = index9 + 1;
            } else {
                command[index9] = (byte) this.param3;
                index3 = index9 + 1;
            }
            int index10 = index3 + 1;
            command[index3] = 29;
            int index11 = index10 + 1;
            command[index10] = 107;
            int i = 0;
            while (i < byteArray.length) {
                command[index11] = byteArray[i];
                i++;
                index11++;
            }
            int j = 0;
            while (j < tmpByte.length) {
                command[index11] = tmpByte[j];
                j++;
                index11++;
            }
            return command;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] getBarcodeCommand2(String content2, byte barcodeType2, int param12, int param22, int param32) {
        byte[] tmpByte;
        try {
            if (this.charsetName != "") {
                tmpByte = content2.getBytes(this.charsetName);
            } else {
                tmpByte = content2.getBytes();
            }
            byte[] command = new byte[(tmpByte.length + 10)];
            command[0] = 29;
            command[1] = 90;
            command[2] = (byte) (barcodeType2 - 100);
            command[3] = 27;
            command[4] = 90;
            command[5] = (byte) param12;
            command[6] = (byte) param22;
            command[7] = (byte) param32;
            command[8] = (byte) (tmpByte.length % 256);
            command[9] = (byte) (tmpByte.length / 256);
            System.arraycopy(tmpByte, 0, command, 10, tmpByte.length);
            return command;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
