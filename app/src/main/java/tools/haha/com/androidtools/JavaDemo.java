package tools.haha.com.androidtools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import tools.haha.com.dynamicload.PluginClassLoader;

public class JavaDemo {
    public void fun(){
        Log.v("test_11", "fun got called");
    }

    public static void fun1(List<? extends List<String>> param){
        Log.v("test_11", "classLoader in fun1: " + JavaDemo.class.getClassLoader().getParent());
    }

    public static void fun2(Context context){
        PluginClassLoader classLoader = PluginClassLoader.getLoader(context,
                "/sdcard/app-debug.apk", context.getClassLoader());
        Log.v("test_11", "customeLoader : " + classLoader.toString());
        try {
            Class<?> clazz = classLoader.loadClass("tools.haha.com.plugin_1.MainActivity");
            Log.v("test_11", "classLoader in fun2: " + clazz.getClassLoader());
            //JavaDemo obj1 = (JavaDemo)clazz.newInstance();
            //Log.v("test_11", obj1.toString());
            //JavaDemo instance = (JavaDemo)clazz.newInstance();
            //instance.fun();
        }catch (Exception e){
            Log.v("test_11", e.getMessage());
        }
//        Log.v("test_11", "getPackageCodePath"+context.getPackageCodePath());
//        Log.v("test_11", "getPackageName"+context.getPackageName());
//        Log.v("test_11", "getPackageResourcePath"+context.getPackageResourcePath());
//        ApplicationInfo applicationInfo = context.getApplicationInfo();
//        Log.v("test_11", "sourceDir "+applicationInfo.sourceDir);
//        Log.v("test_11", "dataDir "+applicationInfo.dataDir);
//        Log.v("test_11", "publicSourceDir "+applicationInfo.publicSourceDir);
//        Log.v("test_11", "taskAffinity "+applicationInfo.taskAffinity);
//        Log.v("test_11", "backupAgentName "+applicationInfo.backupAgentName);
    }
}
