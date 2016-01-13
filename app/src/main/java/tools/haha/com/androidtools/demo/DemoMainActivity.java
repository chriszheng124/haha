package tools.haha.com.androidtools.demo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import tools.haha.com.androidtools.R;
import tools.haha.com.androidtools.ui.MyDrawerLayout;
import tools.haha.com.androidtools.ui.RoundedBitmapDrawable;


public class DemoMainActivity extends Activity implements View.OnClickListener{

    private MyLocalService mBoundService;
    private boolean mBound;
    private MyDrawerLayout mDrawer;

    public DemoMainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("dexposed", "DemoMainActivity::OnCreate was called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_main_activity_layout);
        View root = findViewById(R.id.root);
        root.setBackgroundColor(getColor());
        final ImageView imageView = (ImageView)findViewById(R.id.img_view_1);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
        RoundedBitmapDrawable roundedBitmapDrawable = new RoundedBitmapDrawable(getResources(), bitmap);
        roundedBitmapDrawable.setRadius(55);
        imageView.setImageDrawable(roundedBitmapDrawable);

        mDrawer = (MyDrawerLayout)findViewById(R.id.drawer);
    }

    private int getColor(){
        return Color.BLACK;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((MyLocalService.LocalBinder)service).getService();
            mBoundService.testService();
            //mBound = true;

            Toast.makeText(DemoMainActivity.this, "service connected",
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
            //mBound = false;
            Toast.makeText(DemoMainActivity.this, "service disconnected",
                    Toast.LENGTH_SHORT).show();
        }
    };

    private ServiceConnection mRemoteConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Toast.makeText(DemoMainActivity.this, "service connected",
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            Toast.makeText(DemoMainActivity.this, "service disconnected",
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_list_view:
                startActivity(ListViewActivity.class);
                break;
            case R.id.start_local_service:
                Intent intent = new Intent();
                intent.setClassName(this, MyLocalService.class.getName());
                startService(intent);
                break;
            case R.id.bind_local_service:
                Intent intent2 = new Intent();
                intent2.setClassName(this, MyLocalService.class.getName());
                mBound = bindService(intent2, mConnection, BIND_AUTO_CREATE);
                break;
            case R.id.scroll_view_button:
                startActivity(MyScrollViewActivity.class);
                break;
            case R.id.img_view_1:
                startActivity(TestActivity.class);
                break;
            case R.id.start_remote_service:
                Intent intent3 = new Intent();
                intent3.setClassName(this, MyRemoteService.class.getName());
                startService(intent3);
                break;
            case R.id.bind_remote_service:
                Intent intent4 = new Intent();
                intent4.setClassName(this, MyRemoteService.class.getName());
                bindService(intent4, mRemoteConnection, BIND_AUTO_CREATE);
                break;
            case R.id.btn_perf_test:
                Intent intent5 = new Intent();
                intent5.setClassName(this, PerfTestDemo.class.getName());
                startActivity(intent5);
                break;
            case R.id.btn_open_drawer:
                mDrawer.setVisibility(View.VISIBLE);
                break;
            case R.id.start_drawer_activity:
                Intent intent6 = new Intent();
                intent6.setClassName(this, DrawerActivity.class.getName());
                startActivity(intent6);
                break;
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBound){
            unbindService(mConnection);
        }
    }

    private void startActivity(Class<?> clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
