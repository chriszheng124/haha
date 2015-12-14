package tools.haha.com.dynamicload;

import android.content.Context;
import android.util.ArrayMap;

import dalvik.system.DexClassLoader;

public class PluginClassLoader extends DexClassLoader{
    private final static String OPT_DIR = "plugin_opt_dex_dir";
    private final static String LIB_DIR = "plugin_lib_dir";

    private static ArrayMap<String, PluginClassLoader> sLoaders = new ArrayMap<>();

    public PluginClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
    }

    public static PluginClassLoader getLoader(Context context, String dexPath, ClassLoader parentLoader){
        PluginClassLoader loader = sLoaders.get(dexPath);
        if(loader == null){
            String optimizedDirectory = context.getDir(OPT_DIR, Context.MODE_PRIVATE).getAbsolutePath();
            String libraryPath = context.getDir(LIB_DIR, Context.MODE_PRIVATE).getAbsolutePath();
            loader = new PluginClassLoader(dexPath, optimizedDirectory, libraryPath, parentLoader);
            sLoaders.put(dexPath, loader);
        }
        return loader;
    }
}
