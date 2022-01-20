package com.tcl.base.utils;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;

/**
 * desc   :
 * author : tanksu
 * date   : 2019-11-17
 */
public class AppGlobals {
    private static Application sApplication;

    public static Application getApplication() {
        if (sApplication == null) {
            try {
                sApplication = (Application) Class.forName("android.app.ActivityThread")
                        .getMethod("currentApplication")
                        .invoke(null, (Object[]) null);
            } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return sApplication;
    }
}
