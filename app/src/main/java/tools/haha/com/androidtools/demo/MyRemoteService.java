package tools.haha.com.androidtools.demo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MyRemoteService extends Service{
    public static final String TAG = "_Plugin_";

    private RemoteBinder mBinder = new RemoteBinder();

    public class RemoteBinder extends Binder {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "MyRemoteService:onBind was called");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "MyRemoteService:onDestroyed was called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "MyRemoteService:onStartCommand was called");
        Toast.makeText(this, "service connected",
                Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "MyRemoteService:onCreate was called");
    }
}
