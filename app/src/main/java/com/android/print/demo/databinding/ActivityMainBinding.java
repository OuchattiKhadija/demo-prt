package com.android.print.demo.databinding;


import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.android.print.demov3.R;
import com.google.android.material.tabs.TabLayout;

public class ActivityMainBinding extends ViewDataBinding {
    @Nullable
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;
    @Nullable
    private static final SparseIntArray sViewsWithIds = new SparseIntArray();
    @NonNull
    public final Button bigData;
    @NonNull
    public final TextView connectAddress;
    @NonNull
    public final LinearLayout connectLayout;
    @NonNull
    public final TextView connectName;
    @NonNull
    public final TextView connectState;
    private long mDirtyFlags = -1;
    @NonNull
    private final LinearLayout mboundView0;
    @NonNull
    public final Button printImage;
    @NonNull
    public final Button printQr;
    @NonNull
    public final Button printText;
    @NonNull
    public final TabLayout tabLayout;
    @NonNull
    public final Button update;

    static {
        sViewsWithIds.put(R.id.tabLayout, 1);
        sViewsWithIds.put(R.id.connect_layout, 2);
        sViewsWithIds.put(R.id.connect_state, 3);
        sViewsWithIds.put(R.id.connect_name, 4);
        sViewsWithIds.put(R.id.connect_address, 5);
        sViewsWithIds.put(R.id.printText, 6);
        sViewsWithIds.put(R.id.printImage, 7);
        sViewsWithIds.put(R.id.print_qr, 8);
        sViewsWithIds.put(R.id.bigData, 9);
        sViewsWithIds.put(R.id.update, 10);
    }

    public ActivityMainBinding(@NonNull DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        Object[] bindings = mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds);
        this.bigData = (Button) bindings[9];
        this.connectAddress = (TextView) bindings[5];
        this.connectLayout = (LinearLayout) bindings[2];
        this.connectName = (TextView) bindings[4];
        this.connectState = (TextView) bindings[3];
        this.mboundView0 = (LinearLayout) bindings[0];
        this.mboundView0.setTag((Object) null);
        this.printImage = (Button) bindings[7];
        this.printQr = (Button) bindings[8];
        this.printText = (Button) bindings[6];
        this.tabLayout = (TabLayout) bindings[1];
        this.update = (Button) bindings[10];
        setRootTag(root);
        invalidateAll();
    }

    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 1;
        }
        requestRebind();
    }

    public boolean hasPendingBindings() {
        synchronized (this) {
            if (this.mDirtyFlags != 0) {
                return true;
            }
            return false;
        }
    }

    public boolean setVariable(int variableId, @Nullable Object variable) {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void executeBindings() {
        synchronized (this) {
            long dirtyFlags = this.mDirtyFlags;
            this.mDirtyFlags = 0;
        }
    }

    @NonNull
    public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent bindingComponent) {
        return (ActivityMainBinding) DataBindingUtil.inflate(inflater, R.layout.activity_main, root, attachToRoot, bindingComponent);
    }

    @NonNull
    public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
        return inflate(inflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater, @Nullable DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(R.layout.activity_main, (ViewGroup) null, false), bindingComponent);
    }

    @NonNull
    public static ActivityMainBinding bind(@NonNull View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityMainBinding bind(@NonNull View view, @Nullable DataBindingComponent bindingComponent) {
        if ("layout/activity_main_0".equals(view.getTag())) {
            return new ActivityMainBinding(bindingComponent, view);
        }
        throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
    }
}
