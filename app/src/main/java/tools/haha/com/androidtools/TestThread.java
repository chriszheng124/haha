package tools.haha.com.androidtools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class TestThread extends Thread{
    private Context mContext;

    public void init(Context context){
        mContext = context;
    }
    @Override
    public void run() {
        while (!isInterrupted()){
            //Log.v("test_11", "running...");
        }
        //Log.v("test_11", "end.........................");
        try {
            // no loop created !!!
            Toast.makeText(mContext, "thread is killed", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
