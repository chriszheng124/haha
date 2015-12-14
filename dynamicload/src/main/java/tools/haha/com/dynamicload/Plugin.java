package tools.haha.com.dynamicload;

import android.app.Activity;
import android.app.Instrumentation;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;

import tools.haha.com.dynamicload.hack.InstrumentationHook;


public class Plugin {
    private Activity mActivity;
    private String mPluginPath;

    private ClassLoader mLoader;
    private static boolean sInstrumentationReady = false;

    public Plugin(Activity activity){
        mActivity = activity;
    }

    public void load(String dexPath){
        if(!TextUtils.isEmpty(mPluginPath) && dexPath.equalsIgnoreCase(mPluginPath)){
            return;
        }
        mPluginPath = dexPath;
        mLoader = PluginClassLoader.getLoader(mActivity, mPluginPath, mActivity.getClassLoader());
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

    private void replaceInstrumentation(){
        try{
            Field fieldInstrumentation = Activity.class.getDeclaredField("mInstrumentation");
            fieldInstrumentation.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation)fieldInstrumentation.get(mActivity);
            InstrumentationHook instrumentationHook = new InstrumentationHook(instrumentation, mLoader, this);
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
                Log.v(PluginCfg.TAG, "replaceInstrumentation error : " + e.getMessage());
            }
        }
    }

//    private void replaceResource(){
//        try {
//            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
//            mAssetManager = AssetManager.class.newInstance();
//            method.invoke(mAssetManager, mPluginPath);
//            mResource = new Resources(mAssetManager,
//                    mActivity.getResources().getDisplayMetrics(),
//                    mActivity.getResources().getConfiguration());
//        }catch (Exception e){
//            if(PluginCfg.DEBUG){
//                Log.v(PluginCfg.TAG, "addAssetPath failed " + e.getMessage());
//            }
//        }
//    }

//    private void replaceResource(){
//        try {
//            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
//            Field contextField = ContextWrapper.class.getDeclaredField("mBase");
//            contextField.setAccessible(true);
//            Context context = (Context)contextField.get(mActivity);
//            mAssetManager = context.getAssets();
//            method.invoke(mAssetManager, mPluginPath);
//            mResource = new Resources(mAssetManager,
//                    mActivity.getResources().getDisplayMetrics(),
//                    mActivity.getResources().getConfiguration());
//        }catch (Exception e){
//            if(PluginCfg.DEBUG){
//                Log.v(PluginCfg.TAG, "addAssetPath failed " + e.getMessage());
//            }
//        }
//    }

}
