package tools.haha.com.androidtools.perf_monitor;

import android.os.*;
import android.util.Log;

import com.taobao.android.dexposed.DexposedBridge;
import com.taobao.android.dexposed.XC_MethodHook;

import java.lang.reflect.Field;
import java.util.HashMap;

public class RunnableMetrics {
    private HashMap<String, Long> mBeginTimeMap = new HashMap<>();

    private XC_MethodHook methodHook = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            Message message = (Message)param.args[0];
            try {
                Field callbackField = message.getClass().getDeclaredField("callback");
                callbackField.setAccessible(true);
                Runnable runnable = (Runnable)callbackField.get(message);
                mBeginTimeMap.put(runnable.getClass().getName(), System.currentTimeMillis());
            }catch (Exception e){
                Log.e("RunnableMetrics", "beforeHookedMethod failed " + e.getMessage());
            }
            super.beforeHookedMethod(param);
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            long endTime = System.currentTimeMillis();
            Message message = (Message)param.args[0];
            try {
                Field callbackField = message.getClass().getDeclaredField("callback");
                callbackField.setAccessible(true);
                Runnable runnable = (Runnable)callbackField.get(message);
                long duration = endTime - mBeginTimeMap.get(runnable.getClass().getName());
                if(duration > 1000){
                    Log.e("RunnableMetrics", ">>>>>>>>>>>>>Run task { " + runnable.getClass().getName()
                            + " } using " + duration + " ms");
                }
            }catch (Exception e){
                Log.e("RunnableMetrics", "afterHookedMethod failed " + e.getMessage());
            }
        }
    };

    public void start(){
        try {
            Class<?> clazz = Class.forName("android.os.Handler");
            DexposedBridge.findAndHookMethod(clazz, "handleCallback", Message.class, methodHook);
        }catch (Exception e){
            Log.e("RunnableMetrics", "start failed " + e.getMessage());
        }
    }
}
