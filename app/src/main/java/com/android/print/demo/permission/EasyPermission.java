package com.android.print.demo.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.app.AlertDialog;

import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EasyPermission {
    public static final int SETTINGS_REQ_CODE = 16061;
    @StringRes
    private int mNegativeButtonText = 17039360;
    private String[] mPermissions;
    @StringRes
    private int mPositiveButtonText = 17039370;
    private String mRationale;
    private int mRequestCode;
    private Object object;

    public interface PermissionCallback extends ActivityCompat.OnRequestPermissionsResultCallback {
        void onPermissionDenied(int i, List<String> list);

        void onPermissionGranted(int i, List<String> list);
    }

    private EasyPermission(Object object2) {
        this.object = object2;
    }

    public static EasyPermission with(Activity activity) {
        return new EasyPermission(activity);
    }

    public static EasyPermission with(Fragment fragment) {
        return new EasyPermission(fragment);
    }

    public EasyPermission permissions(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    public EasyPermission rationale(String rationale) {
        this.mRationale = rationale;
        return this;
    }

    public EasyPermission addRequestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    public EasyPermission positveButtonText(@StringRes int positiveButtonText) {
        this.mPositiveButtonText = positiveButtonText;
        return this;
    }

    public EasyPermission nagativeButtonText(@StringRes int negativeButtonText) {
        this.mNegativeButtonText = negativeButtonText;
        return this;
    }

    public void request() {
        requestPermissions(this.object, this.mRationale, this.mPositiveButtonText, this.mNegativeButtonText, this.mRequestCode, this.mPermissions);
    }

    public static boolean hasPermissions(Context context, String... perms) {
        boolean hasPerm;
        if (!Utils.isOverMarshmallow()) {
            return true;
        }
        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(context, perm) == 0) {
                hasPerm = true;
            } else {
                hasPerm = false;
            }
            if (!hasPerm) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissions(Object object2, String rationale, int requestCode, String... perms) {
        requestPermissions(object2, rationale, 17039370, 17039360, requestCode, perms);
    }

    public static void requestPermissions(final Object object2, String rationale, @StringRes int positiveButton, @StringRes int negativeButton, final int requestCode, String... permissions) {
        checkCallingObjectSuitability(object2);
        PermissionCallback mCallBack = (PermissionCallback) object2;
        if (!Utils.isOverMarshmallow()) {
            mCallBack.onPermissionGranted(requestCode, Arrays.asList(permissions));
            return;
        }
        List<String> deniedPermissions = Utils.findDeniedPermissions(Utils.getActivity(object2), permissions);
        boolean shouldShowRationale = false;
        for (String perm : deniedPermissions) {
            if (shouldShowRationale || Utils.shouldShowRequestPermissionRationale(object2, perm)) {
                shouldShowRationale = true;
            } else {
                shouldShowRationale = false;
            }
        }
        if (Utils.isEmpty(deniedPermissions)) {
            mCallBack.onPermissionGranted(requestCode, Arrays.asList(permissions));
            return;
        }
        final String[] deniedPermissionArray = (String[]) deniedPermissions.toArray(new String[deniedPermissions.size()]);
        if (shouldShowRationale) {
            Activity activity = Utils.getActivity(object2);
            if (activity != null) {
                AlertDialog dialog = new AlertDialog.Builder(activity).setMessage((CharSequence) rationale).setPositiveButton(positiveButton, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EasyPermission.executePermissionsRequest(object2, deniedPermissionArray, requestCode);
                    }
                }).setCancelable(false).create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                return;
            }
            return;
        }
        executePermissionsRequest(object2, deniedPermissionArray, requestCode);
    }

    /* access modifiers changed from: private */
    @TargetApi(23)
    public static void executePermissionsRequest(Object object2, String[] perms, int requestCode) {
        checkCallingObjectSuitability(object2);
        if (object2 instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object2, perms, requestCode);
        } else if (object2 instanceof Fragment) {
            ((Fragment) object2).requestPermissions(perms, requestCode);
        } else if (object2 instanceof android.app.Fragment) {
            ((android.app.Fragment) object2).requestPermissions(perms, requestCode);
        }
    }

    public static void onRequestPermissionsResult(Object object2, int requestCode, String[] permissions, int[] grantResults) {
        checkCallingObjectSuitability(object2);
        PermissionCallback mCallBack = (PermissionCallback) object2;
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != 0) {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (Utils.isEmpty(deniedPermissions)) {
            mCallBack.onPermissionGranted(requestCode, Arrays.asList(permissions));
        } else {
            mCallBack.onPermissionDenied(requestCode, deniedPermissions);
        }
    }

    public static boolean checkDeniedPermissionsNeverAskAgain(Object object2, String rationale, @StringRes int positiveButton, @StringRes int negativeButton, List<String> deniedPerms) {
        return checkDeniedPermissionsNeverAskAgain(object2, rationale, positiveButton, negativeButton, (DialogInterface.OnClickListener) null, deniedPerms);
    }

    public static boolean checkDeniedPermissionsNeverAskAgain(final Object object2, String rationale, @StringRes int positiveButton, @StringRes int negativeButton, @Nullable DialogInterface.OnClickListener negativeButtonOnClickListener, List<String> deniedPerms) {
        for (String perm : deniedPerms) {
            if (!Utils.shouldShowRequestPermissionRationale(object2, perm)) {
                final Activity activity = Utils.getActivity(object2);
                if (activity == null) {
                    return true;
                }
                AlertDialog dialog = new AlertDialog.Builder(activity).setMessage((CharSequence) rationale).setPositiveButton(positiveButton, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", activity.getPackageName(), (String) null));
                        EasyPermission.startAppSettingsScreen(object2, intent);
                    }
                }).setNegativeButton(negativeButton, negativeButtonOnClickListener).setCancelable(false).create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    @TargetApi(11)
    public static void startAppSettingsScreen(Object object2, Intent intent) {
        if (object2 instanceof Activity) {
            ((Activity) object2).startActivityForResult(intent, SETTINGS_REQ_CODE);
        } else if (object2 instanceof Fragment) {
            ((Fragment) object2).startActivityForResult(intent, SETTINGS_REQ_CODE);
        } else if (object2 instanceof android.app.Fragment) {
            ((android.app.Fragment) object2).startActivityForResult(intent, SETTINGS_REQ_CODE);
        }
    }

    private static void checkCallingObjectSuitability(Object object2) {
        if (!(object2 instanceof Fragment) && !(object2 instanceof Activity) && !(object2 instanceof android.app.Fragment)) {
            throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
        } else if (!(object2 instanceof PermissionCallback)) {
            throw new IllegalArgumentException("Caller must implement PermissionCallback.");
        }
    }
}
