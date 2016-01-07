package tools.haha.com.androidtools.hotfix;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PatchLoader {
    private static final String PATCH_MAIN = "zzh.com.patcha.PatchMain";
    private static final String PATCH_ENTRY_METHOD = "install";

    public static void load(Context context, String apkPath)throws HotfixException {
        if (!new File(apkPath).exists()) {
            throw new HotfixException("File " + apkPath + " not exist", HotfixException.APK_FILE_NOT_EXIST);
        }
        String odexPath = context.getFilesDir().getAbsolutePath();
        DexClassLoader classLoader = new DexClassLoader(apkPath, odexPath, null, context.getClassLoader());
        Class<?> clazz;
        try {
            clazz = classLoader.loadClass(PATCH_MAIN);
        } catch (Exception e) {
            throw new HotfixException("cannot load class " + PATCH_MAIN);
        }
        if (clazz != null) {
            try {
                Object patch = clazz.newInstance();
                Method method = clazz.getDeclaredMethod(PATCH_ENTRY_METHOD);
                method.setAccessible(true);
                method.invoke(patch);
            } catch (Exception e) {
                throw new HotfixException("newInstance class " + PATCH_MAIN + " failed");
            }
        }
    }
}
