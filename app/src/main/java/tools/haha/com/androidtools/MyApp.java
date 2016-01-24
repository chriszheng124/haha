package tools.haha.com.androidtools;

import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.squareup.leakcanary.RefWatcher;
import com.taobao.android.dexposed.DexposedBridge;

import tools.haha.com.androidtools.perf_monitor.RunnableMetrics;
import tools.haha.com.androidtools.perf_monitor.WatchDog;

@SuppressWarnings("unused")
public class MyApp extends Application{
    public static boolean sCanDexPosed;
    private static MyApp sThis;
    private RefWatcher mRefWatcher;
    private WatchDog mWatchDog = new WatchDog("watchDog", Process.THREAD_PRIORITY_DISPLAY);
    private RunnableMetrics mRunnableMetrics = new RunnableMetrics();

    @Override
    public void onCreate() {
        Log.v("_Plugin_", "MyApp:onCreate was called");
        super.onCreate();
        //mWatchDog.start();
        if(DexposedBridge.canDexposed(this)){
            sCanDexPosed = true;
            //mRunnableMetrics.start();
        }
//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()
//                    .detectDiskWrites()
//                    .detectNetwork()   // or .detectAll() for all detectable problems
//                    .penaltyLog()
//                    .build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectLeakedSqlLiteObjects()
//                    .detectLeakedClosableObjects()
//                    //.detectActivityLeaks()
//                    //.setClassInstanceLimit(DemoMainActivity.class, 1)
//                    .penaltyDropBox()
//                    .penaltyLog()
//                    .penaltyDeath()
//                    .build());
//        }
        sThis = this;

//        mRefWatcher = LeakCanary.install(this);
//
//        Stetho.initialize(
//            Stetho.newInitializerBuilder(this)
//                    .enableDumpapp(
//                            Stetho.defaultDumperPluginsProvider(this))
//                    .enableWebKitInspector(
//                            Stetho.defaultInspectorModulesProvider(this))
//                    .build());
    }

    public static Context getContext(){
        return sThis;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        //TRIM_MEMORY_UI_HIDDEN
    }
}
