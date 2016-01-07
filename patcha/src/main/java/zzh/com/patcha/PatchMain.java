package zzh.com.patcha;

import android.graphics.Color;

import com.taobao.android.dexposed.DexposedBridge;
import com.taobao.android.dexposed.XC_MethodReplacement;

public class PatchMain {

    public void install() {
        XC_MethodReplacement methodReplacement = new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                return Color.WHITE;
            }
        };
        try {
            Class clazz = Class.forName("tools.haha.com.androidtools.demo.DemoMainActivity");
            DexposedBridge.findAndHookMethod(clazz, "getColor", methodReplacement);
        }catch (Exception e){
            throw new RuntimeException("install patch tools.haha.com.androidtools.demo.DemoMainActivity");
        }
    }
}
