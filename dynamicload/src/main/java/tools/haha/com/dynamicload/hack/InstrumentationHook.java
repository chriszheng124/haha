package tools.haha.com.dynamicload.hack;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;

import tools.haha.com.dynamicload.Plugin;
import tools.haha.com.dynamicload.PluginCfg;
import tools.haha.com.dynamicload.ProxyActivity;

public class InstrumentationHook extends Instrumentation {
    private static final String ORIGINAL_CLS_KEY = "original_cls";
    private Instrumentation mInstrumentation;
    private ClassLoader mLoader;
    private Plugin mPlugin;
    private HashSet<Activity> mActivitySet = new HashSet<>();
    private Object mLoadedApk;

    public InstrumentationHook(Instrumentation instrumentation, ClassLoader loader, Plugin plugin){
        mInstrumentation = instrumentation;
        mLoader = loader;
        mPlugin = plugin;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        createLoadedApkInfo(target);
        if(intent.hasExtra(ORIGINAL_CLS_KEY)){
            intent.removeExtra(ORIGINAL_CLS_KEY);
        }
        ActivityInfo activityInfo = intent.resolveActivityInfo(who.getPackageManager(), 0);
        if(activityInfo == null && intent.getComponent() != null){
            String className = intent.getComponent().getClassName();
            String pkgName = intent.getComponent().getPackageName();
            if(!TextUtils.isEmpty(className) && !TextUtils.isEmpty(pkgName)){
                if(className.contains(".")){
                    className = className.substring(className.lastIndexOf(".")+1);
                }
                StringBuilder sb = new StringBuilder();
                sb.append(pkgName).append(".").append(className);
                intent.putExtra(ORIGINAL_CLS_KEY, sb.toString());
                intent.setClassName(who, ProxyActivity.class.getName());
            }else {
                return null;
            }
        }
        try {
            Method method = Instrumentation.class.getDeclaredMethod("execStartActivity",
                    Context.class, IBinder.class, IBinder.class,
                    Activity.class, Intent.class, int.class, Bundle.class);
            return (ActivityResult)method.invoke(mInstrumentation, who,
                    contextThread, token, target, intent, requestCode, options);
        }catch (Exception e){
            return null;
        }
    }

    public Activity newActivity(Class<?> clazz, Context context,
                                IBinder token, Application application, Intent intent, ActivityInfo info,
                                CharSequence title, Activity parent, String id,
                                Object lastNonConfigurationInstance) throws InstantiationException,
            IllegalAccessException {
        try{
            Method method = Instrumentation.class.getDeclaredMethod(
                    "newActivity", ClassLoader.class, String.class, Intent.class);
            //return (Activity)method.invoke(mInstrumentation, cl, className, intent);
            return null;

//            return (Activity)SysHacks.Instrumentation.method(
//                    "newActivity", ClassLoader.class, Context.class, IBinder.class, Application.class,
//                    Intent.class, ActivityInfo.class, CharSequence.class, Activity.class, String.class, Objects.class)
//                    .invoke(mInstrumentation, context, token, application, info, info, title,
//                            parent, id, lastNonConfigurationInstance);
        }catch (Exception e){
            return null;
        }
    }

    public Activity newActivity(ClassLoader cl, String className,Intent intent)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try{
            boolean isPluginActivity = false;
            String cls = intent.getStringExtra(ORIGINAL_CLS_KEY);
            if(!TextUtils.isEmpty(cls)){
                className = cls;
                isPluginActivity = true;
            }
            Method method = Instrumentation.class.getDeclaredMethod(
                    "newActivity", ClassLoader.class, String.class, Intent.class);
            Activity activity = (Activity)method.invoke(mInstrumentation, mLoader, className, intent);
            if(isPluginActivity) {
                mActivitySet.add(activity);
            }
            return activity;

        }catch (Exception e){
            if(PluginCfg.DEBUG){
                Log.v(PluginCfg.TAG, "newActivity error : " + e.getMessage());
            }
            return null;
        }
    }

    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        try {
            if(mActivitySet.contains(activity)) {
                hackContextThemeWrapper(activity);
                hackContextImpl(activity);
            }
            mInstrumentation.callActivityOnCreate(activity, icicle);
        }catch (Exception e){
            if(PluginCfg.DEBUG){
                e.printStackTrace();
                Log.v(PluginCfg.TAG, "callActivityOnCreate error : " + e.getMessage());
            }
        }
    }

    public void callActivityOnCreate(Activity activity, Bundle icicle,
                                     PersistableBundle persistentState) {
        mInstrumentation.callActivityOnCreate(activity, icicle, persistentState);
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            PackageInfo packageInfo = getPackageInfo(context, mPlugin.getPluginPath());
            className = packageInfo.applicationInfo.className;
        }catch (Exception e){
            if(PluginCfg.DEBUG){
                e.printStackTrace();
            }
            return null;
        }
        return mInstrumentation.newApplication(cl, className, context);
    }

    private void hackContextThemeWrapper(Activity activity){
        try {
            Field resourceField = ContextThemeWrapper.class.getDeclaredField("mResources");
            resourceField.setAccessible(true);
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            AssetManager assetManager = AssetManager.class.newInstance();
            method.invoke(assetManager, mPlugin.getPluginPath());
            Resources resources = new Resources(assetManager,
                    activity.getResources().getDisplayMetrics(),
                    activity.getResources().getConfiguration());
            resourceField.set(activity, resources);
        }catch (Exception e){
            if(PluginCfg.DEBUG){
                Log.v(PluginCfg.TAG, "hackContextThemeWrapper error : " + e.getMessage());
            }
        }
    }

    private void hackContextImpl(Activity activity){
        try {
            Class<?> clazz = Class.forName("android.app.ContextImpl");
            Field loadedApkField = clazz.getDeclaredField("mPackageInfo");
            loadedApkField.setAccessible(true);
            loadedApkField.set(activity.getBaseContext(), mLoadedApk);
            makeApplication(activity);
        }catch (Exception e){
            if(PluginCfg.DEBUG){
                Log.v(PluginCfg.TAG, "hackActivityThread error : " + e.getMessage());
            }
        }
    }

    private void createLoadedApkInfo(Activity activity){
        try {
            Class<?> loadApkClazz = Class.forName("android.app.LoadedApk");
            Constructor<?> constructor = loadApkClazz.getDeclaredConstructor(
                    Class.forName("android.app.ActivityThread"),
                    ApplicationInfo.class,
                    Class.forName("android.content.res.CompatibilityInfo"),
                    Class.forName("android.app.ActivityThread"),
                    ClassLoader.class,
                    boolean.class,
                    boolean.class);
            Class<?> contextClazz = Class.forName("android.app.ContextImpl");
            Field loadedApkField = contextClazz.getDeclaredField("mPackageInfo");
            loadedApkField.setAccessible(true);
            Object apkInfo = loadedApkField.get(activity.getBaseContext());

            ApplicationInfo applicationInfo = activity.getApplicationInfo();

            Method getCompatibilityInfoMethod = Resources.class.getDeclaredMethod("getCompatibilityInfo");
            mLoadedApk = constructor.newInstance(
                    getFieldValue(apkInfo.getClass(), apkInfo, "mActivityThread"),
                    applicationInfo,
                    getCompatibilityInfoMethod.invoke(activity.getResources()),
                    getFieldValue(apkInfo.getClass(), apkInfo, "mActivityThread"),
                    mLoader,
                    getFieldValue(apkInfo.getClass(), apkInfo, "mSecurityViolation"),
                    getFieldValue(apkInfo.getClass(), apkInfo, "mIncludeCode"));
            assignValue(mLoadedApk, "mPackageName", getPackageInfo(activity,
                    mPlugin.getPluginPath()).activities[0].packageName);
        }catch (Exception e){
        }
    }

    private void makeApplication(Activity activity){
        try {
            Class<?> loadApkClazz = Class.forName("android.app.LoadedApk");
            Method makeApplicationMethod = loadApkClazz.getDeclaredMethod(
                    "makeApplication", boolean.class, Instrumentation.class);
            Application app = (Application)makeApplicationMethod.invoke(mLoadedApk, false, mInstrumentation);
            Field appLoadedApkField = Application.class.getDeclaredField("mLoadedApk");
            appLoadedApkField.set(app, mLoadedApk);
        }catch (Exception e){
        }
    }

//    private Object getPluginApkInfo(Activity activity){
//        Object loadedApk;
//        try {
//            Class<?> loadApkClazz = Class.forName("android.app.LoadedApk");
//            Constructor<?> constructor = loadApkClazz.getDeclaredConstructor(
//                    Class.forName("android.app.ActivityThread"),
//                    ApplicationInfo.class,
//                    Class.forName("android.content.res.CompatibilityInfo"),
//                    Class.forName("android.app.ActivityThread"),
//                    ClassLoader.class,
//                    boolean.class,
//                    boolean.class);
//            Class<?> contextClazz = Class.forName("android.app.ContextImpl");
//            Field loadedApkField = contextClazz.getDeclaredField("mPackageInfo");
//            loadedApkField.setAccessible(true);
//            Object apkInfo = loadedApkField.get(activity.getBaseContext());
//
//            ApplicationInfo applicationInfo = activity.getApplicationInfo();
//
//            Method getCompatibilityInfoMethod = Resources.class.getDeclaredMethod("getCompatibilityInfo");
//            loadedApk = constructor.newInstance(
//                    getFieldValue(apkInfo.getClass(), apkInfo, "mActivityThread"),
//                    applicationInfo,
//                    getCompatibilityInfoMethod.invoke(activity.getResources()),
//                    getFieldValue(apkInfo.getClass(), apkInfo, "mActivityThread"),
//                    mLoader,
//                    getFieldValue(apkInfo.getClass(), apkInfo, "mSecurityViolation"),
//                    getFieldValue(apkInfo.getClass(), apkInfo, "mIncludeCode"));
//            assignValue(loadedApk, "mPackageName", getPackageInfo(activity,
//                    mPlugin.getPluginPath()).activities[0].packageName);
//
//            Method makeApplicationMethod = loadApkClazz.getDeclaredMethod(
//                    "makeApplication", boolean.class, Instrumentation.class);
//            Application app = (Application)makeApplicationMethod.invoke(loadedApk, false, mInstrumentation);
//            Field appLoadedApkField = Application.class.getDeclaredField("mLoadedApk");
//            appLoadedApkField.set(app, loadedApk);
//
//        }catch (Exception e){
//            return null;
//        }
//        return loadedApk;
//    }

    private Object getFieldValue(Class clazz, Object object, String fieldName){
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        }catch (Exception e){
            return null;
        }
    }

    private ApplicationInfo getApplicationInfo(Object apkInfo, String appName){
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = (ApplicationInfo)getFieldValue(apkInfo.getClass(),
                    apkInfo, "mApplicationInfo");
            Field appNameField = ApplicationInfo.class.getDeclaredField("className");
            appNameField.set(applicationInfo, appName);
        }catch (Exception e){
            return null;
        }
        return applicationInfo;
    }

    private void assignValue(Object dst, String fieldName, Object value){
        try {
            Field field = dst.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(dst, value);
        }catch (Exception e){
        }
    }

    public static PackageInfo getPackageInfo(Context cxt, String apkPath)
            throws PackageManager.NameNotFoundException {
        return cxt.getPackageManager().getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
    }

//    private Object getPluginApkInfo(Activity activity){
//        Object loadedApk;
//        try {
//            Class<?> loadApkClazz = Class.forName("android.app.LoadedApk");
//            Constructor<?> constructor = loadApkClazz.getDeclaredConstructor(Class.forName("android.app.ActivityThread"));
//            Class<?> contextClazz = Class.forName("android.app.ContextImpl");
//            Field loadedApkField = contextClazz.getDeclaredField("mPackageInfo");
//            loadedApkField.setAccessible(true);
//            Object apkInfo = loadedApkField.get(activity.getBaseContext());
//            Field field = loadApkClazz.getDeclaredField("mActivityThread");
//            field.setAccessible(true);
//            loadedApk = constructor.newInstance(field.get(apkInfo));
//
//            assignValue(apkInfo, loadedApk, "mApplicationInfo");
//            assignValue(apkInfo, loadedApk, "");
//
//        }catch (Exception e){
//            return null;
//        }
//        return loadedApk;
//    }
//
//    private void assignValue(Object src, Object dst, String fieldName){
//        try {
//            Field field = src.getClass().getDeclaredField(fieldName);
//            field.setAccessible(true);
//            field.set(dst, field.get(dst));
//        }catch (Exception e){
//
//        }
//    }

}
