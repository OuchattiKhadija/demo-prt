package com.android.print.sdk;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import java.util.Locale;

public class CanvasPrint {
    private Bitmap bitmap;
    private Canvas canvas;
    private float currentY;
    private float length = 0.0f;
    public Paint mPaint;
    private String splitStr = " ";
    private boolean textAlignRight;
    private boolean textExceedNewLine = true;
    private int textSize;
    private boolean useSplit;
    private int width;

    public int getLength() {
        return (int) this.length;
    }

    public int getWidth() {
        return this.width;
    }

    public void initCanvas(int w) {
        this.bitmap = Bitmap.createBitmap(w, w * 5, Bitmap.Config.ARGB_4444);
        this.canvas = new Canvas(this.bitmap);
        this.canvas.drawColor(-1);
    }

    public void initPaint() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(-7829368);
    }

    public void init(PrinterType printerType) {
        if (printerType == PrinterType.T9) {
            this.width = 576;
        } else if (printerType == PrinterType.T5) {
            this.width = 240;
        } else {
            this.width = 384;
        }
        initCanvas(this.width);
        initPaint();
    }

    public void setFontProperty(FontProperty fp) {
        if (fp.sFace != null) {
            try {
                this.mPaint.setTypeface(fp.sFace);
            } catch (Exception e) {
                this.mPaint.setTypeface(Typeface.DEFAULT);
            }
        } else {
            this.mPaint.setTypeface(Typeface.DEFAULT);
        }
        if (fp.bBold) {
            this.mPaint.setFakeBoldText(true);
        } else {
            this.mPaint.setFakeBoldText(false);
        }
        if (fp.bItalic) {
            this.mPaint.setTextSkewX(-0.5f);
        } else {
            this.mPaint.setTextSkewX(0.0f);
        }
        if (fp.bUnderLine) {
            this.mPaint.setUnderlineText(true);
        } else {
            this.mPaint.setUnderlineText(false);
        }
        if (fp.bStrikeout) {
            this.mPaint.setStrikeThruText(true);
        } else {
            this.mPaint.setStrikeThruText(false);
        }
        this.mPaint.setTextSize((float) fp.iSize);
    }

    public void setLineWidth(float w) {
        this.mPaint.setStrokeWidth(w);
    }

    public void setTextSize(int size) {
        this.textSize = size;
        this.mPaint.setTextSize((float) this.textSize);
    }

    public void setItalic(boolean italic) {
        if (italic) {
            this.mPaint.setTextSkewX(-0.5f);
        } else {
            this.mPaint.setTextSkewX(0.0f);
        }
    }

    public void setStrikeThruText(boolean strike) {
        this.mPaint.setStrikeThruText(strike);
    }

    public void setUnderlineText(boolean underline) {
        this.mPaint.setUnderlineText(underline);
    }

    public void setFakeBoldText(boolean fakeBold) {
        this.mPaint.setFakeBoldText(fakeBold);
    }

    public void setUseSplit(boolean useSplit2) {
        this.useSplit = useSplit2;
    }

    public void setUseSplitAndString(boolean useSplit2, String splitStr2) {
        this.useSplit = useSplit2;
        this.splitStr = splitStr2;
    }

    public void setTextExceedNewLine(boolean newLine) {
        this.textExceedNewLine = newLine;
    }

    public void setTextAlignRight(boolean alignRight) {
        this.textAlignRight = alignRight;
    }

    public void drawText(int x, int y, String string) {
        this.currentY += getFontHeight();
        int validWidth = this.width - x;
        float textWidth = getTextWidth(string);
        if (this.textExceedNewLine) {
            while (true) {
                int pos = getValidStringPos(string, validWidth);
                if (pos <= 0 || textWidth <= 0.0f) {
                    this.currentY -= getFontHeight();
                } else {
                    String printStr = string.substring(0, pos);
                    if (this.textAlignRight) {
                        this.canvas.drawText(printStr, ((float) x) + (((float) validWidth) - getTextWidth(printStr)), (float) y, this.mPaint);
                    } else {
                        this.canvas.drawText(printStr, (float) x, (float) y, this.mPaint);
                    }
                    string = string.substring(pos, string.length());
                    textWidth -= (float) validWidth;
                    this.currentY = ((float) y) + getFontHeight();
                }
            }
            this.currentY -= getFontHeight();
        } else if (this.textAlignRight) {
            this.canvas.drawText(string, ((float) x) + (((float) validWidth) - textWidth), (float) y, this.mPaint);
        } else {
            this.canvas.drawText(string, (float) x, (float) y, this.mPaint);
        }
        if (this.length < this.currentY) {
            this.length = this.currentY;
        }
    }

    public void drawText(int x, String string) {
        this.currentY += getFontHeight();
        int validWidth = this.width - x;
        float textWidth = getTextWidth(string);
        if (this.textExceedNewLine) {
            while (true) {
                int pos = getValidStringPos(string, validWidth);
                if (pos <= 0 || textWidth <= 0.0f) {
                    this.currentY -= getFontHeight();
                } else {
                    String printStr = string.substring(0, pos);
                    if (this.textAlignRight) {
                        this.canvas.drawText(printStr, ((float) x) + (((float) validWidth) - getTextWidth(printStr)), this.currentY, this.mPaint);
                    } else {
                        this.canvas.drawText(printStr, (float) x, this.currentY, this.mPaint);
                    }
                    string = string.substring(pos, string.length());
                    textWidth -= (float) validWidth;
                    this.currentY += getFontHeight();
                }
            }
            this.currentY -= getFontHeight();
        } else if (this.textAlignRight) {
            this.canvas.drawText(string, ((float) x) + (((float) validWidth) - textWidth), this.currentY, this.mPaint);
        } else {
            this.canvas.drawText(string, (float) x, this.currentY, this.mPaint);
        }
        if (this.length < this.currentY) {
            this.length = this.currentY;
        }
    }

    public void drawText(String string) {
        this.currentY += getFontHeight();
        int validWidth = this.width;
        float textWidth = getTextWidth(string);
        if (this.textExceedNewLine) {
            int pos = 0;
            do {
                String printStr = string.substring(0, pos);
                if (this.textAlignRight) {
                    this.canvas.drawText(printStr, ((float) validWidth) - getTextWidth(printStr), this.currentY, this.mPaint);
                } else {
                    this.canvas.drawText(printStr, 0.0f, this.currentY, this.mPaint);
                }
                string = string.substring(pos, string.length());
                textWidth -= (float) validWidth;
                if (!string.isEmpty()) {
                    this.currentY += getFontHeight();
                }
                pos = getValidStringPos(string, validWidth);
                if (pos <= 0) {
                    break;
                }
            } while (textWidth > 0.0f);
        } else if (this.textAlignRight) {
            this.canvas.drawText(string, ((float) validWidth) - textWidth, this.currentY, this.mPaint);
        } else {
            this.canvas.drawText(string, 0.0f, this.currentY, this.mPaint);
        }
        if (this.length < this.currentY) {
            this.length = this.currentY;
        }
    }

    public void drawLine(float startX, float startY, float stopX, float stopY) {
        float max;
        this.canvas.drawLine(startX, startY, stopX, stopY, this.mPaint);
        if (startY > stopY) {
            max = startY;
        } else {
            max = stopY;
        }
        if (this.length < max) {
            this.length = max;
        }
    }

    public void drawRectangle(float left, float top, float right, float bottom) {
        float max;
        this.canvas.drawRect(left, top, right, bottom, this.mPaint);
        if (top > bottom) {
            max = top;
        } else {
            max = bottom;
        }
        if (this.length < max) {
            this.length = max;
        }
    }

    public void drawEllips(float left, float top, float right, float bottom) {
        float max;
        this.canvas.drawOval(new RectF(left, top, right, bottom), this.mPaint);
        if (top > bottom) {
            max = top;
        } else {
            max = bottom;
        }
        if (this.length < max) {
            this.length = max;
        }
    }

    public void drawImage(Bitmap image) {
        this.canvas.drawBitmap(image, 0.0f, this.currentY, (Paint) null);
        this.currentY += (float) image.getHeight();
        if (this.length < this.currentY) {
            this.length = this.currentY;
        }
    }

    public void drawImage(int left, Bitmap image) {
        this.canvas.drawBitmap(image, (float) left, this.currentY, (Paint) null);
        this.currentY += (float) image.getHeight();
        if (this.length < this.currentY) {
            this.length = this.currentY;
        }
    }

    public void drawImage(int left, float top, Bitmap image) {
        this.canvas.drawBitmap(image, (float) left, top, (Paint) null);
        this.currentY += (float) image.getHeight();
        if (this.length < this.currentY) {
            this.length = this.currentY;
        }
    }

    public Bitmap getCanvasImage() {
        return Bitmap.createBitmap(this.bitmap, 0, 0, this.width, getLength());
    }

    private float getTextWidth(String text) {
        return this.mPaint.measureText(text);
    }

    public float getCurrentPointY() {
        return this.currentY;
    }

    private float getFontHeight() {
        Paint.FontMetrics fm = this.mPaint.getFontMetrics();
        return (float) Math.ceil((double) (fm.descent - fm.ascent));
    }

    private float getCharacterWidth() {
        float spacing = this.mPaint.getFontSpacing();
        String lang = Locale.getDefault().getLanguage();
        if (lang.equals("ja") || lang.equals("ko") || lang.equals("zh")) {
            return spacing;
        }
        return spacing / 2.0f;
    }

    private int getValidStringPos(String string, int validWidth) {
        float textWidth = getTextWidth(string);
        while (textWidth > 0.0f && textWidth > ((float) validWidth)) {
            int subPos = (int) (((float) (string.length() * validWidth)) / textWidth);
            string = string.substring(0, subPos);
            textWidth = getTextWidth(string);
            if (textWidth <= ((float) validWidth)) {
                if (!this.useSplit || !string.contains(this.splitStr)) {
                    return subPos;
                }
                return string.lastIndexOf(this.splitStr);
            }
        }
        return string.length();
    }
}
