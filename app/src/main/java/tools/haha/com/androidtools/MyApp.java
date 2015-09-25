package tools.haha.com.androidtools;

import android.app.Application;
import android.content.Context;

@SuppressWarnings("unused")
public class MyApp extends Application{
    private static MyApp sThis;

    @Override
    public void onCreate() {
        super.onCreate();
        sThis = this;
    }

    public static Context getContext(){
        return sThis.getApplicationContext();
    }
}
