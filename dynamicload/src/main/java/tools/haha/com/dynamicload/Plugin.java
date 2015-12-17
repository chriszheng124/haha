package tools.haha.com.dynamicload;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import tools.haha.com.dynamicload.hack.ClassLoaderHook;
import tools.haha.com.dynamicload.hack.InstrumentationHook;


public class Plugin {
    private Activity mActivity;
    private String mPluginPath;

    private ClassLoader mClassLoader;
    private static boolean sInstrumentationReady = false;
    private Object mLoadedApk;

    public Plugin(Activity activity, String dexPath){
        mActivity = activity;
        mClassLoader = new ClassLoaderHook(activity, dexPath);
        mPluginPath = dexPath;
    }

    public void load(){
        if(mLoadedApk != null){
            return;
        }
        try{
            if(!sInstrumentationReady) {
                replaceInstrumentation();
            }
        }catch (Exception e){
            if(PluginCfg.DEBUG){
                Log.v(PluginCfg.TAG, "loadPlugin failed " + e.getMessage());
            }
        }
    }

    public String getPluginPath(){
        return mPluginPath;
    }

    public Object getLoadedApk(){
        return mLoadedApk;
    }

    private void replaceInstrumentation(){
        replaceInstrumentationOfActivity();
        createLoadedApk();
        setClassLoader();
    }

    private void replaceInstrumentationOfActivity(){
        try{
            Field fieldInstrumentation = Activity.class.getDeclaredField("mInstrumentation");
            fieldInstrumentation.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation)fieldInstrumentation.get(mActivity);
            InstrumentationHook instrumentationHook = new InstrumentationHook(instrumentation, mClassLoader, this);
            fieldInstrumentation.set(mActivity, instrumentationHook);
            Field activityThread = Activity.class.getDeclaredField("mMainThread");
            activityThread.setAccessible(true);
            Object object = activityThread.get(mActivity);
            Field activityThreadInstrumentation = object.getClass().getDeclaredField("mInstrumentation");
            activityThreadInstrumentation.setAccessible(true);
            activityThreadInstrumentation.set(object, instrumentationHook);
            sInstrumentationReady = true;
        }catch (Exception e){
            if(PluginCfg.DEBUG){
                Log.v(PluginCfg.TAG, "replaceInstrumentationOfActivity error : " + e.getMessage());
            }
        }
    }

    private void createLoadedApk(){
        try {
            Method getCompatibilityInfoMethod = Resources.class.getDeclaredMethod("getCompatibilityInfo");
            getCompatibilityInfoMethod.setAccessible(true);
            Object compatibilityInfo = getCompatibilityInfoMethod.invoke(mActivity.getResources());

            Field activityThread = Activity.class.getDeclaredField("mMainThread");
            activityThread.setAccessible(true);
            Object activityThreadInstance = activityThread.get(mActivity);
            Class cls = Class.forName("android.content.res.CompatibilityInfo");
            Method getPackageInfoNoCheckMethod = Class.forName("android.app.ActivityThread").
                    getDeclaredMethod("getPackageInfoNoCheck", ApplicationInfo.class, cls);
            getPackageInfoNoCheckMethod.setAccessible(true);
            mLoadedApk = getPackageInfoNoCheckMethod.invoke(activityThreadInstance,
                    mActivity.getApplicationInfo(), compatibilityInfo);
        } catch (Exception e) {
            if(PluginCfg.DEBUG){
                e.printStackTrace();
            }
        }
    }

    private void setClassLoader(){
        try {
            Field mClassLoaderField = mLoadedApk.getClass().getDeclaredField("mClassLoader");
            mClassLoaderField.setAccessible(true);
            mClassLoaderField.set(mLoadedApk, mClassLoader);
        }catch (Exception e){
            if(PluginCfg.DEBUG){
                e.printStackTrace();
            }
        }
    }
}
