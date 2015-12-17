package tools.haha.com.dynamicload.hack;

import android.content.Context;

import tools.haha.com.dynamicload.PluginClassLoader;

public class ClassLoaderHook extends ClassLoader{

    private PluginClassLoader mLoader;

    public ClassLoaderHook(Context context, String path) {
        super(context.getClassLoader());
        mLoader = PluginClassLoader.getLoader(context, path, Object.class.getClassLoader());
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        return super.loadClass(className);
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        return mLoader.findClass(className);
    }
}
