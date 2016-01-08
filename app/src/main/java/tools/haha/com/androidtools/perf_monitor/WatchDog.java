package tools.haha.com.androidtools.perf_monitor;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import tools.haha.com.androidtools.utils.DumpStackTrace;


public class WatchDog extends HandlerThread{
    private static final long CHECK_INTERVAL = 1000;
    private Handler mTargetHandler = new Handler(Looper.getMainLooper());
    private Handler mHandler;
    private WatchTask mTask = new WatchTask();
    private boolean mHasProcessedByTarget;

    public WatchDog(String name, int priority) {
        super(name, priority);
    }

    @Override
    public void start(){
        super.start();
        mHandler = new Handler(getLooper());
        watch();
    }

    private void watch(){
        mHandler.postDelayed(mTask, CHECK_INTERVAL);
        mTargetHandler.post(mTask);
        mHasProcessedByTarget = false;
    }

    private class WatchTask implements Runnable{
        @Override
        public void run() {
            synchronized (this){
                if(Looper.myLooper() == Looper.getMainLooper()){
                    mHasProcessedByTarget = true;
                }else {
                    if(!mHasProcessedByTarget){
                        StackTraceElement[] stackTraceElements = Looper.getMainLooper().getThread().getStackTrace();
                        mTargetHandler.removeCallbacks(mTask);
                        throw new RuntimeException(DumpStackTrace.dump(stackTraceElements));
                    }
                    watch();
                }
            }
        }
    }
}
