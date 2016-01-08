package tools.haha.com.androidtools.hotfix;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import tools.haha.com.androidtools.BusFactory;

public class PatchLoader {
    private static final String PATCH_ENTRY_METHOD = "install";
    private Context mContext;
    private String mApkPath;
    private String mClassName;

    public PatchLoader(Context context, String apkPath, String className){
        mContext = context;
        mApkPath = apkPath;
        mClassName = className;
    }

    public void load(){
        new AsyncTask<Void, Void, Void>(){
            PatchNotification.PatchEvent event;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    run(mContext, mApkPath, mClassName);
                }catch (HotfixException e){
                    event = new PatchNotification.PatchFail(e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(event == null){
                    event = new PatchNotification.PatchSuccess();
                }
                BusFactory.getBus().post(event);
            }
        }.execute();
    }

    public void run(Context context, String apkPath, String className)throws HotfixException {
        if (!new File(apkPath).exists()) {
            throw new HotfixException("File " + apkPath + " not exist", HotfixException.APK_FILE_NOT_EXIST);
        }
        String odexPath = context.getFilesDir().getAbsolutePath();
        DexClassLoader classLoader = new DexClassLoader(apkPath, odexPath, null, context.getClassLoader());
        Class<?> clazz;
        try {
            clazz = classLoader.loadClass(className);
        } catch (Exception e) {
            throw new HotfixException("cannot load class " + className);
        }
        if (clazz != null) {
            try {
                Object patch = clazz.newInstance();
                Method method = clazz.getDeclaredMethod(PATCH_ENTRY_METHOD);
                method.setAccessible(true);
                method.invoke(patch);
            } catch (Exception e) {
                throw new HotfixException("newInstance class " + className + " failed");
            }
        }
    }
}
