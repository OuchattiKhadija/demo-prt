package com.android.print.demo.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public final class Utils {
    private Utils() {
    }

    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= 23;
    }

    @TargetApi(23)
    public static List<String> findDeniedPermissions(Activity activity, String... permission) {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permission) {
            if (activity.checkSelfPermission(value) != 0) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    @TargetApi(23)
    public static boolean shouldShowRequestPermissionRationale(Object object, String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        }
        if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        }
        if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).shouldShowRequestPermissionRationale(perm);
        }
        return false;
    }

    @TargetApi(11)
    public static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        }
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        }
        if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).getActivity();
        }
        return null;
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }
}
