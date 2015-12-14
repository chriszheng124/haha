package tools.haha.com.androidtools;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.os.Trace;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

@SuppressWarnings("unused")
public class MyApp extends Application{
    private static MyApp sThis;
    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    //.detectActivityLeaks()
                    //.setClassInstanceLimit(MainActivity.class, 1)
                    .penaltyDropBox()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        sThis = this;

        mRefWatcher = LeakCanary.install(this);

        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                    .enableDumpapp(
                            Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(
                            Stetho.defaultInspectorModulesProvider(this))
                    .build());
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
