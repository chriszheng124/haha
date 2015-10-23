package tools.haha.com.androidtools;

import android.app.Application;
import android.content.Context;

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
}
