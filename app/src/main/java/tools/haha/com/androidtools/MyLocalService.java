package tools.haha.com.androidtools;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MyLocalService extends Service{
    public static final String TAG = "_Plugin_";

    private LocalBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        MyLocalService getService() {
            return MyLocalService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "MyLocalService:onBind was called");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "MyLocalService:onDestroyed was called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "MyLocalService:onStartCommand was called");
        Toast.makeText(this, "service connected",
                Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "MyLocalService:onCreate was called");
    }

    public MyLocalService() {
        super();
    }

    public void testService(){
        Toast.makeText(this, getResources().getString(R.string.startLocalService),
                Toast.LENGTH_SHORT).show();

//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setClassName(this, TestActivity.class.getName());
//        startActivity(intent);
    }
}
