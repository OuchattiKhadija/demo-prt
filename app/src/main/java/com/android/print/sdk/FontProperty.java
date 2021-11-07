package com.android.print.sdk;

import android.content.Context;
import android.graphics.Typeface;

public class FontProperty {
    boolean bBold;
    boolean bItalic;
    boolean bStrikeout;
    boolean bUnderLine;
    int iSize;
    Typeface sFace;

    public void setFont(boolean bBold2, boolean bItalic2, boolean bUnderLine2, boolean bStrikeout2, int iSize2, Typeface sFace2) {
        this.bBold = bBold2;
        this.bItalic = bItalic2;
        this.bUnderLine = bUnderLine2;
        this.bStrikeout = bStrikeout2;
        this.iSize = iSize2;
        this.sFace = sFace2;
    }

    public void initTypeface(Context mContext, String path) {
        this.sFace = Typeface.createFromAsset(mContext.getAssets(), path);
    }

    public void initTypefaceToString(String familyName, int style) {
        this.sFace = Typeface.create(familyName, style);
    }

    public void setBold(boolean bBold2) {
        this.bBold = bBold2;
    }

    public void setItalic(boolean bItalic2) {
        this.bItalic = bItalic2;
    }

    public void setUnderLine(boolean bUnderLine2) {
        this.bUnderLine = bUnderLine2;
    }

    public void setStrikeout(boolean bStrikeout2) {
        this.bStrikeout = bStrikeout2;
    }

    public void setSize(int iSize2) {
        this.iSize = iSize2;
    }

    public void setFace(Typeface sFace2) {
        this.sFace = sFace2;
    }
}
